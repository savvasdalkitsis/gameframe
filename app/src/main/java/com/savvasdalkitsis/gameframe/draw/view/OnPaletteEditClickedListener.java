package com.savvasdalkitsis.gameframe.draw.view;

interface OnPaletteEditClickedListener {

    OnPaletteEditClickedListener NO_OP = () -> {};

    void onPaletteEditClicked();
}
