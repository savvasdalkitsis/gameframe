package com.savvasdalkitsis.gameframe.feature.workspace.serialization

import com.google.gson.*
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailablePorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.composition.model.PorterDuffOperator
import java.lang.reflect.Type

class PorterDuffAdapter : JsonSerializer<PorterDuffOperator>, JsonDeserializer<PorterDuffOperator> {

    override fun serialize(src: PorterDuffOperator, typeOfSrc: Type?, context: JsonSerializationContext?) =
            JsonPrimitive(AvailablePorterDuffOperator.from(src).name)


    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?) =
            AvailablePorterDuffOperator.fromName(json.asString)

}