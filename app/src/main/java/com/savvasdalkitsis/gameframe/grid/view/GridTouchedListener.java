package com.savvasdalkitsis.gameframe.grid.view;

public interface GridTouchedListener {

    GridTouchedListener NO_OP = (column, row) -> {};

    void onGridTouchedListener(int column, int row);
}
