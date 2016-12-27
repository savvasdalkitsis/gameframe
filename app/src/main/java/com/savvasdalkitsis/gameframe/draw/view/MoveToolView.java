package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.MoveTool;

public class MoveToolView extends ToolView {

    private final MoveTool moveTool = new MoveTool();

    public MoveToolView(Context context) {
        super(context);
    }

    public MoveToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(R.drawable.ic_open_with_black_48px);
    }

    @Override
    protected DrawingTool getDrawingTool() {
        return moveTool;
    }
}
