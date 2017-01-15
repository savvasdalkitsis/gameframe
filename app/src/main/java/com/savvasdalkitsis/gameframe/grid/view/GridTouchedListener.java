package com.savvasdalkitsis.gameframe.grid.view;

public interface GridTouchedListener {

    GridTouchedListener NO_OP = new GridTouchedListener() {
        @Override
        public void onGridTouchStarted() {}

        @Override
        public void onGridTouch(int startColumn, int startRow, int column, int row) {}

        @Override
        public void onGridTouchFinished() {}
    };

    void onGridTouchStarted();

    void onGridTouch(int startColumn, int startRow, int column, int row);

    void onGridTouchFinished();
}
