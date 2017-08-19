package com.savvasdalkitsis.gameframe.main.view

import com.savvasdalkitsis.gameframe.ip.model.IpAddress

interface MainView {

    fun ipAddressLoaded(ipAddress: IpAddress)
    fun ipCouldNotBeFound(throwable: Throwable)
}
