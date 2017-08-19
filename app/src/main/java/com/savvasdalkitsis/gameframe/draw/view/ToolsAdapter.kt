package com.savvasdalkitsis.gameframe.draw.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

import com.savvasdalkitsis.gameframe.draw.model.Tools

class ToolsAdapter : RecyclerView.Adapter<ToolsViewHolder>() {

    var toolSelectedListener: ToolSelectedListener? = null
        set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ToolsViewHolder(parent)

    override fun onBindViewHolder(holder: ToolsViewHolder, position: Int) = with(holder) {
        clearListeners()
        bind(Tools.values()[position])
        setOnClickListener { toolSelectedListener?.onToolSelected(Tools.values()[holder.adapterPosition]) }
    }

    override fun getItemCount() = Tools.values().size

}
