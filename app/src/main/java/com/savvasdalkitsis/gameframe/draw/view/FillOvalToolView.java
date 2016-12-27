package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.FillOvalTool;

public class FillOvalToolView extends ToolView {

    private final FillOvalTool fillOvalTool = new FillOvalTool();

    public FillOvalToolView(Context context) {
        super(context);
    }

    public FillOvalToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillOvalToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(R.drawable.ic_disk_black_48px);
    }

    @Override
    protected DrawingTool getDrawingTool() {
        return fillOvalTool;
    }
}
