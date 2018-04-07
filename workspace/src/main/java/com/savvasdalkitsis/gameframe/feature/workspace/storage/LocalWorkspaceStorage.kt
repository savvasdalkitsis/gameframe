package com.savvasdalkitsis.gameframe.feature.workspace.storage

import com.google.gson.Gson
import com.savvasdalkitsis.gameframe.feature.storage.usecase.LocalStorageUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.model.SaveContainer
import io.reactivex.Completable
import io.reactivex.Single
import java.io.InputStream

private const val SAVED_PROJECTS_DIR = "saved_projects"
private const val SAVED_EXTENSION = ".gameframe"

class LocalWorkspaceStorage(private val gson: Gson,
                            private val localStorageUseCase: LocalStorageUseCase) : WorkspaceStorage {

    override fun saveWorkspace(name: String, workspaceModel: SaveContainer): Completable =
            localStorageUseCase.saveFile(SAVED_PROJECTS_DIR, withExtension(name), { serialize(workspaceModel) }, true)
                    .toCompletable()

    override fun listProjectNames(): Single<List<String>> =
            localStorageUseCase.listFilesIn(SAVED_PROJECTS_DIR)
                    .flattenAsFlowable { it }
                    .map { it.name }
                    .filter { it.endsWith(SAVED_EXTENSION) }
                    .map { it.removeSuffix(SAVED_EXTENSION) }
                    .toList()

    override fun readWorkspace(name: String) =
            localStorageUseCase.readFile(SAVED_PROJECTS_DIR, withExtension(name))

    override fun deleteWorkspace(name: String) =
            localStorageUseCase.deleteFile(SAVED_PROJECTS_DIR, withExtension(name))

    private fun serialize(model: SaveContainer): InputStream = gson.toJson(model).byteInputStream()

    private fun withExtension(name: String) = "$name$SAVED_EXTENSION"
}