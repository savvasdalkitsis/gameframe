package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.composition.model.AvailableBlendMode;
import com.savvasdalkitsis.gameframe.composition.model.AvailablePorterDuffOperator;
import com.savvasdalkitsis.gameframe.composition.model.BlendMode;
import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator;
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.view.LedGridView;

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

    public void renderOn(LedGridView ledGridView) {
        if (!isVisible) {
            return;
        }
        if (isBackground) {
            ledGridView.display(colorGrid);
        } else {
            ColorGrid composite = colorGrid.compose(ledGridView.getColorGrid(), colorGrid, blendMode, porterDuffOperator, alpha);
            ledGridView.display(composite);
        }
    }

    public static Layer.LayerBuilder create() {
        return builder()
                .blendMode(AvailableBlendMode.defaultMode())
                .porterDuffOperator(AvailablePorterDuffOperator.defaultOperator())
                .alpha(1)
                .isBackground(false)
                .isVisible(true)
                .colorGrid(new ColorGrid());
    }

    public static Layer.LayerBuilder from(Layer layer) {
        return builder()
                .isBackground(layer.isBackground)
                .alpha(layer.alpha)
                .colorGrid(layer.colorGrid)
                .porterDuffOperator(layer.porterDuffOperator)
                .isVisible(layer.isVisible)
                .blendMode(layer.blendMode);
    }
}
