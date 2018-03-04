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
import com.savvasdalkitsis.gameframe.feature.workspace.model.*
import com.savvasdalkitsis.gameframe.feature.workspace.storage.WorkspaceStorage
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.io.Reader
import java.io.StringReader
import kotlin.reflect.KClass

class WorkspaceUseCase(private val gson: Gson,
                       private val workspaceStorage: WorkspaceStorage,
                       private val localWorkspaceStorage: WorkspaceStorage) {

    fun saveProject(name: String, workspaceModel: WorkspaceModel) =
            workspaceStorage.saveWorkspace(name, SaveContainer(workspaceModel))

    fun savedProjectNames(): Single<List<String>> = workspaceStorage.listProjectNames()

    fun savedProjects(): Single<List<WorkspaceItem>> = projectsFrom(savedProjectNames())

    fun locallySavedProjects(): Single<List<WorkspaceItem>> = projectsFrom(localWorkspaceStorage.listProjectNames(), localWorkspaceStorage)

    private fun projectsFrom(names: Single<List<String>>, workspaceStorage: WorkspaceStorage = this.workspaceStorage) = names
            .toFlowable()
            .flatMap { Flowable.fromIterable(it) }
            .flatMapSingle { name -> loadWith(name, workspaceStorage).map { WorkspaceItem(name ,it) } }
            .toList()

    fun load(name: String): Single<WorkspaceModel> = loadWith(name)

    private fun loadWith(name: String, workspaceStorage: WorkspaceStorage = this.workspaceStorage): Single<WorkspaceModel> =
            workspaceStorage.readWorkspace(name)
                    .map(::reusable)
                    .flatMap(::versionCheck)
                    .map(::deserializeWorkspace)

    fun deleteProject(name: String): Completable =
            workspaceStorage.deleteWorkspace(name)

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
}