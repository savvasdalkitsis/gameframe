package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.PencilTool;

public class PencilToolView extends ToolView {

    private final PencilTool pencilTool = new PencilTool();

    public PencilToolView(Context context) {
        super(context);
    }

    public PencilToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PencilToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(R.drawable.ic_create_black_48px);
    }

    @Override
    protected DrawingTool getDrawingTool() {
        return pencilTool;
    }
}
