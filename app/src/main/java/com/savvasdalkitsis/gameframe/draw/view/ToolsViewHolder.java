package com.savvasdalkitsis.gameframe.draw.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.Tools;

class ToolsViewHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final ToolView toolView;

    ToolsViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_tool_entry, parent, false));
        title = (TextView) itemView.findViewById(R.id.view_tool_entry_title);
        toolView = (ToolView) itemView.findViewById(R.id.view_tool_entry_icon);
    }

    public void bind(Tools tool) {
        title.setText(tool.getName());
        toolView.bind(tool);
    }

    void clearListeners() {
        setOnClickListener(null);
    }

    void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }
}
