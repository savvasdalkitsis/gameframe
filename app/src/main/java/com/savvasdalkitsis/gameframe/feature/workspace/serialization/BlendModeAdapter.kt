package com.savvasdalkitsis.gameframe.feature.workspace.serialization

import com.google.gson.*
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailableBlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.BlendMode
import java.lang.reflect.Type

class BlendModeAdapter: JsonSerializer<BlendMode>, JsonDeserializer<BlendMode> {

    override fun serialize(src: BlendMode, typeOfSrc: Type?, context: JsonSerializationContext?) =
            JsonPrimitive(AvailableBlendMode.from(src).name)

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?) =
            AvailableBlendMode.fromName(json.asString)
}