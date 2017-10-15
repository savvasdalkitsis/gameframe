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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.PaletteSettings

class PaletteSettingsView : LinearLayout {

    private lateinit var title: EditText

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        title = findViewById(R.id.view_palette_title)
    }

    fun bindTo(palette: Palette) {
        title.setText(palette.title)
    }

    val paletteSettings: PaletteSettings
        get() = PaletteSettings(title = title.text.toString())

    companion object {

        internal fun show(context: Context, palette: Palette, root: ViewGroup, paletteSettingsSetListener: PaletteSettingsSetListener) {
            val settingsView = LayoutInflater.from(context).inflate(R.layout.view_palette_settings, root, false) as PaletteSettingsView
            settingsView.bindTo(palette)
            AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                    .setTitle(R.string.palette_settings)
                    .setView(settingsView)
                    .setPositiveButton(android.R.string.ok) { _, _ -> paletteSettingsSetListener.onPaletteSettingsSet(settingsView.paletteSettings) }
                    .setNegativeButton(android.R.string.cancel) { _, _ -> }
                    .create()
                    .show()
        }
    }
}
