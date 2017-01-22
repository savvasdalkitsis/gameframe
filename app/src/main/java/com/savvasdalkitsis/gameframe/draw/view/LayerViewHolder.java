package com.savvasdalkitsis.gameframe.draw.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.grid.view.LedGridView;

class LayerViewHolder extends RecyclerView.ViewHolder {

    private final LedGridView ledGridView;
    private final View visibilityVisible;
    private final View visibilityInvisible;
    private final View delete;
    private final View duplicate;
    private final View settings;
    private final TextView title;

    LayerViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layer_view, parent, false));
        title = (TextView) itemView.findViewById(R.id.view_layer_title);
        visibilityVisible = itemView.findViewById(R.id.view_layer_visibility_visible);
        visibilityInvisible = itemView.findViewById(R.id.view_layer_visibility_invisible);
        delete = itemView.findViewById(R.id.view_layer_delete);
        duplicate = itemView.findViewById(R.id.view_layer_duplicate);
        settings = itemView.findViewById(R.id.view_layer_settings);
        ledGridView = (LedGridView) itemView.findViewById(R.id.view_layer_thumbnail);
        ledGridView.setThumbnailMode();
    }

    public void bind(Layer layer) {
        itemView.setSelected(layer.isSelected());
        title.setText(layer.getLayerSettings().getTitle());
        ledGridView.display(layer.getColorGrid());
        delete.setVisibility(View.VISIBLE);
        duplicate.setVisibility(View.VISIBLE);
        settings.setVisibility(View.VISIBLE);
        visibilityVisible.setVisibility(layer.isVisible() ? View.VISIBLE : View.GONE);
        visibilityInvisible.setVisibility(layer.isVisible() ? View.GONE : View.VISIBLE);
        hideControlsIfBackground(layer, delete, duplicate, settings, visibilityInvisible, visibilityVisible);
    }

    private void hideControlsIfBackground(Layer layer, View... views) {
        if (layer.isBackground()) {
            for (View view : views) {
                view.setVisibility(View.GONE);
            }
        }
    }

    void clearListeners() {
        setOnClickListener(null);
        setOnItemDeletedListener(OnItemDeletedListener.NO_OP);
        setOnLayerDuplicatedListener(OnLayerDuplicatedListener.NO_OP);
        setOnLayerSettingsClickedListener(OnLayerSettingsClickedListener.NO_OP);
        setOnLayerVisibilityChangedListener(OnLayerVisibilityChangedListener.NO_OP);
    }

    void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }

    void setOnItemDeletedListener(OnItemDeletedListener onItemDeletedListener) {
        delete.setOnClickListener(v -> onItemDeletedListener.onItemDeleted());
    }

    void setOnLayerDuplicatedListener(OnLayerDuplicatedListener onLayerDuplicatedListener) {
        duplicate.setOnClickListener(v -> onLayerDuplicatedListener.onLayerDuplicated());
    }

    void setOnLayerSettingsClickedListener(OnLayerSettingsClickedListener onLayerSettingsClickedListener) {
        settings.setOnClickListener(v -> onLayerSettingsClickedListener.onLayerSettingsClicked());
    }

    void setOnLayerVisibilityChangedListener(OnLayerVisibilityChangedListener onLayerVisibilityChangedListener) {
        visibilityVisible.setOnClickListener(v -> onLayerVisibilityChangedListener.onLayerVisibilityChanged(false));
        visibilityInvisible.setOnClickListener(v -> onLayerVisibilityChangedListener.onLayerVisibilityChanged(true));
    }
}
