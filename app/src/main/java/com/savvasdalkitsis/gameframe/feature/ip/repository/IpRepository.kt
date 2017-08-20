package com.savvasdalkitsis.gameframe.feature.ip.repository

import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import io.reactivex.Single

interface IpRepository {

    val ipAddress: Single<IpAddress>

    fun saveIpAddress(ipAddress: IpAddress)
}
