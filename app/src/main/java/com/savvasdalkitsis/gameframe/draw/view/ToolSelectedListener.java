package com.savvasdalkitsis.gameframe.draw.view;

import com.savvasdalkitsis.gameframe.draw.model.Tools;

interface ToolSelectedListener {

    ToolSelectedListener NO_OP = tool -> {};

    void onToolSelected(Tools tool);
}
