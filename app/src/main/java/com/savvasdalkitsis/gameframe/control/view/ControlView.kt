package com.savvasdalkitsis.gameframe.control.view

import com.savvasdalkitsis.gameframe.ip.model.IpAddress

interface ControlView {

    fun operationSuccess()

    fun operationFailure(e: Throwable)

    fun ipAddressLoaded(ipAddress: IpAddress)

    fun ipCouldNotBeFound(throwable: Throwable)
}
