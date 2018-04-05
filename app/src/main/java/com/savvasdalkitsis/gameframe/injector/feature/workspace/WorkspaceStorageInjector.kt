package com.savvasdalkitsis.gameframe.injector.feature.workspace

import com.savvasdalkitsis.gameframe.feature.authentication.injector.AuthenticationInjector
import com.savvasdalkitsis.gameframe.feature.authentication.injector.AuthenticationInjector.authenticationUseCase
import com.savvasdalkitsis.gameframe.feature.storage.injector.StorageInjector.localStorageUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.storage.AuthenticationAwareWorkspaceStorage
import com.savvasdalkitsis.gameframe.feature.workspace.storage.FirebaseWorkspaceStorage
import com.savvasdalkitsis.gameframe.feature.workspace.storage.LocalWorkspaceStorage
import com.savvasdalkitsis.gameframe.injector.infra.parsing.GsonInjector.gson

object WorkspaceStorageInjector {

    fun workspaceStorage() = AuthenticationAwareWorkspaceStorage(authenticationUseCase(), localWorkspaceStorage(), firebaseWorkspaceStorage())

    fun localWorkspaceStorage() = LocalWorkspaceStorage(gson(), localStorageUseCase())

    fun firebaseWorkspaceStorage() = FirebaseWorkspaceStorage(authenticationUseCase(), gson())
}

