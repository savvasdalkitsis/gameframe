package com.savvasdalkitsis.gameframe.draw.view;

interface SwatchSelectedListener {

    void onSwatchSelected(SwatchView swatchView);

    void onSwatchLongPressed(SwatchView swatch);

    SwatchSelectedListener NO_OP = new SwatchSelectedListener() {
        @Override
        public void onSwatchSelected(SwatchView swatchView) {
        }

        @Override
        public void onSwatchLongPressed(SwatchView swatch) {
        }
    };
}
