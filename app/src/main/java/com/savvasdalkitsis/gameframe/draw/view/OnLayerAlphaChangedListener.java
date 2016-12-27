package com.savvasdalkitsis.gameframe.draw.view;

interface OnLayerAlphaChangedListener {

    OnLayerAlphaChangedListener NO_OP = alpha -> {};

    void onLayeralphaChanged(float alpha);
}
