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
package com.savvasdalkitsis.gameframe.feature.workspace.usecase

import com.google.gson.Gson
import com.savvasdalkitsis.gameframe.feature.saves.usecase.FileUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.model.SaveContainer
import com.savvasdalkitsis.gameframe.feature.workspace.model.VersionContainer
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import io.reactivex.Completable
import io.reactivex.Single
import java.io.*
import kotlin.reflect.KClass
import com.savvasdalkitsis.gameframe.feature.workspace.model.CURRENT_VERSION

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
                    .map(this::reusable)
                    .flatMap(this::versionCheck)
                    .map(this::deserializeWorkspace)

    fun deleteProject(name: String): Completable = fileUseCase.deleteFile(SAVED_PROJECTS_DIR, withExtension(name))

    private fun reusable(reader: Reader) = StringReader(reader.readText())

    private fun versionCheck(reader: StringReader): Single<StringReader> {
        val versionContainer = deserialize(reader, VersionContainer::class)
        val version = versionContainer.version
        if (version > CURRENT_VERSION) {
            throw UnsupportedProjectVersionException("Trying to load project with version $version. Max supported version is $CURRENT_VERSION")
        }
        reader.reset()
        return Single.just(reader)
    }

    private fun deserializeWorkspace(reader: StringReader) = deserialize(reader, SaveContainer::class).workspaceModel

    private fun <T: Any> deserialize(reader: Reader, type: KClass<T>): T = gson.fromJson(reader, type.java)

    private fun serialize(model: WorkspaceModel): InputStream = gson.toJson(SaveContainer(model)).byteInputStream()

    private fun withExtension(name: String) = "$name$SAVED_EXTENSION"

    companion object {
        private val SAVED_PROJECTS_DIR = "saved_projects"
        private val SAVED_EXTENSION = ".gameframe"
    }
}