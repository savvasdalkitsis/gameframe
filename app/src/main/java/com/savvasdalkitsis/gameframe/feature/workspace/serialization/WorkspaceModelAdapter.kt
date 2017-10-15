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