package com.savvasdalkitsis.gameframe.gameframe.usecase;

class GameFrameCommandError extends Throwable {

    GameFrameCommandError(String msg) {
        super(msg);
    }
}
