package com.savvasdalkitsis.gameframe.injector.presenter

import com.savvasdalkitsis.gameframe.control.presenter.ControlPresenter
import com.savvasdalkitsis.gameframe.draw.presenter.DrawPresenter
import com.savvasdalkitsis.gameframe.ip.presenter.IpSetupPresenter
import com.savvasdalkitsis.gameframe.home.presenter.HomePresenter
import com.savvasdalkitsis.gameframe.widget.presenter.WidgetPresenter

import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector.navigator
import com.savvasdalkitsis.gameframe.injector.ip.repository.IpRepositoryInjector.ipRepository
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.gameFrameUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.ipDiscoveryUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.savedDrawingUseCase

object PresenterInjector {

    fun ipSetupPresenter() =
            IpSetupPresenter(gameFrameUseCase(), ipRepository(), ipDiscoveryUseCase())

    fun controlPresenter() = ControlPresenter(gameFrameUseCase(), ipRepository())

    fun mainPresenter() = HomePresenter(ipRepository())

    fun widgetPresenter() =
            WidgetPresenter(gameFrameUseCase(), ipRepository(), navigator())

    fun drawPresenter() = DrawPresenter(gameFrameUseCase(), savedDrawingUseCase())
}
