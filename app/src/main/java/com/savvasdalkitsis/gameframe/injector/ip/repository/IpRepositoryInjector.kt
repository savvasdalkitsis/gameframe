package com.savvasdalkitsis.gameframe.injector.ip.repository

import com.savvasdalkitsis.gameframe.injector.rx.RxSharedPreferencesInjector.rxSharedPreferences
import com.savvasdalkitsis.gameframe.ip.repository.IpRepository
import com.savvasdalkitsis.gameframe.ip.repository.PreferenceIpRepository

object IpRepositoryInjector {

    fun ipRepository(): IpRepository = PreferenceIpRepository(rxSharedPreferences())

}
