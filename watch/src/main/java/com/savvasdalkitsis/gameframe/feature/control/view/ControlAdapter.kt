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
import android.view.LayoutInflater
import android.view.ViewGroup
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.control.model.ControlModel

class ControlAdapter(private val clickClickListener: ControlClickListener): RecyclerView.Adapter<ControlViewHolder>() {

    private val items = listOf(
            ControlViewModel(R.drawable.ic_block_power, R.string.power, ControlModel.POWER),
            ControlViewModel(R.drawable.ic_view_headline_white_48px, R.string.menu, ControlModel.MENU),
            ControlViewModel(R.drawable.ic_skip_next_white_48px, R.string.next, ControlModel.NEXT)
    )

    override fun onBindViewHolder(holder: ControlViewHolder, position: Int) {
        holder.bind(items[position], clickClickListener)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ControlViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_control, parent, false))

}