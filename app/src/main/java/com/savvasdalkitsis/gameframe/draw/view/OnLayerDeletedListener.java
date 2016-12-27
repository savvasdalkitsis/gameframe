package com.savvasdalkitsis.gameframe.draw.view;

interface OnLayerDeletedListener {

    OnLayerDeletedListener NO_OP = () -> {};

    void onLayerDeleted();
}
