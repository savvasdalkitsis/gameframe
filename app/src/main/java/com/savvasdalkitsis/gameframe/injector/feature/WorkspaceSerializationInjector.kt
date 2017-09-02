package com.savvasdalkitsis.gameframe.injector.feature

import com.google.gson.GsonBuilder
import com.savvasdalkitsis.gameframe.feature.composition.model.BlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.PorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.feature.workspace.serialization.BlendModeAdapter
import com.savvasdalkitsis.gameframe.feature.workspace.serialization.PorterDuffAdapter
import com.savvasdalkitsis.gameframe.feature.workspace.serialization.WorkspaceModelAdapter

object WorkspaceSerializationInjector {

    fun workspaceSerialization(gsonBuilder: GsonBuilder): GsonBuilder = gsonBuilder
            .registerTypeHierarchyAdapter(BlendMode::class.java, BlendModeAdapter())
            .registerTypeHierarchyAdapter(PorterDuffOperator::class.java, PorterDuffAdapter())
            .registerTypeHierarchyAdapter(WorkspaceModel::class.java, WorkspaceModelAdapter())

}