package com.savvasdalkitsis.gameframe.view.grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.savvasdalkitsis.gameframe.R;

public class LedGridView extends View {

    private ColorGrid colorGrid = new ColorGrid();
    private float side;
    private Paint paint;

    public LedGridView(Context context) {
        super(context);
    }

    public LedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LedGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        paint = new Paint();
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.grid_line_width));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        side = getMeasuredWidth() / 16.0f;
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void display(@NonNull ColorGrid colorGrid) {
        this.colorGrid = colorGrid;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        canvas.drawARGB(255, 0, 0, 0);
        for (int column = 1; column <= 16; column++) {
            for (int row = 1; row <= 16; row++) {
                float left = (column - 1) * side;
                float top = (row - 1) * side;
                paint.setColor(colorGrid.getColor(column, row));
                canvas.drawRect(left, top, left + side, top + side, paint);
            }
        }
        paint.setColor(Color.BLACK);
        for (int i = 1; i <= 15; i++) {
            canvas.drawLine(0, i * side, width, i * side, paint);
            canvas.drawLine(i * side, 0, i * side, width, paint);
        }
    }
}
