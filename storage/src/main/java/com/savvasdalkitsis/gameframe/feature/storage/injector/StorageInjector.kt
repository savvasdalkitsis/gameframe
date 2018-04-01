package com.savvasdalkitsis.gameframe.feature.storage.injector

import com.savvasdalkitsis.gameframe.feature.storage.usecase.LocalStorageUseCase
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application

object StorageInjector {
    fun localStorageUseCase() = LocalStorageUseCase(application())
}