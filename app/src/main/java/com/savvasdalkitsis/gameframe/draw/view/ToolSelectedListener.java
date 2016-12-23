package com.savvasdalkitsis.gameframe.draw.view;

import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;

interface ToolSelectedListener {

    ToolSelectedListener NO_OP = drawingTool -> {};

    void onToolSelected(DrawingTool drawingTool);
}
