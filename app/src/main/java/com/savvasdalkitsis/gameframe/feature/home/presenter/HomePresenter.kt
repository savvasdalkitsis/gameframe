package com.savvasdalkitsis.gameframe.feature.home.presenter

import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.home.view.HomeView
import com.savvasdalkitsis.gameframe.infra.rx.RxTransformers

class HomePresenter(private val ipRepository: IpRepository) {
    private var homeView: HomeView? = null

    fun bindView(homeView: HomeView) {
        this.homeView = homeView
    }

    fun loadIpAddress() {
        ipRepository.ipAddress
                .compose<IpAddress>(RxTransformers.schedulers<IpAddress>())
                .subscribe(
                        { homeView?.ipAddressLoaded(it) },
                        { homeView?.ipCouldNotBeFound(it) }
                )
    }
}
