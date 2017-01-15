package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.draw.model.Historical;
import com.savvasdalkitsis.gameframe.draw.model.Model;

public class LayersView extends RecyclerView {

    private LayersAdapter layers;

    public LayersView(Context context) {
        super(context);
    }

    public LayersView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LayersView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        layers = new LayersAdapter();
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        setHasFixedSize(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MoveLayersItemHelper());
        itemTouchHelper.attachToRecyclerView(this);
        setAdapter(layers);
    }

    public void addNewLayer() {
        layers.addNewLayer();
    }

    public void bind(Historical<Model> modelHistory) {
        layers.bind(modelHistory);
    }

    private class MoveLayersItemHelper extends ItemTouchHelper.SimpleCallback {

        private final int ALLOW_DRAG = makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
        private boolean newMove = true;

        MoveLayersItemHelper() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        }

        @Override
        public void onMoved(RecyclerView recyclerView, ViewHolder viewHolder, int fromPos, ViewHolder target, int toPos, int x, int y) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            layers.swapLayers(viewHolder, target);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            if (newMove) {
                newMove = false;
                layers.swapStarted();
            }
            return target.getAdapterPosition() > 0;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            return viewHolder.getAdapterPosition() > 0 ? ALLOW_DRAG : 0;
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            motionFinished();
        }

        private void motionFinished() {
            newMove = true;
            layers.swapLayersFinished();
        }
    }
}