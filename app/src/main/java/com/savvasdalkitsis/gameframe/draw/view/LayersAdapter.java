package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.model.Historical;
import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.draw.model.LayerSettings;
import com.savvasdalkitsis.gameframe.draw.model.Model;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

class LayersAdapter extends RecyclerView.Adapter<LayerViewHolder> {

    private Historical<Model> modelHistory;

    @Override
    public LayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LayerViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(LayerViewHolder holder, int position) {
        holder.clearListeners();
        holder.bind(layers().get(position));
        holder.setOnClickListener(v -> selectWithHistory(holder.getAdapterPosition()));
        holder.setOnLayerVisibilityChangedListener(visible ->
                modifyLayer(holder, layer -> layer.setVisible(visible)));
        holder.setOnItemDeletedListener(() -> removeLayer(holder));
        holder.setOnLayerDuplicatedListener(() -> duplicateLayer(holder));
        holder.setOnLayerSettingsClickedListener(() -> layerSettings(holder));
    }

    @Override
    public int getItemCount() {
        return layers().size();
    }

    private void modifyLayer(LayerViewHolder holder, Action1<Layer> layerModifier) {
        int position = holder.getAdapterPosition();
        progressTime();
        Layer layer = layers().get(position);
        layerModifier.call(layer);
        notifyItemChanged(position);
        notifyObservers();
    }

    private void selectWithHistory(int position) {
        progressTime();
        select(position);
    }

    private void select(int position) {
        int selectedItemPosition = selectedItemPosition();
        layers().get(selectedItemPosition).setSelected(false);
        layers().get(position).setSelected(true);
        notifyItemChanged(selectedItemPosition);
        notifyItemChanged(position);
        notifyObservers();
    }

    private void removeLayer(LayerViewHolder holder) {
        int position = holder.getAdapterPosition();
        progressTime();
        if (layers().get(position).isSelected()) {
            layers().get(position - 1).setSelected(true);
            notifyItemChanged(position - 1);
        }
        layers().remove(position);
        notifyItemRemoved(position);
        notifyObservers();
    }

    private void duplicateLayer(LayerViewHolder holder) {
        int position = holder.getAdapterPosition();
        Layer layer = layers().get(position);
        Layer newLayer = layer.replicateMoment();
        newLayer.getLayerSettings().setTitle(layer.getLayerSettings().getTitle() + " copy");
        addNewLayer(newLayer, position + 1);
    }

    void swapStarted() {
        modelHistory.progressTimeWithoutAnnouncing();
    }

    void swapLayers(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int itemPosition = viewHolder.getAdapterPosition();
        int targetPosition = target.getAdapterPosition();
        Collections.swap(layers(), itemPosition, targetPosition);
        notifyItemMoved(itemPosition, targetPosition);
    }

    void swapLayersFinished() {
        modelHistory.collapsePresentWithPastIfTheSame();
        notifyObservers();
    }

    private void layerSettings(LayerViewHolder holder) {
        Context context = holder.itemView.getContext();
        LayerSettingsView.show(context, layers().get(holder.getAdapterPosition()), (ViewGroup) holder.itemView,
                layerSettings -> modifyLayer(holder, layer -> layer.setLayerSettings(layerSettings)));
    }

    void addNewLayer() {
        addNewLayer(Layer.create(LayerSettings.create().title("Layer " + layers().size())).build(), layers().size());
    }

    private void addNewLayer(Layer layer, int position) {
        progressTime();
        layers().add(position, layer);
        notifyItemInserted(position);
        select(position);
    }

    private List<Layer> layers() {
        return modelHistory.present().getLayers();
    }

    private void notifyObservers() {
        modelHistory.announcePresent();
    }

    private void progressTime() {
        modelHistory.progressTime();
    }

    void bind(Historical<Model> modelHistory) {
        this.modelHistory = modelHistory;
        modelHistory.observe().subscribe(model -> notifyDataSetChanged());
    }

    private int selectedItemPosition() {
        List<Layer> layers = layers();
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            if (layer.isSelected()) {
                return i;
            }
        }
        throw new IllegalStateException("Could not find selected layer");
    }
}
