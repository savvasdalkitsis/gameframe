/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.feature.ip.repository

import com.savvasdalkitsis.gameframe.feature.networking.model.IpAddress
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
