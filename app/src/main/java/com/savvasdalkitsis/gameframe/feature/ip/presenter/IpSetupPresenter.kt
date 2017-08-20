package com.savvasdalkitsis.gameframe.feature.ip.presenter

import com.savvasdalkitsis.gameframe.feature.gameframe.usecase.GameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.ip.usecase.IpDiscoveryUseCase
import com.savvasdalkitsis.gameframe.feature.ip.view.IpSetupView
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers
import io.reactivex.disposables.CompositeDisposable

class IpSetupPresenter(private val gameFrameUseCase: GameFrameUseCase,
                       private val ipRepository: IpRepository,
                       private val ipDiscoveryUseCase: IpDiscoveryUseCase) {

    private val disposables = CompositeDisposable()
    private var ipSetupView: IpSetupView? = null

    fun bindView(ipSetupView: IpSetupView) {
        this.ipSetupView = ipSetupView
        loadStoredIp()
    }

    fun unbind() {
        disposables.clear()
    }

    fun setup(ipAddress: IpAddress) {
        ipRepository.saveIpAddress(ipAddress)
        ipSetupView?.addressSaved(ipAddress)
    }

    fun discoverIp() {
        ipSetupView?.displayDiscovering()
        disposables.add(ipDiscoveryUseCase.monitoredIps()
                .compose(RxTransformers.schedulersFlowable())
                .subscribe( { ipSetupView?.tryingAddress(it) }, { }))
        disposables.add(gameFrameUseCase.discoverGameFrameIp()
                .compose(RxTransformers.schedulers<IpAddress>())
                .doOnSuccess { ipSetupView?.displayIdleView() }
                .subscribe(
                        { ipSetupView?.ipAddressDiscovered(it) },
                        { throwable ->
                            ipSetupView?.errorDiscoveringIpAddress(throwable)
                            loadStoredIp()
                        }
                ))
    }

    fun cancelDiscover() {
        disposables.clear()
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
