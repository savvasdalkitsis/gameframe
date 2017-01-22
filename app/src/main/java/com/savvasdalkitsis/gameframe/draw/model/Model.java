package com.savvasdalkitsis.gameframe.draw.model;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.model.Moment;
import com.savvasdalkitsis.gameframe.model.MomentList;

import java.util.List;

import rx.Observable;

public class Model implements Moment<Model> {

    private final MomentList<Layer> layers;
    private final MomentList<Palette> palettes;

    public Model() {
        this(newLayers(), newPalettes());
    }

    private Model(MomentList<Layer> layers, MomentList<Palette> palettes) {
        this.layers = layers;
        this.palettes = palettes;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public List<Palette> getPalettes() {
        return palettes;
    }

    public Palette getSelectedPalette() {
        return Observable.from(palettes)
                .first(Palette::isSelected)
                .toBlocking()
                .first();
    }

    public Layer getSelectedLayer() {
        return Observable.from(layers)
                .first(Layer::isSelected)
                .toBlocking()
                .first();
    }

    @Override
    public Model replicateMoment() {
        return new Model(layers.replicateMoment(), palettes.replicateMoment());
    }

    @Override
    public boolean isIdenticalTo(Model moment) {
        return layers.isIdenticalTo(moment.layers) && palettes.isIdenticalTo(moment.palettes);
    }

    @NonNull
    private static MomentList<Layer> newLayers() {
        return new MomentList<>(Layer.create(LayerSettings.create().title("Background"))
                .isBackground(true)
                .isSelected(true)
                .colorGrid(new ColorGrid().fill(Color.GRAY))
                .build());
    }

    @Nullable
    private static MomentList<Palette> newPalettes() {
        return new MomentList<>(Palettes.defaultPalette()
                .isSelected(true)
                .build());
    }

}
