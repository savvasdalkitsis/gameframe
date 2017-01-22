package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.savvasdalkitsis.gameframe.draw.model.Palette;
import com.savvasdalkitsis.gameframe.model.Historical;
import com.savvasdalkitsis.gameframe.draw.model.Model;

public class PalettesView extends RecyclerView {

    private PalettesAdapter palettes;

    public PalettesView(Context context) {
        super(context);
    }

    public PalettesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PalettesView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        palettes = new PalettesAdapter();
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setHasFixedSize(true);
        setAdapter(palettes);
    }

    public void addNewPalette(Palette palette) {
        palettes.addNewPalette(palette);
    }

    public void bind(Historical<Model> modelHistory) {
        palettes.bind(modelHistory);
    }

}