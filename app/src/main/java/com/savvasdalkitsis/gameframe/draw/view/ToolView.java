package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.Tools;

public class ToolView extends ImageView {

    private DrawingTool drawingTool;

    public ToolView(Context context) {
        super(context);
    }

    public ToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected DrawingTool getDrawingTool() {
        return drawingTool;
    }

    public void bind(Tools tool) {
        this.drawingTool = tool.getTool();
        setImageResource(tool.getIcon());
    }
}
