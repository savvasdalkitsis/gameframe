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
package com.savvasdalkitsis.gameframe.feature.account.presenter

import android.util.Log
import com.savvasdalkitsis.gameframe.base.BasePresenter
import com.savvasdalkitsis.gameframe.base.plusAssign
import com.savvasdalkitsis.gameframe.feature.account.model.Account
import com.savvasdalkitsis.gameframe.feature.account.model.SignedInAccount
import com.savvasdalkitsis.gameframe.feature.account.usecase.AuthenticationCase
import com.savvasdalkitsis.gameframe.feature.account.view.AccountView
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class AccountPresenter<in AuthenticationData>(private val authenticationCase: AuthenticationCase<AuthenticationData>) : BasePresenter<AccountView>() {

    fun start() {
        view?.displayLoading()
        view?.let { bindEvents(it) }
        managedStreams += authenticationCase.accountState()
                .compose(RxTransformers.schedulersFlowable<Account>())
                .subscribe(::accountRetrieved, ::accountRetrieveFailed)
    }

    private fun bindEvents(view: AccountView) {
        managedStreams += view.logIn.apply(::throttle).subscribe { authenticationCase.signIn() }
        managedStreams += view.logOut.apply(::throttle).subscribe { authenticationCase.signOut() }
        managedStreams += view.deleteAccount.apply(::throttle).subscribe {
            view.askUserToVerifyAccountDeletion {
                authenticationCase.deleteAccount()
            }
        }
    }

    private fun accountRetrieved(account: Account) {
        when (account) {
            is SignedInAccount -> view?.displaySignedInAccount(account)
            else -> view?.displaySignedOut()
        }
    }

    private fun accountRetrieveFailed(error: Throwable) {
        Log.w(AccountPresenter<*>::javaClass.name, "Could not load account details", error)
        view?.displayErrorLoadingAccount()
    }

    fun handleResultForLoginRequest(requestCode: Int, resultCode: Int, authenticationData: AuthenticationData?) {
        authenticationCase.handleResult(requestCode, resultCode, authenticationData)
    }

    private fun throttle(stream: Observable<Unit>) {
        stream.throttleFirst(200, TimeUnit.MILLISECONDS)
    }
}