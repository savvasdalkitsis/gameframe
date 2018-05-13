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
package com.savvasdalkitsis.gameframe.feature.messaging.usecase

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Wearable

class GoogleApiMessagingUseCase(private val client: GoogleApiClient) : MessagingUseCase {

    override fun sendMessage(message: String) {
        Wearable.NodeApi.getConnectedNodes(client).setResultCallback { result ->
            result.nodes?.forEach { node ->
                Wearable.MessageApi.sendMessage(client, node.id, "/command", message.toByteArray())
            }
        }
    }
}