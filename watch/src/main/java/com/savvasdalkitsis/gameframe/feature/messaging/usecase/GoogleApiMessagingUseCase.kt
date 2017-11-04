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