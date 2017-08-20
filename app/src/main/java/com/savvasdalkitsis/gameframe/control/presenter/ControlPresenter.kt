package com.savvasdalkitsis.gameframe.control.presenter

import com.savvasdalkitsis.gameframe.control.model.*
import com.savvasdalkitsis.gameframe.control.view.ControlView
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import io.reactivex.Completable

class ControlPresenter(private val gameFrameUseCase: GameFrameUseCase, private val ipRepository: IpRepository) {

    private lateinit var controlView: ControlView

    fun bindView(controlView: ControlView) {
        this.controlView = controlView
    }

    fun loadIpAddress() {
        ipRepository.ipAddress
                .compose<IpAddress>(RxTransformers.schedulers<IpAddress>())
                .subscribe({ controlView.ipAddressLoaded(it) }, { controlView.ipCouldNotBeFound(it) })
    }

    fun togglePower() {
        runCommandAndNotifyView(gameFrameUseCase.togglePower())
    }

    fun menu() {
        runCommandAndNotifyView(gameFrameUseCase.menu())
    }

    fun next() {
        runCommandAndNotifyView(gameFrameUseCase.next())
    }

    fun changeBrightness(brightness: Brightness) {
        runCommandAndIgnoreResult(gameFrameUseCase.setBrightness(brightness))
    }

    fun changePlaybackMode(playbackMode: PlaybackMode) {
        runCommandAndIgnoreResult(gameFrameUseCase.setPlaybackMode(playbackMode))
    }

    fun changeCycleInterval(cycleInterval: CycleInterval) {
        runCommandAndIgnoreResult(gameFrameUseCase.setCycleInterval(cycleInterval))
    }

    fun changeDisplayMode(displayMode: DisplayMode) {
        runCommandAndIgnoreResult(gameFrameUseCase.setDisplayMode(displayMode))
    }

    fun changeClockFace(clockFace: ClockFace) {
        runCommandAndIgnoreResult(gameFrameUseCase.setClockFace(clockFace))
    }

    private fun runCommand(command: Completable) =
            command.compose(RxTransformers.interceptIpMissingException())
                    .compose(RxTransformers.schedulers())

    private fun runCommandAndNotifyView(command: Completable) {
        runCommand(command).subscribe({ controlView.operationSuccess() }, { e -> controlView.operationFailure(e) })
    }

    private fun runCommandAndIgnoreResult(command: Completable) {
        runCommand(command).subscribe({ }, { })
    }
}
