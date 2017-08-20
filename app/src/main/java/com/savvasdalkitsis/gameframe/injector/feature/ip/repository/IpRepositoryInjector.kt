package com.savvasdalkitsis.gameframe.injector.feature.ip.repository

import com.savvasdalkitsis.gameframe.feature.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.feature.ip.repository.PreferenceIpRepository
import com.savvasdalkitsis.gameframe.injector.infra.rx.RxSharedPreferencesInjector.rxSharedPreferences

object IpRepositoryInjector {

    fun ipRepository(): IpRepository = PreferenceIpRepository(rxSharedPreferences())

}
