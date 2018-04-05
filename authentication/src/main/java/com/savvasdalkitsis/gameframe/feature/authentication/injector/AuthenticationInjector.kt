package com.savvasdalkitsis.gameframe.feature.authentication.injector

import com.savvasdalkitsis.gameframe.feature.authentication.usecase.FirebaseAuthenticationUseCase
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector

object AuthenticationInjector {
    fun authenticationUseCase() = FirebaseAuthenticationUseCase(TopActivityProviderInjector.topActivityProvider())
}