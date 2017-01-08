package com.savvasdalkitsis.gameframe.draw.view;

interface OnLayerDuplicatedListener {

    OnLayerDuplicatedListener NO_OP = () -> {};

    void onLayerDuplicated();
}
