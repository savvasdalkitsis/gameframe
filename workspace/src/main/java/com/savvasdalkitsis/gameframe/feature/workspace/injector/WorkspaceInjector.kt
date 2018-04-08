package com.savvasdalkitsis.gameframe.feature.workspace.injector

import android.view.Menu
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.savvasdalkitsis.gameframe.feature.authentication.injector.AuthenticationInjector.authenticationUseCase
import com.savvasdalkitsis.gameframe.feature.bitmap.injector.BitmapInjector.bitmapFileUseCase
import com.savvasdalkitsis.gameframe.feature.composition.CompositionInjector.blendUseCase
import com.savvasdalkitsis.gameframe.feature.composition.model.BlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.PorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.device.injector.DeviceInjector.deviceCase
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipNavigator
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector.messageDisplay
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector.wifiUseCase
import com.savvasdalkitsis.gameframe.feature.storage.injector.StorageInjector.localStorageUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.feature.workspace.navigation.AndroidWorkspaceNavigator
import com.savvasdalkitsis.gameframe.feature.workspace.navigation.WorkspaceNavigator
import com.savvasdalkitsis.gameframe.feature.workspace.presenter.WorkspacePresenter
import com.savvasdalkitsis.gameframe.feature.workspace.serialization.BlendModeAdapter
import com.savvasdalkitsis.gameframe.feature.workspace.serialization.PorterDuffAdapter
import com.savvasdalkitsis.gameframe.feature.workspace.serialization.WorkspaceModelAdapter
import com.savvasdalkitsis.gameframe.feature.workspace.storage.AuthenticationAwareWorkspaceStorage
import com.savvasdalkitsis.gameframe.feature.workspace.storage.FirebaseWorkspaceStorage
import com.savvasdalkitsis.gameframe.feature.workspace.storage.LocalWorkspaceStorage
import com.savvasdalkitsis.gameframe.feature.workspace.usecase.WorkspaceUseCase
import com.savvasdalkitsis.gameframe.infra.android.StringUseCase
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector.topActivityProvider

object WorkspaceInjector {

    fun workspaceNavigator(): WorkspaceNavigator = AndroidWorkspaceNavigator(topActivityProvider(), application())

    fun workspacePresenter() = WorkspacePresenter<Menu, View>(deviceCase(), blendUseCase(),
            workspaceUseCase(), stringUseCase(), messageDisplay(), workspaceNavigator(),
            ipNavigator(), bitmapFileUseCase(), wifiUseCase())

    fun workspaceUseCase() = WorkspaceUseCase(gson(), workspaceStorage(), localWorkspaceStorage())

    fun firebaseWorkspaceStorage() = FirebaseWorkspaceStorage(authenticationUseCase(), gson())

    private fun localWorkspaceStorage() = LocalWorkspaceStorage(gson(), localStorageUseCase())

    private fun stringUseCase() = StringUseCase(application())

    private fun workspaceStorage() = AuthenticationAwareWorkspaceStorage(authenticationUseCase(),
            localWorkspaceStorage(), firebaseWorkspaceStorage())

    private fun gson(): Gson = GsonBuilder()
            .let { workspaceSerialization(it) }
            .create()

    private fun workspaceSerialization(gsonBuilder: GsonBuilder): GsonBuilder = gsonBuilder
            .registerTypeHierarchyAdapter(BlendMode::class.java, BlendModeAdapter())
            .registerTypeHierarchyAdapter(PorterDuffOperator::class.java, PorterDuffAdapter())
            .registerTypeHierarchyAdapter(WorkspaceModel::class.java, WorkspaceModelAdapter())
}