package com.savvasdalkitsis.gameframe.ip.repository

import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import io.reactivex.Single

interface IpRepository {

    val ipAddress: Single<IpAddress>

    fun saveIpAddress(ipAddress: IpAddress)
}
