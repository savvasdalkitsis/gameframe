/**
 * Copyright 2018 Savvas Dalkitsis
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
package com.savvasdalkitsis.gameframe.feature.workspace.storage

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.gson.Gson
import com.savvasdalkitsis.gameframe.feature.authentication.usecase.AuthenticationUseCase
import com.savvasdalkitsis.gameframe.feature.workspace.model.SaveContainer
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.io.Reader
import java.io.StringReader

private val TAG = FirebaseWorkspaceStorage::javaClass.name
private const val KEY = "json"

class FirebaseWorkspaceStorage(private val authenticationUseCase: AuthenticationUseCase<*>,
                               private val gson: Gson) : WorkspaceStorage {

    private val firestore by lazy {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()
        }
    }

    override fun saveWorkspace(name: String, workspaceModel: SaveContainer) =
            simpleActionForDrawing(name) { set(mapOf(KEY to gson.toJson(workspaceModel))) }

    override fun listProjectNames(): Single<List<String>> =
            forDrawings { (collection, emitter) ->
                collection
                        .get()
                        .handleFailuresWith(emitter)
                        .addOnSuccessListener { query ->
                            Log.w(TAG, "Got drawing documents ${query.documents}")
                            try {
                                emitter.onSuccess(query.documents.map { it.id }.toList())
                            } catch (e: Throwable) {
                                Log.w(TAG, "Error trying to read drawings from successful response", e)
                                emitter.onError(e)
                            }
                        }
            }

    override fun readWorkspace(name: String): Single<Reader> =
            forDrawing(name) { (document, emitter) ->
                document.get()
                        .handleFailuresWith(emitter)
                        .addOnSuccessListener { result ->
                            if (result.exists()) {
                                try {
                                    emitter.onSuccess(StringReader(result.data[KEY].toString()))
                                } catch (e: Throwable) {
                                    emitter.onError(e)
                                }
                            } else {
                                Log.d(TAG, "Tried to load document but didn't exist")
                                emitter.onError(IllegalArgumentException("Could not find drawing with name $name in firestore"))
                            }
                        }
            }

    override fun deleteWorkspace(name: String) =
            simpleActionForDrawing(name) { delete() }

    private fun simpleActionForDrawing(name: String, action: DocumentReference.() -> Task<Void>): Completable =
            forDrawing<Unit>(name) { (document, emitter) ->
                action(document)
                        .handleFailuresWith(emitter)
                        .addOnSuccessListener {
                            Log.d(TAG, "Action success for drawing $name")
                            emitter.onSuccess(Unit)
                        }
            }.toCompletable()

    private fun <T> forDrawing(name: String, documentAction: (Pair<DocumentReference, SingleEmitter<T>>) -> Unit): Single<T> =
            forDrawings { (collection, emitter) ->
                Pair(collection.document(name), emitter).run(documentAction)
            }

    private fun <T> forDrawings(collectionAction: (Pair<CollectionReference, SingleEmitter<T>>) -> Unit): Single<T> =
            authenticationUseCase.userId()
                    .flatMap { userId ->
                        Single.create<T> { emitter ->
                            val collection = firestore.collection("users")
                                    .document(userId)
                                    .collection("drawings")
                            Pair(collection, emitter).run(collectionAction)
                        }
                    }

    private fun <TResult, T> Task<TResult>.handleFailuresWith(emitter: SingleEmitter<T>) =
            addOnFailureListener {
                Log.w(TAG, "Error for task", it)
                emitter.onError(it)
            }

}

