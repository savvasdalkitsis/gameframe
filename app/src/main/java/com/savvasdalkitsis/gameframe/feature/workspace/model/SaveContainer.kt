package com.savvasdalkitsis.gameframe.feature.workspace.model

const val CURRENT_VERSION = 1

data class SaveContainer(val workspaceModel: WorkspaceModel) {
    val version = CURRENT_VERSION
}