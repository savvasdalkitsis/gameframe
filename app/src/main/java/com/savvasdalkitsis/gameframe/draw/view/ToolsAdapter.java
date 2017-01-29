package com.savvasdalkitsis.gameframe.draw.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.draw.model.Tools;

class ToolsAdapter extends RecyclerView.Adapter<ToolsViewHolder> {

    private ToolSelectedListener toolSelectedListener;

    @Override
    public ToolsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ToolsViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ToolsViewHolder holder, int position) {
        holder.clearListeners();
        holder.bind(Tools.values()[position]);
        holder.setOnClickListener(v -> toolSelectedListener.onToolSelected(Tools.values()[holder.getAdapterPosition()]));
    }

    @Override
    public int getItemCount() {
        return Tools.values().length;
    }

    void setOnToolSelectedListener(ToolSelectedListener toolSelectedListener) {
        this.toolSelectedListener = toolSelectedListener;
    }
}
