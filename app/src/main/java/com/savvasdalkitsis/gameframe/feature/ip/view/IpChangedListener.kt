package com.savvasdalkitsis.gameframe.feature.ip.view

import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress

interface IpChangedListener {

    fun onIpChangedListener(ipAddress: IpAddress)
}