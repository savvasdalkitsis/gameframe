package com.savvasdalkitsis.gameframe.feature.home.view

import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress

interface HomeView {

    fun ipAddressLoaded(ipAddress: IpAddress)
    fun ipCouldNotBeFound(throwable: Throwable)
}
