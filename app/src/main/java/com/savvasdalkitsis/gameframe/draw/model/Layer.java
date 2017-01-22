package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.model.Moment;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

@Getter
@Setter
@Builder
public class Layer implements Moment<Layer> {

    private LayerSettings layerSettings;
    private ColorGrid colorGrid;
    private boolean isBackground;
    private boolean isVisible;
    private boolean isSelected;

    public static Layer.LayerBuilder create(LayerSettings.LayerSettingsBuilder layerSettings) {
        return builder()
                .layerSettings(layerSettings.build())
                .isBackground(false)
                .isVisible(true)
                .isSelected(false)
                .colorGrid(new ColorGrid());
    }

    @Override
    public Layer replicateMoment() {
        return builder()
                .isBackground(isBackground)
                .layerSettings(layerSettings.replicateMoment())
                .colorGrid(colorGrid.replicateMoment())
                .isSelected(isSelected)
                .isVisible(isVisible).build();
    }

    @Override
    public boolean isIdenticalTo(Layer moment) {
        return isBackground == moment.isBackground
                && isVisible == moment.isVisible
                && isSelected == moment.isSelected
                && colorGrid.isIdenticalTo(moment.colorGrid)
                && layerSettings.isIdenticalTo(moment.layerSettings);
    }
}
