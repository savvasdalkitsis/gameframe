package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.composition.model.AvailableBlendMode;
import com.savvasdalkitsis.gameframe.composition.model.AvailablePorterDuffOperator;
import com.savvasdalkitsis.gameframe.composition.model.BlendMode;
import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator;
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;

import lombok.Getter;
import lombok.experimental.Builder;

@Getter
@Builder
public class Layer {

    private float alpha;
    private BlendMode blendMode;
    private PorterDuffOperator porterDuffOperator;
    private ColorGrid colorGrid;
    private boolean isBackground;
    private boolean isVisible;
    private String title;

    public static Layer.LayerBuilder create() {
        return builder()
                .title("")
                .blendMode(AvailableBlendMode.defaultMode())
                .porterDuffOperator(AvailablePorterDuffOperator.defaultOperator())
                .alpha(1)
                .isBackground(false)
                .isVisible(true)
                .colorGrid(new ColorGrid());
    }

    public static Layer.LayerBuilder from(Layer layer) {
        return builder()
                .title(layer.title)
                .isBackground(layer.isBackground)
                .alpha(layer.alpha)
                .colorGrid(layer.colorGrid)
                .porterDuffOperator(layer.porterDuffOperator)
                .isVisible(layer.isVisible)
                .blendMode(layer.blendMode);
    }
}
