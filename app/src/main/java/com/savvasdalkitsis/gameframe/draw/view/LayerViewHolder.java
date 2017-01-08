package com.savvasdalkitsis.gameframe.draw.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.composition.model.AvailableBlendMode;
import com.savvasdalkitsis.gameframe.composition.model.AvailablePorterDuffOperator;
import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.grid.view.LedGridView;

class LayerViewHolder extends RecyclerView.ViewHolder {

    private final LedGridView ledGridView;
    private final Spinner blendMode;
    private final Spinner porterDuffMode;
    private final View visibilityVisible;
    private final View visibilityInvisible;
    private final View delete;
    private final View duplicate;
    private final SeekBar alpha;
    private final TextView title;

    LayerViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layer_view, parent, false));
        title = (TextView) itemView.findViewById(R.id.view_layer_title);
        alpha = (SeekBar) itemView.findViewById(R.id.view_layer_alpha);
        visibilityVisible = itemView.findViewById(R.id.view_layer_visibility_visible);
        visibilityInvisible = itemView.findViewById(R.id.view_layer_visibility_invisible);
        delete = itemView.findViewById(R.id.view_layer_delete);
        duplicate = itemView.findViewById(R.id.view_layer_duplicate);
        ledGridView = (LedGridView) itemView.findViewById(R.id.view_layer_thumbnail);
        ledGridView.setThumbnailMode();
        blendMode = (Spinner) itemView.findViewById(R.id.view_layer_blend_mode);
        porterDuffMode = (Spinner) itemView.findViewById(R.id.view_layer_porter_duff);
        blendMode.setAdapter(adapter(parent, AvailableBlendMode.values()));
        porterDuffMode.setAdapter(adapter(parent, AvailablePorterDuffOperator.values()));
    }

    @NonNull
    private <T> ArrayAdapter<T> adapter(ViewGroup parent, T[] values) {
        ArrayAdapter<T> adapter = new ArrayAdapter<>(parent.getContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void bind(Layer layer) {
        title.setText(layer.getTitle());
        ledGridView.display(layer.getColorGrid());
        delete.setVisibility(View.VISIBLE);
        duplicate.setVisibility(View.VISIBLE);
        alpha.setVisibility(View.VISIBLE);
        alpha.setProgress((int) (layer.getAlpha() * 100));
        blendMode.setVisibility(View.VISIBLE);
        blendMode.setSelection(AvailableBlendMode.indexOf(layer.getBlendMode()));
        porterDuffMode.setSelection(AvailablePorterDuffOperator.indexOf(layer.getPorterDuffOperator()));
        porterDuffMode.setVisibility(View.VISIBLE);
        visibilityVisible.setVisibility(layer.isVisible() ? View.VISIBLE : View.GONE);
        visibilityInvisible.setVisibility(layer.isVisible() ? View.GONE : View.VISIBLE);
        hideControlsIfBackground(layer, alpha, delete, duplicate, visibilityInvisible,
                visibilityVisible, blendMode, porterDuffMode);
    }

    private void hideControlsIfBackground(Layer layer, View... views) {
        if (layer.isBackground()) {
            for (View view : views) {
                view.setVisibility(View.GONE);
            }
        }
    }

    void setSelected(boolean selected) {
        itemView.setSelected(selected);
    }

    void clearListeners() {
        setOnClickListener(null);
        setOnLayerAlphaChangedListener(OnLayerAlphaChangedListener.NO_OP);
        setOnLayerDeletedListener(OnLayerDeletedListener.NO_OP);
        setOnLayerDuplicatedListener(OnLayerDuplicatedListener.NO_OP);
        setOnLayerVisibilityChangedListener(OnLayerVisibilityChangedListener.NO_OP);
        setOnLayerBlendModeSelectedListener(OnLayerBlendModeSelectedListener.NO_OP);
        setOnLayerPorterDuffOperatorSelectedListener(OnLayerPorterDuffOperatorSelectedListener.NO_OP);
    }

    void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }

    void setOnLayerAlphaChangedListener(OnLayerAlphaChangedListener onLayerAlphaChangedListener) {
        alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                onLayerAlphaChangedListener.onLayeralphaChanged(seekBar.getProgress() / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    void setOnLayerDeletedListener(OnLayerDeletedListener onLayerDeletedListener) {
        delete.setOnClickListener(v -> onLayerDeletedListener.onLayerDeleted());
    }

    void setOnLayerDuplicatedListener(OnLayerDuplicatedListener onLayerDuplicatedListener) {
        duplicate.setOnClickListener(v -> onLayerDuplicatedListener.onLayerDuplicated());
    }

    void setOnLayerVisibilityChangedListener(OnLayerVisibilityChangedListener onLayerVisibilityChangedListener) {
        visibilityVisible.setOnClickListener(v -> onLayerVisibilityChangedListener.onLayerVisibilityChanged(false));
        visibilityInvisible.setOnClickListener(v -> onLayerVisibilityChangedListener.onLayerVisibilityChanged(true));
    }

    void setOnLayerBlendModeSelectedListener(OnLayerBlendModeSelectedListener onLayerBlendModeSelectedListener) {
        blendMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                onLayerBlendModeSelectedListener.onLayerBlendModeSelected(AvailableBlendMode.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    void setOnLayerPorterDuffOperatorSelectedListener(OnLayerPorterDuffOperatorSelectedListener onLayerPorterDuffOperatorSelectedListener) {
        porterDuffMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                onLayerPorterDuffOperatorSelectedListener.onLayerPorterDuffOperatorSelected(AvailablePorterDuffOperator.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
