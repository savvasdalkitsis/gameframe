package com.savvasdalkitsis.gameframe.feature.gameframe.usecase

import com.savvasdalkitsis.gameframe.feature.gameframe.api.CommandResponse

internal class GameFrameCommandError(msg: String, val response: CommandResponse) : Throwable(msg)
