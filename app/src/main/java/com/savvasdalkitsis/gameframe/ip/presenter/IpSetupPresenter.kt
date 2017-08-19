package com.savvasdalkitsis.gameframe.ip.presenter

import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.ip.view.IpSetupView
import com.savvasdalkitsis.gameframe.rx.RxTransformers
import com.savvasdalkitsis.gameframe.gameframe.usecase.GameFrameUseCase

import rx.subscriptions.CompositeSubscription

class IpSetupPresenter(private val gameFrameUseCase: GameFrameUseCase,
                       private val ipRepository: IpRepository,
                       private val ipDiscoveryUseCase: IpDiscoveryUseCase) {

    private val subscriptions = CompositeSubscription()
    private var ipSetupView: IpSetupView? = null

    fun bindView(ipSetupView: IpSetupView) {
        this.ipSetupView = ipSetupView
        loadStoredIp()
    }

    fun unbind() {
        subscriptions.clear()
    }

    fun setup(ipAddress: IpAddress) {
        ipRepository.saveIpAddress(ipAddress)
        ipSetupView?.addressSaved(ipAddress)
    }

    fun discoverIp() {
        ipSetupView?.displayDiscovering()
        subscriptions.add(ipDiscoveryUseCase.monitoredIps()
                .compose(RxTransformers.schedulers())
                .subscribe( { ipSetupView?.tryingAddress(it) }, { }))
        subscriptions.add(gameFrameUseCase.discoverGameFrameIp()
                .compose(RxTransformers.schedulers())
                .doOnCompleted { ipSetupView?.displayIdleView() }
                .subscribe(
                        { ipSetupView?.ipAddressDiscovered(it) },
                        { throwable ->
                            ipSetupView?.errorDiscoveringIpAddress(throwable)
                            loadStoredIp()
                        }
                ))
    }

    fun cancelDiscover() {
        subscriptions.clear()
        loadStoredIp()
    }

    private fun loadStoredIp() {
        ipSetupView?.displayIdleView()
        ipRepository.ipAddress
                .subscribe(
                        { ipSetupView?.displayIpAddress(it) },
                        { ipSetupView?.displayIpAddress(IpAddress()) }
                )
    }
}
