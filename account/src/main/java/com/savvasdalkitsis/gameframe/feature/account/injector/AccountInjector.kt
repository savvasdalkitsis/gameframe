package com.savvasdalkitsis.gameframe.feature.account.injector

import com.savvasdalkitsis.gameframe.feature.account.navigation.AccountNavigator
import com.savvasdalkitsis.gameframe.feature.account.navigation.AndroidAccountNavigator
import com.savvasdalkitsis.gameframe.feature.account.presenter.AccountPresenter
import com.savvasdalkitsis.gameframe.feature.authentication.injector.AuthenticationInjector.authenticationUseCase
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector.messageDisplay
import com.savvasdalkitsis.gameframe.feature.workspace.injector.WorkspaceInjector.firebaseWorkspaceStorage
import com.savvasdalkitsis.gameframe.feature.workspace.injector.WorkspaceInjector.workspaceUseCase
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector.topActivityProvider

object AccountInjector {

    fun accountNavigator(): AccountNavigator = AndroidAccountNavigator(topActivityProvider(), application())

    fun accountPresenter() = AccountPresenter(authenticationUseCase(), workspaceUseCase(), firebaseWorkspaceStorage(), messageDisplay())
}