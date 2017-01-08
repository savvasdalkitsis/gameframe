package com.savvasdalkitsis.gameframe.draw.view;

interface OnLayerSettingsClickedListener {

    OnLayerSettingsClickedListener NO_OP = () -> {};

    void onLayerSettingsClicked();
}
