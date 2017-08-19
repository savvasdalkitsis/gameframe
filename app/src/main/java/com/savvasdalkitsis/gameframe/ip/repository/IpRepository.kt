package com.savvasdalkitsis.gameframe.ip.repository

import com.savvasdalkitsis.gameframe.ip.model.IpAddress

import rx.Observable

interface IpRepository {

    val ipAddress: Observable<IpAddress>

    fun saveIpAddress(ipAddress: IpAddress)
}
