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

public class LedGridView extends View {

    private ColorGrid colorGrid = new ColorGrid();
    private float side;
    private Paint paint;
    private Drawable tile;
    private GridTouchedListener gridTouchedListener = GridTouchedListener.NO_OP;

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
        //noinspection deprecation
        tile = getResources().getDrawable(R.drawable.transparency_background);
        setOnTouchListener(touched());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        side = getMeasuredWidth() / 16.0f;
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void setThumbnailMode() {
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.grid_line_width_thumbnail));
        setEnabled(false);
    }

    public void display(@NonNull ColorGrid colorGrid) {
        this.colorGrid = colorGrid;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        for (int column = 1; column <= 16; column++) {
            for (int row = 1; row <= 16; row++) {
                float left = (column - 1) * side;
                float top = (row - 1) * side;
                paint.setColor(colorGrid.getColor(column, row));
                tile.setBounds((int) left, (int) top, (int) (left + side), (int) (top + side));
                tile.draw(canvas);
                canvas.drawRect(left, top, left + side, top + side, paint);
            }
        }
        paint.setColor(Color.BLACK);
        for (int i = 0; i <= 16; i++) {
            canvas.drawLine(0, i * side, width, i * side, paint);
            //noinspection SuspiciousNameCombination
            canvas.drawLine(i * side, 0, i * side, width, paint);
        }
    }

    public void setOnGridTouchedListener(GridTouchedListener gridTouchedListener) {
        this.gridTouchedListener = gridTouchedListener;
    }

    public ColorGrid getColorGrid() {
        return colorGrid;
    }

    @NonNull
    private OnTouchListener touched() {
        return (view, motionEvent) -> {
            if (!isEnabled()) {
                return false;
            }
            int block = getWidth() / 16;
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_DOWN:
                    gridTouchedListener.onGridTouchedListener(
                            blockCoordinate(block, motionEvent.getX()),
                            blockCoordinate(block, motionEvent.getY())
                    );
                    return true;
            }
            return false;
        };
    }

    private int blockCoordinate(int block, float coordinate) {
        int c = (int) (coordinate / block) + 1;
        return Math.max(1, Math.min(c, 16));
    }
}
