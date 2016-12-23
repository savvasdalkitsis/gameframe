package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.color.CircleView;

public class SwatchView extends CircleView {

    private int color;
    private PaletteView paletteView;

    public SwatchView(Context context) {
        super(context);
    }

    public SwatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(view -> {
            paletteView.deselectAllSwatches();
            setSelected(true);
            paletteView.notifyListenerOfSwatchSelected(this);
        });
        setOnLongClickListener(view -> {
            paletteView.notifyListenerOfSwatchLongClicked(this);
            return true;
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        paletteView = (PaletteView) getParent();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackground(Drawable background) {
        // needed to avoid parent illegal argument exception when inflating from xml
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void bind(int color) {
        this.color = color;
        setBackgroundColor(color);
    }

    public int getColor() {
        return color;
    }
}
