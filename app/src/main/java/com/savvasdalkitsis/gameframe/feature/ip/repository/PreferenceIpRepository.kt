package com.savvasdalkitsis.gameframe.feature.ip.repository

import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.feature.ip.model.IpBaseHostMissingException
import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences
import io.reactivex.Single

class PreferenceIpRepository(private val preferences: RxSharedPreferences) : IpRepository {

    override val ipAddress: Single<IpAddress>
        get() = preferences.getString(PREF_IP_BASE)
                .map { IpAddress.parse(it) }
                .toSingle()
                .flatMap<IpAddress> {
                    if (it.isValid()) {
                        Single.just(it)
                    } else {
                        Single.error(IllegalStateException("saved ip address was not valid: $it"))
                    }
                }
                .onErrorResumeNext { Single.error(IpBaseHostMissingException("Error trying to read ip address from repository", it)) }

    override fun saveIpAddress(ipAddress: IpAddress) {
        preferences.setString(PREF_IP_BASE, ipAddress.toString())
    }

    companion object {
        private val PREF_IP_BASE = "pref_ip_base"
    }

}
