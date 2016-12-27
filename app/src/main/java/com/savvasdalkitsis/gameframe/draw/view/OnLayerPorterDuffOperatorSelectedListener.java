package com.savvasdalkitsis.gameframe.draw.view;

import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator;

interface OnLayerPorterDuffOperatorSelectedListener {

    OnLayerPorterDuffOperatorSelectedListener NO_OP = porterDuffOperator -> {};

    void onLayerPorterDuffOperatorSelected(PorterDuffOperator porterDuffOperator);
}
