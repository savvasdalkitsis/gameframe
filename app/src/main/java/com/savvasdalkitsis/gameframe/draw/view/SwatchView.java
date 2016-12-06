package com.savvasdalkitsis.gameframe.draw.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.savvasdalkitsis.gameframe.R;

public class SwatchView extends View {

    private int color;
    private PaletteView paletteView;
    private float elevation;

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
        elevation = getResources().getDimension(R.dimen.swatch_elevation);
        setOnClickListener(view -> {
            paletteView.deselectAllSwatches();
            elevate(elevation);
            paletteView.notifyListenerOfSwatchSelected(this);
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        paletteView = (PaletteView) getParent();
    }

    public void deselect() {
        elevate(0);
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

    private void elevate(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ObjectAnimator.ofFloat(this, "elevation", elevation).start();
        }
    }
}
