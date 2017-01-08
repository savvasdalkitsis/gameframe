package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.composition.model.AvailableBlendMode;
import com.savvasdalkitsis.gameframe.composition.model.AvailablePorterDuffOperator;
import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.draw.model.LayerSettings;

public class LayerSettingsView extends LinearLayout {

    private EditText title;
    private Spinner blendMode;
    private Spinner porterDuff;
    private SeekBar alpha;

    public LayerSettingsView(Context context) {
        super(context);
    }

    public LayerSettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayerSettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    static void show(Context context, Layer layer, ViewGroup root, LayerSettingsSetListener layerSettingsSetListener) {
        LayerSettingsView settingsView = (LayerSettingsView) LayoutInflater.from(context).inflate(R.layout.view_layer_settings, root, false);
        settingsView.bindTo(layer);
        new AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                .setTitle(R.string.layer_settings)
                .setView(settingsView)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> layerSettingsSetListener.onLayerSettingsSet(settingsView.getLayerSettings()))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {})
                .create()
                .show();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (EditText) findViewById(R.id.view_layer_title);
        alpha = (SeekBar) findViewById(R.id.view_layer_alpha);
        blendMode = (Spinner) findViewById(R.id.view_layer_blend_mode);
        blendMode.setAdapter(adapter(AvailableBlendMode.values()));
        porterDuff = (Spinner) findViewById(R.id.view_layer_porter_duff);
        porterDuff.setAdapter(adapter(AvailablePorterDuffOperator.values()));
    }

    public void bindTo(Layer layer) {
        title.setText(layer.getLayerSettings().getTitle());
        alpha.setProgress((int) (layer.getLayerSettings().getAlpha() * 100));
        blendMode.setVisibility(View.VISIBLE);
        blendMode.setSelection(AvailableBlendMode.indexOf(layer.getLayerSettings().getBlendMode()));
        porterDuff.setSelection(AvailablePorterDuffOperator.indexOf(layer.getLayerSettings().getPorterDuffOperator()));
        porterDuff.setVisibility(View.VISIBLE);
    }

    @NonNull
    private <T> ArrayAdapter<T> adapter(T[] values) {
        ArrayAdapter<T> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public LayerSettings getLayerSettings() {
        return LayerSettings.builder()
                .title(title.getText().toString())
                .alpha(alpha.getProgress() / 100f)
                .blendMode(AvailableBlendMode.values()[blendMode.getSelectedItemPosition()])
                .porterDuffOperator(AvailablePorterDuffOperator.values()[porterDuff.getSelectedItemPosition()])
                .build();
    }
}
