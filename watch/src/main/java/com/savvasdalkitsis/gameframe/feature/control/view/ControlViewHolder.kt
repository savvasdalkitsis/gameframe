/**
 * Copyright 2018 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
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

