package com.savvasdalkitsis.gameframe.feature.control.presenter

import com.savvasdalkitsis.gameframe.feature.messaging.usecase.GoogleApiMessagingUseCase
import com.savvasdalkitsis.gameframe.feature.messaging.usecase.MessagingUseCase

class ControlPresenter {

    private lateinit var messagingUseCase: MessagingUseCase

    fun power() {
        messagingUseCase.sendMessage("power")
    }

    fun menu() {
        messagingUseCase.sendMessage("menu")
    }

    fun next() {
        messagingUseCase.sendMessage("next")
    }

    fun bind(messagingUseCase: GoogleApiMessagingUseCase) {
        this.messagingUseCase = messagingUseCase
    }

}

