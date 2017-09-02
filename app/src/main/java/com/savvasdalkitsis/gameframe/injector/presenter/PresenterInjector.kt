package com.savvasdalkitsis.gameframe.injector.presenter

import android.view.Menu
import com.savvasdalkitsis.gameframe.feature.control.presenter.ControlPresenter
import com.savvasdalkitsis.gameframe.feature.home.presenter.HomePresenter
import com.savvasdalkitsis.gameframe.feature.ip.presenter.IpSetupPresenter
import com.savvasdalkitsis.gameframe.feature.widget.presenter.WidgetPresenter
import com.savvasdalkitsis.gameframe.feature.workspace.presenter.WorkspacePresenter
import com.savvasdalkitsis.gameframe.injector.feature.ip.repository.IpRepositoryInjector.ipRepository
import com.savvasdalkitsis.gameframe.injector.feature.navigation.NavigatorInjector.navigator
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.blendUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.bmpUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.gameFrameUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.ipDiscoveryUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.fileUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.stringUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.workspaceUseCase

object PresenterInjector {

    fun ipSetupPresenter() =
            IpSetupPresenter(gameFrameUseCase(), ipRepository(), ipDiscoveryUseCase())

    fun controlPresenter() = ControlPresenter(gameFrameUseCase(), ipRepository(), navigator())

    fun mainPresenter() = HomePresenter(ipRepository())

    fun widgetPresenter() =
            WidgetPresenter(gameFrameUseCase(), ipRepository(), navigator())

    fun workspacePresenter() = WorkspacePresenter<Menu>(gameFrameUseCase(), fileUseCase(),
            bmpUseCase(), blendUseCase(), workspaceUseCase(), stringUseCase())
}
