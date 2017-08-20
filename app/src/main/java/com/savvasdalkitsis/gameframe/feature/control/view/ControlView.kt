package com.savvasdalkitsis.gameframe.feature.control.view

import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress

interface ControlView {

    fun operationSuccess()

    fun operationFailure(e: Throwable)

    fun ipAddressLoaded(ipAddress: IpAddress)

    fun ipCouldNotBeFound(throwable: Throwable)
}
