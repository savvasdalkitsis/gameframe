package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.draw.model.Palette;

public class PaletteView extends GridLayout {

    private static final int SWATCH_COUNT = 20;
    private SwatchView[] swatches;
    private SwatchSelectedListener swatchSelectedListener = SwatchSelectedListener.NO_OP;

    public PaletteView(Context context) {
        super(context);
    }

    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaletteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != SWATCH_COUNT) {
            throw new IllegalStateException("Wrong number of swatches in PaletteView. Expecting " + SWATCH_COUNT + " found " + childCount);
        }
        swatches = new SwatchView[SWATCH_COUNT];
        for (int i = 0; i < SWATCH_COUNT; i++) {
            swatches[i] = (SwatchView) getChildAt(i);
            SwatchView swatch = swatches[i];
            swatch.setOnLongClickListener(view -> {
                swatchSelectedListener.onSwatchLongPressed(swatch);
                return true;
            });
        }
    }

    public void bind(Palette palette) {
        int[] colors = palette.getColors();
        for (int i = 0; i < SWATCH_COUNT; i++) {
            swatches[i].bind(colors[i]);
        }
    }

    public void deselectAllSwatches() {
        for (SwatchView swatch : swatches) {
            swatch.deselect();
        }
    }

    public void setOnSwatchSelectedListener(SwatchSelectedListener swatchSelectedListener) {
        this.swatchSelectedListener = swatchSelectedListener;
    }

    public void notifyListenerOfSwatchSelected(SwatchView swatchView) {
        swatchSelectedListener.onSwatchSelected(swatchView.getColor());
    }
}
