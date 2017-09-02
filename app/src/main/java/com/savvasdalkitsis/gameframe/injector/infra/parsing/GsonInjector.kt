package com.savvasdalkitsis.gameframe.injector.infra.parsing

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.savvasdalkitsis.gameframe.injector.feature.WorkspaceSerializationInjector.workspaceSerialization

object GsonInjector {

    fun gson(): Gson = GsonBuilder()
            .apply { workspaceSerialization(this) }
            .create()
}

