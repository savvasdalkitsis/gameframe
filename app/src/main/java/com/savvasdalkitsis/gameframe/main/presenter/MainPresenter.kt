package com.savvasdalkitsis.gameframe.main.presenter

import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.main.view.MainView
import com.savvasdalkitsis.gameframe.rx.RxTransformers

class MainPresenter(private val ipRepository: IpRepository) {
    private var mainView: MainView? = null

    fun bindView(mainView: MainView) {
        this.mainView = mainView
    }

    fun loadIpAddress() {
        ipRepository.ipAddress
                .compose<IpAddress>(RxTransformers.schedulers<IpAddress>())
                .subscribe(
                        { mainView?.ipAddressLoaded(it) },
                        { mainView?.ipCouldNotBeFound(it) }
                )
    }
}
