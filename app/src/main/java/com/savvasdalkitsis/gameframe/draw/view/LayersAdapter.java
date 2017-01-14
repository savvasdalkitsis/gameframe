package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.draw.model.LayerSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

class LayersAdapter extends RecyclerView.Adapter<LayerViewHolder> {

    private final List<Layer> layers = new ArrayList<>();
    private final BehaviorSubject<List<Layer>> change = BehaviorSubject.create();
    private int selectedPosition = 0;

    LayersAdapter() {
        Layer layer = Layer.create(LayerSettings.create().title("Background")).isBackground(true).build();
        layer.getColorGrid().fill(Color.GRAY);
        layers.add(layer);
    }

    @Override
    public LayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LayerViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(LayerViewHolder holder, int position) {
        holder.clearListeners();
        holder.bind(layers.get(position));
        holder.setSelected(selectedPosition == position);
        holder.setOnClickListener(v -> select(holder.getAdapterPosition()));
        holder.setOnLayerVisibilityChangedListener(visible ->
                modifyLayer(holder, layer -> layer.isVisible(visible)));
        holder.setOnLayerDeletedListener(() -> removeLayer(holder));
        holder.setOnLayerDuplicatedListener(() -> duplicateLayer(holder));
        holder.setOnLayerSettingsClickedListener(() -> layerSettings(holder));
    }

    @Override
    public int getItemCount() {
        return layers.size();
    }

    List<Layer> getLayers() {
        return layers;
    }

    private void modifyLayer(LayerViewHolder holder, Func1<Layer.LayerBuilder, Layer.LayerBuilder> layerBuilder) {
        int position = holder.getAdapterPosition();
        Layer layer = layers.get(position);
        layers.set(position, layerBuilder.call(Layer.from(layer)).build());
        notifyItemChanged(position);
        notifyObservers();
    }

    private void select(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
    }

    private void removeLayer(LayerViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (selectedPosition >= position) {
            selectedPosition--;
        }
        layers.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(selectedPosition);
        notifyObservers();
    }

    private void duplicateLayer(LayerViewHolder holder) {
        int position = holder.getAdapterPosition();
        Layer layer = layers.get(position);
        Layer newLayer = Layer.from(layer)
                .layerSettings(LayerSettings.from(layer.getLayerSettings())
                        .title(layer.getLayerSettings().getTitle() + " copy")
                        .build())
                .build();
        addNewLayer(newLayer, position + 1);
    }

    void swapLayers(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int itemPosition = viewHolder.getAdapterPosition();
        int targetPosition = target.getAdapterPosition();
        Collections.swap(layers, itemPosition, targetPosition);
        notifyItemMoved(itemPosition, targetPosition);
        if (selectedPosition == itemPosition) {
            selectedPosition = targetPosition;
        } else if (selectedPosition == targetPosition) {
            selectedPosition = itemPosition;
        }
        notifyObservers();
    }

    private void layerSettings(LayerViewHolder holder) {
        Context context = holder.itemView.getContext();
        LayerSettingsView.show(context, layers.get(holder.getAdapterPosition()), (ViewGroup) holder.itemView,
                layerSettings -> modifyLayer(holder, layer -> layer.layerSettings(layerSettings)));
    }

    Layer getSelectedLayer() {
        return layers.get(selectedPosition);
    }

    void addNewLayer() {
        addNewLayer(Layer.create(LayerSettings.create().title("Layer " + layers.size())).build(), layers.size());
    }

    private void addNewLayer(Layer layer, int position) {
        if (selectedPosition >= position) {
            selectedPosition++;
        }
        layers.add(position, layer);
        notifyItemInserted(position);
        select(position);
        notifyObservers();
    }

    private void notifyObservers() {
        change.onNext(getLayers());
    }

    Observable<List<Layer>> onChange() {
        return change;
    }
}
