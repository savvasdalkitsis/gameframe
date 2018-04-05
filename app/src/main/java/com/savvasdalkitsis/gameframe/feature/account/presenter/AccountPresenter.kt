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
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.authentication.model.Account
import com.savvasdalkitsis.gameframe.feature.authentication.model.SignedInAccount
import com.savvasdalkitsis.gameframe.feature.authentication.usecase.AuthenticationUseCase
import com.savvasdalkitsis.gameframe.feature.account.view.AccountView
import com.savvasdalkitsis.gameframe.feature.message.MessageDisplay
import com.savvasdalkitsis.gameframe.feature.workspace.model.SaveContainer
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceItem
import com.savvasdalkitsis.gameframe.feature.workspace.storage.WorkspaceStorage
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.WorkspaceUseCase
import com.savvasdalkitsis.gameframe.infra.base.BasePresenter
import com.savvasdalkitsis.gameframe.infra.base.plusAssign
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import io.reactivex.Flowable

private val TAG = AccountPresenter<*>::javaClass.name

class AccountPresenter<in AuthenticationData>(private val authenticationUseCase: AuthenticationUseCase<AuthenticationData>,
                                              private val workspaceUseCase: WorkspaceUseCase,
                                              private val remoteWorkspaceStorage: WorkspaceStorage,
                                              private val messageDisplay: MessageDisplay) : BasePresenter<AccountView>() {

    fun start() {
        view?.displayLoading()
        managedStreams += authenticationUseCase.accountState()
                .compose(RxTransformers.schedulersFlowable<Account>())
                .subscribe(::accountRetrieved, ::accountRetrieveFailed)
    }

    fun logIn() {
        authenticationUseCase.signIn()
    }

    fun logOut() {
        authenticationUseCase.signOut()
    }

    fun reUpload() {
        uploadLocallySavedProjects()
    }

    fun deleteAccount() {
        view?.askUserToVerifyAccountDeletion {
            authenticationUseCase.deleteAccount()
        }
    }

    private fun accountRetrieved(account: Account) {
        when (account) {
            is SignedInAccount -> {
                view?.displaySignedInAccount(account)
                uploadLocallySavedProjects()
            }
            else -> view?.displaySignedOut()
        }
    }

    private fun accountRetrieveFailed(error: Throwable) {
        Log.w(TAG, "Could not load account details", error)
        view?.displayErrorLoadingAccount()
    }

    private fun uploadLocallySavedProjects() {
        workspaceUseCase.locallySavedProjects()
                .compose(RxTransformers.schedulers<List<WorkspaceItem>>())
                .subscribe(::savedProjectsLoaded, {})
    }

    private fun savedProjectsLoaded(projects: List<WorkspaceItem>) {
        if (!projects.isEmpty()) {
            view?.askUserToUploadSavedProjects {
                messageDisplay.show(R.string.projects_upload_started)
                Flowable.fromIterable(projects)
                        .flatMapCompletable { (name, model) ->
                            remoteWorkspaceStorage.saveWorkspace(name, SaveContainer(model))
                        }
                        .compose(RxTransformers.schedulers())
                        .subscribe({
                            messageDisplay.show(R.string.projects_upload_finished)
                        }, {
                            Log.w(TAG, "Failed to upload some projects", it)
                            messageDisplay.show(R.string.projects_failed_to_upload)
                        })
            }
        } else {
            messageDisplay.show(R.string.no_projects_to_upload)
        }
    }

    fun handleResultForLoginRequest(requestCode: Int, resultCode: Int, authenticationData: AuthenticationData?) {
        authenticationUseCase.handleResult(requestCode, resultCode, authenticationData)
    }
}