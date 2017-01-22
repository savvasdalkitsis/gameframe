package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.Palette;
import com.savvasdalkitsis.gameframe.draw.model.PaletteSettings;

public class PaletteSettingsView extends LinearLayout {

    private EditText title;

    public PaletteSettingsView(Context context) {
        super(context);
    }

    public PaletteSettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaletteSettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    static void show(Context context, Palette palette, ViewGroup root, PaletteSettingsSetListener paletteSettingsSetListener) {
        PaletteSettingsView settingsView = (PaletteSettingsView) LayoutInflater.from(context).inflate(R.layout.view_palette_settings, root, false);
        settingsView.bindTo(palette);
        new AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                .setTitle(R.string.palette_settings)
                .setView(settingsView)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> paletteSettingsSetListener.onPaletteSettingsSet(settingsView.getPaletteSettings()))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {})
                .create()
                .show();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (EditText) findViewById(R.id.view_palette_title);
    }

    public void bindTo(Palette palette) {
        title.setText(palette.getTitle());
    }


    public PaletteSettings getPaletteSettings() {
        return PaletteSettings.builder()
                .title(title.getText().toString())
                .build();
    }
}
