package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.ClearTool;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;

public class ClearToolView extends ToolView {

    private final ClearTool clearTool = new ClearTool();

    public ClearToolView(Context context) {
        super(context);
    }

    public ClearToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(R.drawable.ic_clear_all_black_48px);
    }

    @Override
    protected DrawingTool getDrawingTool() {
        return clearTool;
    }
}
