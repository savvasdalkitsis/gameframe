package com.savvasdalkitsis.gameframe.draw.view;

interface SwatchSelectedListener {

    void onSwatchSelected(int color);

    void onSwatchLongPressed(SwatchView swatch);

    SwatchSelectedListener NO_OP = new SwatchSelectedListener() {
        @Override
        public void onSwatchSelected(int color) {
        }

        @Override
        public void onSwatchLongPressed(SwatchView swatch) {
        }
    };
}
