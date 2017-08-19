package com.savvasdalkitsis.gameframe.ip.repository

import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences
import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.ip.model.IpBaseHostMissingException
import rx.Observable

class PreferenceIpRepository(private val preferences: RxSharedPreferences) : IpRepository {

    override val ipAddress: Observable<IpAddress>
        get() = preferences.getString(PREF_IP_BASE)
                .map<IpAddress> { IpAddress.parse(it) }
                .flatMap<IpAddress> {
                    if (it.isValid()) {
                        Observable.just(it)
                    } else {
                        Observable.error(IllegalStateException("saved ip address was not valid: $it"))
                    }
                }
                .onErrorResumeNext { Observable.error(IpBaseHostMissingException("Error trying to read ip address from repository", it)) }

    override fun saveIpAddress(ipAddress: IpAddress) {
        preferences.setString(PREF_IP_BASE, ipAddress.toString())
    }

    companion object {
        private val PREF_IP_BASE = "pref_ip_base"
    }

}
