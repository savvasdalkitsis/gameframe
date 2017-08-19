package com.savvasdalkitsis.gameframe.draw.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.draw.model.Palette
import com.savvasdalkitsis.gameframe.draw.model.PaletteSettings

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
