package com.savvasdalkitsis.gameframe.composition.usecase;

import com.savvasdalkitsis.gameframe.composition.model.ARGB;
import com.savvasdalkitsis.gameframe.composition.model.BlendMode;
import com.savvasdalkitsis.gameframe.composition.model.PorterDuffOperator;
import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.view.LedGridView;

public class BlendUseCase {

    public void renderOn(Layer layer, LedGridView ledGridView) {
        if (!layer.isVisible()) {
            return;
        }
        if (layer.isBackground()) {
            ledGridView.display(layer.getColorGrid());
        } else {
            ColorGrid composite = compose(ledGridView.getColorGrid(), layer.getColorGrid(), layer.getBlendMode(), layer.getPorterDuffOperator(), layer.getAlpha());
            ledGridView.display(composite);
        }
    }

    private ColorGrid compose(ColorGrid dest, ColorGrid source, BlendMode blendMode, PorterDuffOperator porterDuffOperator, float alpha) {
        ColorGrid colorGrid = new ColorGrid();
        for (int col = 1; col <= ColorGrid.SIDE; col++) {
            for (int row = 1; row <= ColorGrid.SIDE; row++) {
                ARGB blend = mix(source.getColor(col, row), dest.getColor(col, row),
                        blendMode, porterDuffOperator, alpha);
                colorGrid.setColor(blend.color(), col, row);
            }
        }
        return colorGrid;
    }

    private ARGB mix(int source, int dest, BlendMode blendMode, PorterDuffOperator porterDuffOperator, float alpha) {
        ARGB alphaSource = new ARGB(source).multiplyAlpha(alpha);
        ARGB destination = new ARGB(dest);
        ARGB blend = blend(alphaSource, destination, blendMode);
        return compose(blend, destination, porterDuffOperator);
    }

    private static ARGB blend(ARGB source, ARGB dest, BlendMode blendMode) {
        return source.multiply(1 - toFloat(dest.a)).add(blendMode.blend(source, dest).multiply(toFloat(dest.a)));
    }

    private static ARGB compose(ARGB source, ARGB dest, PorterDuffOperator porterDuffOperator) {
        return source.multiply(toFloat(source.a) * porterDuffOperator.fa(dest)).add(dest.multiply(toFloat(dest.a) * porterDuffOperator.fb(source)));
    }

    private static float toFloat(int inBytes) {
        return inBytes / 255f;
    }
}
