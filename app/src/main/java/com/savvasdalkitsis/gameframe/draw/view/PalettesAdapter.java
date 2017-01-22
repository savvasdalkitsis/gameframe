package com.savvasdalkitsis.gameframe.draw.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.model.Historical;
import com.savvasdalkitsis.gameframe.draw.model.Model;
import com.savvasdalkitsis.gameframe.draw.model.Palette;

import java.util.List;


class PalettesAdapter extends RecyclerView.Adapter<PaletteViewHolder> {

    private Historical<Model> modelHistory;

    @Override
    public PaletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PaletteViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(PaletteViewHolder holder, int position) {
        holder.clearListeners();
        holder.bind(palettes().get(position));
        holder.setOnClickListener(v -> selectWithHistory(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return palettes().size();
    }

    private void selectWithHistory(int position) {
        progressTime();
        select(position);
        notifyObservers();
    }

    private void select(int position) {
        int selectedItemPosition = selectedItemPosition();
        palettes().get(selectedItemPosition).setSelected(false);
        palettes().get(position).setSelected(true);
        notifyItemChanged(selectedItemPosition);
        notifyItemChanged(position);
    }

    void addNewPalette(Palette palette) {
        progressTime();
        palettes().add(Palette.from(palette)
                .isSelected(true)
                .build());
        int lastIndex = palettes().size() - 1;
        notifyItemInserted(lastIndex);
        select(lastIndex);
        notifyObservers();
    }

    private List<Palette> palettes() {
        return modelHistory.present().getPalettes();
    }

    private void notifyObservers() {
        modelHistory.announcePresent();
    }

    private void progressTime() {
        modelHistory.progressTime();
    }

    void bind(Historical<Model> modelHistory) {
        this.modelHistory = modelHistory;
        modelHistory.observe().subscribe(model -> notifyDataSetChanged());
    }

    private int selectedItemPosition() {
        List<Palette> palettes = palettes();
        for (int i = 0; i < palettes.size(); i++) {
            Palette palette = palettes.get(i);
            if (palette.isSelected()) {
                return i;
            }
        }
        throw new IllegalStateException("Could not find selected palette");
    }
}
