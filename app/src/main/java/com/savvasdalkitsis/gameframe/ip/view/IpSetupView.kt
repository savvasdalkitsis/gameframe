package com.savvasdalkitsis.gameframe.ip.view

import com.savvasdalkitsis.gameframe.ip.model.IpAddress

interface IpSetupView {

    fun displayIpAddress(ipAddress: IpAddress)

    fun errorDiscoveringIpAddress(throwable: Throwable)

    fun addressSaved(ipAddress: IpAddress)

    fun displayDiscovering()

    fun ipAddressDiscovered(ipAddress: IpAddress)

    fun tryingAddress(ipAddress: IpAddress)

    fun displayIdleView()
}
