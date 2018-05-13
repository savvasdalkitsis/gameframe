/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.feature.authentication.usecase

import android.app.Activity
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.savvasdalkitsis.gameframe.feature.authentication.R
import com.savvasdalkitsis.gameframe.feature.authentication.model.*
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor

const val SIGN_IN = 123

class FirebaseAuthenticationUseCase(private val topActivityProvider: TopActivityProvider) : AuthenticationUseCase<Intent> {
    private var firebaseAuth = FirebaseAuth.getInstance()

    init {
        firebaseAuth.addAuthStateListener {
            val user = it.currentUser
            accountStates.onNext(when (user) {
                null -> SignedOutAccount()
                else -> SignedInAccount(name = user.displayName, id = user.uid, image = user.photoUrl)
            })
        }
    }

    private val activity: FragmentActivity?
        get() {
            val topActivity = topActivityProvider.topActivity
            return when (topActivity) {
                is FragmentActivity -> topActivity
                else -> null
            }
        }

    private val providers = listOf(
            AuthUI.EMAIL_PROVIDER,
            AuthUI.GOOGLE_PROVIDER,
            AuthUI.FACEBOOK_PROVIDER,
            AuthUI.TWITTER_PROVIDER)
            .map { AuthUI.IdpConfig.Builder(it).build() }

    override fun userId(): Single<String> = accountStates.firstOrError()
            .flatMap {
                when (it) {
                    is SignedInAccount -> Single.just(it.id)
                    else -> Single.error(IllegalStateException("User state was not logged in"))
                }
            }
            .onErrorResumeNext(Single.error(UserNotLoggedInException("Could not retrieve user id from firebase authentication")))

    private val accountStates = BehaviorProcessor.create<Account>()

    override fun accountState(): Flowable<Account> = accountStates

    override fun signIn() {
        activity?.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.mipmap.ic_launcher)
                        .setTheme(R.style.AppTheme)
                        .build(),
                SIGN_IN)
    }

    override fun signOut() {
        executeTask { (auth, activity) -> auth.signOut(activity) }
    }

    override fun deleteAccount() {
        executeTask { (auth, activity) -> auth.delete(activity) }
    }

    private fun executeTask(task: (Pair<AuthUI, FragmentActivity>) -> Task<Void>) {
        activity?.let { fragmentActivity ->
            val success = AuthUI.getInstance()
                    .run { task(Pair(this, fragmentActivity)) }.isSuccessful
            if (!success) {
                accountStates.onNext(AccountStateError())
            }
        }
    }

    override fun handleResult(requestCode: Int, resultCode: Int, authenticationData: Intent?) {
        if (requestCode == SIGN_IN && resultCode != Activity.RESULT_OK) {
            val response = IdpResponse.fromResultIntent(authenticationData)
            Log.w(FirebaseAuthenticationUseCase::class.java.name, "Error logging in ${response?.errorCode}")
            accountStates.onNext(AccountStateError())
        }
    }
}