package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.EraseTool;

public class EraseToolView extends ToolView {

    private final EraseTool eraseTool = new EraseTool();

    public EraseToolView(Context context) {
        super(context);
    }

    public EraseToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EraseToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(R.drawable.ic_eraser_variant);
    }

    @Override
    protected DrawingTool getDrawingTool() {
        return eraseTool;
    }
}
