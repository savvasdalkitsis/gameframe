package com.savvasdalkitsis.gameframe.draw.view;

interface OnItemDeletedListener {

    OnItemDeletedListener NO_OP = () -> {};

    void onItemDeleted();
}
