package com.savvasdalkitsis.gameframe.injector.feature.gameframe.api

import com.savvasdalkitsis.gameframe.feature.gameframe.api.GameFrameApi

import com.savvasdalkitsis.gameframe.injector.infra.network.RetrofitInjector.retrofit

object GameFrameApiInjector {

    fun gameFrameApi(): GameFrameApi = retrofit().create(GameFrameApi::class.java)

}
