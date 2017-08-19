package com.savvasdalkitsis.gameframe.injector.gameframe.api

import com.savvasdalkitsis.gameframe.gameframe.api.GameFrameApi

import com.savvasdalkitsis.gameframe.injector.infra.network.RetrofitInjector.retrofit

object GameFrameApiInjector {

    fun gameFrameApi(): GameFrameApi = retrofit().create(GameFrameApi::class.java)

}
