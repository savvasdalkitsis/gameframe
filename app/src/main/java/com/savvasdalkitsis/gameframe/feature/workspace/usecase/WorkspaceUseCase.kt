package com.savvasdalkitsis.gameframe.feature.workspace.usecase

import com.google.gson.Gson
import com.savvasdalkitsis.gameframe.feature.saves.usecase.FileUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.model.SaveContainer
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import io.reactivex.Completable
import io.reactivex.Single
import java.io.*

class WorkspaceUseCase(private val gson: Gson,
                       private val fileUseCase: FileUseCase) {

    fun saveProject(name: String, workspaceModel: WorkspaceModel) =
            fileUseCase.saveFile(SAVED_PROJECTS_DIR, withExtension(name), { serialize(workspaceModel) }, true)

    fun savedProjects(): Single<List<String>> = fileUseCase.listFilesIn(SAVED_PROJECTS_DIR)
            .flattenAsFlowable { it }
            .map { it.name }
            .filter { it.endsWith(SAVED_EXTENSION) }
            .map { it.removeSuffix(SAVED_EXTENSION) }
            .toList()

    fun load(name: String): Single<WorkspaceModel> =
            fileUseCase.readFile(SAVED_PROJECTS_DIR, withExtension(name))
                    .map { deserialize(it) }

    fun deleteProject(name: String): Completable = fileUseCase.deleteFile(SAVED_PROJECTS_DIR, withExtension(name))

    private fun deserialize(reader: Reader) = gson.fromJson(reader, SaveContainer::class.java).workspaceModel

    private fun serialize(model: WorkspaceModel): InputStream = gson.toJson(SaveContainer(model)).byteInputStream()

    private fun withExtension(name: String) = "$name$SAVED_EXTENSION"

    companion object {
        private val SAVED_PROJECTS_DIR = "saved_projects"
        private val SAVED_EXTENSION = ".gameframe"
    }
}