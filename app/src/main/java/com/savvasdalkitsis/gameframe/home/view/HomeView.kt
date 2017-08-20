package com.savvasdalkitsis.gameframe.home.view

import com.savvasdalkitsis.gameframe.ip.model.IpAddress

interface HomeView {

    fun ipAddressLoaded(ipAddress: IpAddress)
    fun ipCouldNotBeFound(throwable: Throwable)
}
