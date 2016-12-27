package com.savvasdalkitsis.gameframe.draw.view;

import com.savvasdalkitsis.gameframe.composition.model.BlendMode;

interface OnLayerBlendModeSelectedListener {

    OnLayerBlendModeSelectedListener NO_OP = blendMode -> {};

    void onLayerBlendModeSelected(BlendMode blendMode);
}
