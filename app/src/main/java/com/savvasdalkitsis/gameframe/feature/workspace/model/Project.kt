package com.savvasdalkitsis.gameframe.feature.workspace.model

import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase

data class Project(var name: String? = null,
                   val history: HistoryUseCase<WorkspaceModel>,
                   var displayLayoutBorders: Boolean = true)