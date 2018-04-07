package com.savvasdalkitsis.gameframe.feature.workspace.injector

import android.view.Menu
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.savvasdalkitsis.gameframe.feature.authentication.injector.AuthenticationInjector
import com.savvasdalkitsis.gameframe.feature.bitmap.injector.BitmapInjector
import com.savvasdalkitsis.gameframe.feature.composition.CompositionInjector
import com.savvasdalkitsis.gameframe.feature.composition.model.BlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.PorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.device.injector.DeviceInjector
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector
import com.savvasdalkitsis.gameframe.feature.storage.injector.StorageInjector
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

    fun workspacePresenter() = WorkspacePresenter<Menu, View>(DeviceInjector.gameFrameUseCase(),
            CompositionInjector.blendUseCase(), workspaceUseCase(), stringUseCase(), MessageDisplayInjector.messageDisplay(), workspaceNavigator(),
            IpInjector.ipNavigator(), BitmapInjector.bitmapFileUseCase(), NetworkingInjector.wifiUseCase())

    fun workspaceUseCase() = WorkspaceUseCase(gson(), workspaceStorage(), localWorkspaceStorage())

    private fun stringUseCase() = StringUseCase(application())

    fun workspaceStorage() = AuthenticationAwareWorkspaceStorage(AuthenticationInjector.authenticationUseCase(), localWorkspaceStorage(), firebaseWorkspaceStorage())

    fun localWorkspaceStorage() = LocalWorkspaceStorage(gson(), StorageInjector.localStorageUseCase())

    fun firebaseWorkspaceStorage() = FirebaseWorkspaceStorage(AuthenticationInjector.authenticationUseCase(), gson())

    private fun gson(): Gson = GsonBuilder()
            .let { workspaceSerialization(it) }
            .create()

    private fun workspaceSerialization(gsonBuilder: GsonBuilder): GsonBuilder = gsonBuilder
            .registerTypeHierarchyAdapter(BlendMode::class.java, BlendModeAdapter())
            .registerTypeHierarchyAdapter(PorterDuffOperator::class.java, PorterDuffAdapter())
            .registerTypeHierarchyAdapter(WorkspaceModel::class.java, WorkspaceModelAdapter())
}