package com.savvasdalkitsis.gameframe.ip.view

import com.savvasdalkitsis.gameframe.ip.model.IpAddress

interface IpChangedListener {

    fun onIpChangedListener(ipAddress: IpAddress)
}