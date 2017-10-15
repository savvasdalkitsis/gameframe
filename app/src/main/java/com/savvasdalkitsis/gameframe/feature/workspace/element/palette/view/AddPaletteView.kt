/**
 * Copyright 2017 Savvas Dalkitsis
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
package com.savvasdalkitsis.gameframe.feature.workspace.element.palette.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palettes

class AddPaletteView : RecyclerView {

    private val palettes = SimplePalettesAdapter(Palettes.preLoaded())

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setHasFixedSize(true)
        adapter = palettes
    }

    fun setOnAddNewPaletteSelectedListener(onAddNewPaletteSelectedListener: AddNewPaletteSelectedListener) =
        palettes.setOnAddNewPaletteSelectedListener(onAddNewPaletteSelectedListener)

    companion object {

        fun show(context: Context, root: ViewGroup, addNewPaletteSelectedListener: AddNewPaletteSelectedListener) {
            val dialog = AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                    .setTitle(R.string.add_new_palette)
                    .setNegativeButton(android.R.string.cancel) { _, _ -> }
                    .create()
            val addPaletteView = LayoutInflater.from(context).inflate(R.layout.view_add_palette_view, root, false) as AddPaletteView
            addPaletteView.setOnAddNewPaletteSelectedListener(object : AddNewPaletteSelectedListener {
                override fun onAddNewPalletSelected(palette: Palette) {
                    dialog.dismiss()
                    addNewPaletteSelectedListener.onAddNewPalletSelected(palette)
                }
            })
            dialog.setView(addPaletteView)
            dialog.show()
        }
    }
}
