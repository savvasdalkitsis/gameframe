package com.savvasdalkitsis.gameframe.control.presenter

import com.savvasdalkitsis.gameframe.control.view.ControlView
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.model.*
import com.savvasdalkitsis.gameframe.rx.RxTransformers
import rx.Observable

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

    private fun runCommand(command: Observable<Void>) =
            command.compose(RxTransformers.interceptIpMissingException<Void>())
                    .compose(RxTransformers.schedulers<Void>())

    private fun runCommandAndNotifyView(command: Observable<Void>) {
        runCommand(command).subscribe({ controlView.operationSuccess() }, { e -> controlView.operationFailure(e) })
    }

    private fun runCommandAndIgnoreResult(command: Observable<Void>) {
        runCommand(command).subscribe({ }, { })
    }
}
