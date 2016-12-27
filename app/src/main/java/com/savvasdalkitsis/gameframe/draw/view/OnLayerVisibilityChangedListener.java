package com.savvasdalkitsis.gameframe.draw.view;

interface OnLayerVisibilityChangedListener {

    OnLayerVisibilityChangedListener NO_OP = visible -> {};

    void onLayerVisibilityChanged(boolean visible);
}
