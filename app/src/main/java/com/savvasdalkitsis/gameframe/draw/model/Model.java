package com.savvasdalkitsis.gameframe.draw.model;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class Model implements Moment<Model> {

    private final List<Layer> layers;

    public Model() {
        this(newLayers());
    }

    private Model(List<Layer> layers) {
        this.layers = layers;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    @Override
    public Model replicateMoment() {
        return new Model(new ArrayList<>(Observable.from(layers)
                .map(Layer::replicateMoment)
                .toList()
                .toBlocking()
                .first()));
    }

    @Override
    public boolean isIdenticalTo(Model moment) {
        return Observable.zip(Observable.from(layers), Observable.from(moment.layers), Pair::create)
                .filter(pair -> !pair.first.isIdenticalTo(pair.second)).isEmpty()
                .toBlocking()
                .first();
    }

    @NonNull
    private static ArrayList<Layer> newLayers() {
        ArrayList<Layer> layers = new ArrayList<>();
        Layer layer = Layer.create(LayerSettings.create().title("Background"))
                .isBackground(true)
                .isSelected(true)
                .build();
        layer.getColorGrid().fill(Color.GRAY);
        layers.add(layer);
        return layers;
    }

    public Layer getSelectedLayer() {
        return Observable.from(layers)
                .first(Layer::isSelected)
                .toBlocking()
                .first();
    }
}
