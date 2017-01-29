package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class ToolsView extends RecyclerView {

    private ToolsAdapter tools;

    public ToolsView(Context context) {
        super(context);
    }

    public ToolsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolsView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tools = new ToolsAdapter();
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setHasFixedSize(true);
        setAdapter(tools);
    }

    protected void setOnToolSelectedListener(ToolSelectedListener toolSelectedListener) {
        tools.setOnToolSelectedListener(toolSelectedListener);
    }
}