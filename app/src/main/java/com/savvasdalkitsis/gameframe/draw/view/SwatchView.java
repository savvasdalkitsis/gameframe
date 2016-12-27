package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.color.CircleView;
import com.savvasdalkitsis.gameframe.R;

public class SwatchView extends CircleView {

    private int color;
    private PaletteView paletteView;
    private Path circlePath;
    private RectF circleRect;
    private Drawable tile;

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
        circlePath = new Path();
        circleRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredWidth());
        //noinspection deprecation
        tile = getResources().getDrawable(R.drawable.transparency_background);
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
        circlePath.reset();
        circleRect.set(0, 0, getMeasuredWidth(), getMeasuredWidth());
        circlePath.addOval(circleRect, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(circlePath);
        tile.setBounds(0, 0, getMeasuredWidth(), getMeasuredWidth());
        tile.draw(canvas);
        canvas.restore();
        super.onDraw(canvas);
    }

    public void bind(int color) {
        this.color = color;
        setBackgroundColor(color);
    }

    public int getColor() {
        return color;
    }
}
