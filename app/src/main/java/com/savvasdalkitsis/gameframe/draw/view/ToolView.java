package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;

public abstract class ToolView extends ImageView {

    private ToolSelectedListener toolSelectedListener = ToolSelectedListener.NO_OP;

    public ToolView(Context context) {
        super(context);
    }

    public ToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(v -> {
            toolSelectedListener.onToolSelected(getDrawingTool());
            setAlpha(1f);
        });
    }

    public void setToolSelectedListener(ToolSelectedListener toolSelectedListener) {
        this.toolSelectedListener = toolSelectedListener;
    }

    protected abstract DrawingTool getDrawingTool();
}
