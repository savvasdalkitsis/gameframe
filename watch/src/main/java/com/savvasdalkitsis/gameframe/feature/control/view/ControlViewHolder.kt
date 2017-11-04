package com.savvasdalkitsis.gameframe.feature.control.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.savvasdalkitsis.gameframe.R

class ControlViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var fab: ImageView = itemView.findViewById(R.id.view_control_fab)
    private var title: TextView = itemView.findViewById(R.id.view_control_title)

    fun bind(model: ControlViewModel, controlClickListener: ControlClickListener) {
        fab.setImageResource(model.layoutId)
        title.setText(model.title)
        itemView.setOnClickListener { controlClickListener.onControlClicked(model.controlModel) }
    }
}

