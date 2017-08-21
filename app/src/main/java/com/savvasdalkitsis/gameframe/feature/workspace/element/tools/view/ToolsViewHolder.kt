package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model.Tools
import com.savvasdalkitsis.gameframe.infra.kotlin.ViewAction

class ToolsViewHolder internal constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_tool_entry, parent, false)) {

    private val title: TextView = itemView.findViewById(R.id.view_tool_entry_title)
    private val toolView: ToolView = itemView.findViewById(R.id.view_tool_entry_icon)

    fun bind(tool: Tools) {
        title.text = tool.label
        toolView.bind(tool)
    }

    internal fun clearListeners() {
        setOnClickListener(null)
    }

    internal fun setOnClickListener(onClickListener: ViewAction?) {
        itemView.setOnClickListener(onClickListener)
    }
}
