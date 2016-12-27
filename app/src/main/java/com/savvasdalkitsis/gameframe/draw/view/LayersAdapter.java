package com.savvasdalkitsis.gameframe.draw.view;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.draw.model.Layer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

class LayersAdapter extends RecyclerView.Adapter<LayerViewHolder> {

    private final List<Layer> layers = new ArrayList<>();
    private final BehaviorSubject<List<Layer>> change = BehaviorSubject.create();
    private int selectedPosition = 0;

    LayersAdapter() {
        Layer layer = Layer.create().isBackground(true).build();
        layer.getColorGrid().fill(Color.GRAY);
        layers.add(layer);
    }

    @Override
    public LayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayerViewHolder holder = new LayerViewHolder(parent);
        holder.setOnClickListener(v -> select(holder.getAdapterPosition()));
        return holder;
    }

    @Override
    public void onBindViewHolder(LayerViewHolder holder, int position) {
        Layer layer = layers.get(position);
        holder.setOnLayerBlendModeSelectedListener(blendMode ->
                modifyLayer(position, Layer.from(layer).blendMode(blendMode).build()));
        holder.setOnLayerPorterDuffOperatorSelectedListener(porterDuffOperator ->
                modifyLayer(position, Layer.from(layer).porterDuffOperator(porterDuffOperator).build()));
        holder.setOnLayerVisibilityChangedListener(visible ->
                modifyLayer(position, Layer.from(layer).isVisible(visible).build()));
        holder.bind(layer);
        holder.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return layers.size();
    }

    List<Layer> getLayers() {
        return layers;
    }

    private void modifyLayer(int position, Layer layer) {
        layers.set(position, layer);
        notifyItemChanged(position);
        notifyObservers();
    }

    private void select(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
    }

    Layer getSelectedLayer() {
        return layers.get(selectedPosition);
    }

    void addNewLayer() {
        layers.add(Layer.create().build());
        int position = layers.size() - 1;
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
