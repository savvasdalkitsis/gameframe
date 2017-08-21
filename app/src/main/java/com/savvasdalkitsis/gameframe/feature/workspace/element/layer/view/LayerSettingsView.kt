package com.savvasdalkitsis.gameframe.feature.workspace.element.layer.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailableBlendMode
import com.savvasdalkitsis.gameframe.feature.composition.model.AvailablePorterDuffOperator
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.LayerSettings

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
        blendMode.visibility = View.VISIBLE
        blendMode.setSelection(AvailableBlendMode.indexOf(layer.layerSettings.blendMode))
        porterDuff.setSelection(AvailablePorterDuffOperator.indexOf(layer.layerSettings.porterDuffOperator))
        porterDuff.visibility = View.VISIBLE
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
