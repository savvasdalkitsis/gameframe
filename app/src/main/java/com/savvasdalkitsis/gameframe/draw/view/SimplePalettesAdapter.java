package com.savvasdalkitsis.gameframe.draw.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.draw.model.Palette;


class SimplePalettesAdapter extends RecyclerView.Adapter<PaletteViewHolder> {

    private Palette[] palettes;
    private AddNewPaletteSelectedListener onAddNewPaletteSelectedListener;

    SimplePalettesAdapter(Palette[] palettes) {
        this.palettes = palettes;
    }

    @Override
    public PaletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PaletteViewHolder(parent, false);
    }

    @Override
    public void onBindViewHolder(PaletteViewHolder holder, int position) {
        holder.clearListeners();
        holder.bind(palettes[position]);
        holder.setOnClickListener(v -> onAddNewPaletteSelectedListener.onAddNewPalletSelected(palettes[position]));
    }

    @Override
    public int getItemCount() {
        return palettes.length;
    }

    void setOnAddNewPaletteSelectedListener(AddNewPaletteSelectedListener onAddNewPaletteSelectedListener) {
        this.onAddNewPaletteSelectedListener = onAddNewPaletteSelectedListener;
    }

}
