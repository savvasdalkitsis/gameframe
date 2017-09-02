package com.savvasdalkitsis.gameframe.feature.workspace.serialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.savvasdalkitsis.gameframe.feature.history.model.Moment
import com.savvasdalkitsis.gameframe.feature.history.model.MomentList
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import java.lang.reflect.Type

class WorkspaceModelAdapter : JsonDeserializer<WorkspaceModel> {

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext) =
            WorkspaceModel(
                    layers = momentList("layers", json, context),
                    palettes = momentList("palettes", json, context)
            )

    private inline fun <reified T : Moment<T>> momentList(name: String, json: JsonElement, context: JsonDeserializationContext) =
            MomentList(json.asJsonObject.getAsJsonArray(name).toList().map { context.deserialize<T>(it, T::class.java) } as Collection<T>)

}