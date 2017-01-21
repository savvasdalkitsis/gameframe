package com.savvasdalkitsis.gameframe.draw.model;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.savvasdalkitsis.gameframe.draw.model.Palette.Builder.palette;

public class Model implements Moment<Model> {

    private final List<Layer> layers;
    private final Palette palette;

    public Model() {
        this(newLayers(), newPalette());
    }

    private Model(List<Layer> layers, Palette palette) {
        this.layers = layers;
        this.palette = palette;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public Palette getPalette() {
        return palette;
    }

    public Layer getSelectedLayer() {
        return Observable.from(layers)
                .first(Layer::isSelected)
                .toBlocking()
                .first();
    }

    @Override
    public Model replicateMoment() {
        return new Model(new ArrayList<>(Observable.from(layers)
                .map(Layer::replicateMoment)
                .toList()
                .toBlocking()
                .first()), palette.replicateMoment());
    }

    @Override
    public boolean isIdenticalTo(Model moment) {
        if (layers.size() != moment.layers.size()) {
            return false;
        }
        if (!palette.isIdenticalTo(moment.getPalette())) {
            return false;
        }
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

    @Nullable
    private static Palette newPalette() {
        return palette()
                .colors(ApplicationInjector.application().getResources().getIntArray(R.array.palette))
                .build();
    }
}
