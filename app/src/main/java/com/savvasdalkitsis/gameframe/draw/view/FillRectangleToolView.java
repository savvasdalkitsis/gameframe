package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.FillRectangleTool;

public class FillRectangleToolView extends ToolView {

    private final FillRectangleTool fillRectangleTool = new FillRectangleTool();

    public FillRectangleToolView(Context context) {
        super(context);
    }

    public FillRectangleToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillRectangleToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(R.drawable.ic_rect_black_48px);
    }

    @Override
    protected DrawingTool getDrawingTool() {
        return fillRectangleTool;
    }
}
