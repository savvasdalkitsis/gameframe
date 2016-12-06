package com.savvasdalkitsis.gameframe.draw.view;

interface SwatchSelectedListener {

    void onSwatchSelected(int color);

    SwatchSelectedListener NO_OP = color -> {};
}
