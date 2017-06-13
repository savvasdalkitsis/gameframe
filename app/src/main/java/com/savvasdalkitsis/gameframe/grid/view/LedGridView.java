package com.savvasdalkitsis.gameframe.grid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.model.Grid;
import com.savvasdalkitsis.gameframe.math.MathExtras;

public class LedGridView extends View {

    private Grid colorGrid = new ColorGrid();
    private float tileSide;
    private Paint paint;
    private Drawable thumbBackground;
    private GridTouchedListener gridTouchedListener = GridTouchedListener.NO_OP;
    private float startX;
    private float startY;
    private int columnTranslation;
    private int rowTranslation;

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
        setOnTouchListener(touched());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        tileSide = getMeasuredWidth() / (float) ColorGrid.SIDE;
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void setThumbnailMode() {
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.grid_line_width_thumbnail));
        setEnabled(false);
        //noinspection deprecation
        thumbBackground = getResources().getDrawable(R.drawable.transparency_backround_tiled);
    }

    public void display(@NonNull Grid colorGrid) {
        this.colorGrid = colorGrid;
        invalidate();
    }

    public void displayBoundaries(int columnTranslation, int rowTranslation) {
        this.columnTranslation = columnTranslation;
        this.rowTranslation = rowTranslation;
    }

    public void clearBoundaries() {
        displayBoundaries(0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int gridSide = getMeasuredWidth();
        if (thumbBackground != null) {
            thumbBackground.setBounds(0, 0, gridSide, gridSide);
            thumbBackground.draw(canvas);
        }
        for (int column = 1; column <= ColorGrid.SIDE; column++) {
            for (int row = 1; row <= ColorGrid.SIDE; row++) {
                float left = toPx(column - 1);
                float top = toPx(row - 1);
                paint.setColor(colorGrid.getColor(column, row));
                canvas.drawRect(left, top, left + tileSide, top + tileSide, paint);
            }
        }
        paint.setColor(Color.BLACK);
        for (int i = 0; i <= ColorGrid.SIDE; i++) {
            drawRow(i, 0, canvas);
            drawColumn(i, 0, canvas);
        }
        if (columnTranslation != 0 || rowTranslation != 0) {
            paint.setColor(Color.LTGRAY);

            drawRow(rowTranslation, columnTranslation, canvas);
            drawRow(ColorGrid.SIDE + rowTranslation, columnTranslation, canvas);
            drawColumn(columnTranslation, rowTranslation, canvas);
            drawColumn(ColorGrid.SIDE + columnTranslation, rowTranslation, canvas);
        }
    }

    private void drawColumn(int column, int verticalOffset, Canvas canvas) {
        if (column >= 0 && column <= ColorGrid.SIDE) {
            canvas.drawLine(toPx(column), toPx(verticalOffset), toPx(column), getMeasuredWidth() + toPx(verticalOffset) , paint);
        }
    }

    private void drawRow(int row, int horizontalOffset, Canvas canvas) {
        if (row >= 0 && row <= ColorGrid.SIDE) {
            canvas.drawLine(toPx(horizontalOffset), toPx(row), getMeasuredWidth() + toPx(horizontalOffset), toPx(row), paint);
        }
    }

    private float toPx(int tile) {
        return tile * tileSide;
    }

    public void setOnGridTouchedListener(GridTouchedListener gridTouchedListener) {
        this.gridTouchedListener = gridTouchedListener;
    }

    public Grid getColorGrid() {
        return colorGrid;
    }

    @NonNull
    private OnTouchListener touched() {
        return (view, motionEvent) -> {
            if (!isEnabled()) {
                return false;
            }
            int block = getWidth() / ColorGrid.SIDE;
            float x;
            float y;
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    gridTouchedListener.onGridTouchStarted();
                    gridTouchedListener.onGridTouch(
                            blockCoordinate(block, startX),
                            blockCoordinate(block, startY),
                            blockCoordinate(block, x),
                            blockCoordinate(block, y)
                    );
                    return true;
                case MotionEvent.ACTION_MOVE:
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    gridTouchedListener.onGridTouch(
                            blockCoordinate(block, startX),
                            blockCoordinate(block, startY),
                            blockCoordinate(block, x),
                            blockCoordinate(block, y)
                    );
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    gridTouchedListener.onGridTouchFinished();
            }
            return false;
        };
    }

    private int blockCoordinate(int block, float coordinate) {
        int c = (int) (coordinate / block) + 1;
        return MathExtras.clip(1, c, ColorGrid.SIDE);
    }
}
