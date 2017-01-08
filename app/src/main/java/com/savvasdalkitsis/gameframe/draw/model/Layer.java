package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;

import lombok.Getter;
import lombok.experimental.Builder;

@Getter
@Builder
public class Layer {

    private LayerSettings layerSettings;
    private ColorGrid colorGrid;
    private boolean isBackground;
    private boolean isVisible;

    public static Layer.LayerBuilder create(LayerSettings.LayerSettingsBuilder layerSettings) {
        return builder()
                .layerSettings(layerSettings.build())
                .isBackground(false)
                .isVisible(true)
                .colorGrid(new ColorGrid());
    }

    public static Layer.LayerBuilder from(Layer layer) {
        return builder()
                .isBackground(layer.isBackground)
                .layerSettings(layer.layerSettings)
                .colorGrid(layer.colorGrid.copy())
                .isVisible(layer.isVisible);
    }
}
