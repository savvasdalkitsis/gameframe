package com.savvasdalkitsis.gameframe.gameframe.usecase

import com.savvasdalkitsis.gameframe.gameframe.api.CommandResponse

internal class GameFrameCommandError(msg: String, val response: CommandResponse) : Throwable(msg)
