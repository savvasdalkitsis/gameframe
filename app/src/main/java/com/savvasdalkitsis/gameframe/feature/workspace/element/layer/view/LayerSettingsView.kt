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
package com.savvasdalkitsis.gameframe.feature.workspace.element.layer.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailableBlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailablePorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.LayerSettings
import com.savvasdalkitsis.gameframe.kotlin.visible

class LayerSettingsView : LinearLayout {

    private lateinit var title: EditText
    private lateinit var blendMode: Spinner
    private lateinit var porterDuff: Spinner
    private lateinit var alpha: SeekBar

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        title = findViewById(R.id.view_layer_title)
        alpha = findViewById(R.id.view_layer_alpha)
        blendMode = findViewById(R.id.view_layer_blend_mode)
        blendMode.adapter = adapter(AvailableBlendMode.values())
        porterDuff = findViewById(R.id.view_layer_porter_duff)
        porterDuff.adapter = adapter(AvailablePorterDuffOperator.values())
    }

    fun bindTo(layer: Layer) {
        title.setText(layer.layerSettings.title)
        alpha.progress = (layer.layerSettings.alpha * 100).toInt()
        blendMode.visible()
        blendMode.setSelection(AvailableBlendMode.indexOf(layer.layerSettings.blendMode))
        porterDuff.setSelection(AvailablePorterDuffOperator.indexOf(layer.layerSettings.porterDuffOperator))
        porterDuff.visible()
    }

    private fun <T> adapter(values: Array<T>) = ArrayAdapter(context, android.R.layout.simple_spinner_item, values).apply {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    val layerSettings: LayerSettings
        get() = LayerSettings(
                title = title.text.toString(),
                alpha = alpha.progress / 100f,
                blendMode = AvailableBlendMode.values()[blendMode.selectedItemPosition],
                porterDuffOperator = AvailablePorterDuffOperator.values()[porterDuff.selectedItemPosition]
        )

    companion object {

        internal fun show(context: Context, layer: Layer, root: ViewGroup, layerSettingsSetListener: LayerSettingsSetListener) {
            val settingsView = LayoutInflater.from(context).inflate(R.layout.view_layer_settings, root, false) as LayerSettingsView
            settingsView.bindTo(layer)
            AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                    .setTitle(R.string.layer_settings)
                    .setView(settingsView)
                    .setPositiveButton(android.R.string.ok) { _, _ -> layerSettingsSetListener.onLayerSettingsSet(settingsView.layerSettings) }
                    .setNegativeButton(android.R.string.cancel) { _, _ -> }
                    .create()
                    .show()
        }
    }
}
