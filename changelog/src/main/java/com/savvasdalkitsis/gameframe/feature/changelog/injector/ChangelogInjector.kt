package com.savvasdalkitsis.gameframe.feature.changelog.injector

import com.savvasdalkitsis.gameframe.feature.changelog.usecase.ChangeLogUseCase
import com.savvasdalkitsis.gameframe.infra.injector.RxSharedPreferencesInjector.rxSharedPreferences

object ChangelogInjector {

    fun changeLogUseCase() = ChangeLogUseCase(rxSharedPreferences())

}