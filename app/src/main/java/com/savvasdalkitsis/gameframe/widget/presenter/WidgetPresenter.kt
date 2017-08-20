package com.savvasdalkitsis.gameframe.widget.presenter

import android.util.Log
import com.savvasdalkitsis.gameframe.control.view.PowerTileService
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.navigation.Navigator
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import com.savvasdalkitsis.gameframe.widget.view.WidgetView
import io.reactivex.Completable

class WidgetPresenter(private val gameFrameUseCase: GameFrameUseCase,
                      private val ipRepository: IpRepository,
                      private val navigator: Navigator) {

    private var widgetView: WidgetView? = null

    fun bindView(widgetView: WidgetView) {
        this.widgetView = widgetView
    }

    fun menu() = perform(gameFrameUseCase.menu())

    fun next() = perform(gameFrameUseCase.next())

    fun power() = perform(gameFrameUseCase.togglePower())

    private fun perform(operation: Completable) {
        ipRepository.ipAddress
                .doOnError {
                    Log.e(PowerTileService::class.java.name, "IP address not found", it)
                    navigator.navigateToIpSetup()
                }
                .flatMapCompletable { operation }
                .compose(RxTransformers.schedulers())
                .subscribe({ }, {
                    Log.e(PowerTileService::class.java.name, "Error communicating with the GameFrame", it)
                    widgetView?.operationError()
                })
    }
}
