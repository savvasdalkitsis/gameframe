package com.savvasdalkitsis.gameframe.feature.workspace.usecase

import com.google.gson.Gson
import com.savvasdalkitsis.gameframe.feature.saves.usecase.FileUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import io.reactivex.Single
import java.io.*

class WorkspaceUseCase(private val gson: Gson,
                       private val fileUseCase: FileUseCase) {

    fun saveProject(name: String, workspaceModel: WorkspaceModel) =
            fileUseCase.saveFile(SAVED_PROJECTS_DIR, "$name$SAVED_EXTENSION", { serialize(workspaceModel) }, true)

    fun savedProjects(): Single<List<String>> = fileUseCase.listFilesIn(SAVED_PROJECTS_DIR)
            .flattenAsFlowable { it }
            .map { it.name }
            .filter { it.endsWith(SAVED_EXTENSION) }
            .map { it.removeSuffix(SAVED_EXTENSION) }
            .toList()

    fun load(name: String): Single<WorkspaceModel> =
            fileUseCase.readFile(SAVED_PROJECTS_DIR, "$name$SAVED_EXTENSION")
                    .map { deserialize(it) }

    private fun deserialize(reader: Reader) = gson.fromJson(reader, WorkspaceModel::class.java)

    private fun serialize(model: WorkspaceModel): InputStream = gson.toJson(model).byteInputStream()

    companion object {
        private val SAVED_PROJECTS_DIR = "saved_projects"
        private val SAVED_EXTENSION = ".gameframe"
    }
}