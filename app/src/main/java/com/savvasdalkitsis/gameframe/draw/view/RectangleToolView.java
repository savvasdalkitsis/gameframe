package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.RectangleTool;

public class RectangleToolView extends ToolView {

    private final RectangleTool rectangleTool = new RectangleTool();

    public RectangleToolView(Context context) {
        super(context);
    }

    public RectangleToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(R.drawable.ic_crop_din_black_48px);
    }

    @Override
    protected DrawingTool getDrawingTool() {
        return rectangleTool;
    }
}
