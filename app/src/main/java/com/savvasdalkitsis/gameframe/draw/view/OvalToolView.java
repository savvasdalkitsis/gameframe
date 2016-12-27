package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.OvalTool;

public class OvalToolView extends ToolView {

    private final OvalTool ovalTool = new OvalTool();

    public OvalToolView(Context context) {
        super(context);
    }

    public OvalToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OvalToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageResource(R.drawable.ic_panorama_fish_eye_black_48px);
    }

    @Override
    protected DrawingTool getDrawingTool() {
        return ovalTool;
    }
}
