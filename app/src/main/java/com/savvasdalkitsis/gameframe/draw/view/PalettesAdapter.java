package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.model.Historical;
import com.savvasdalkitsis.gameframe.draw.model.Model;
import com.savvasdalkitsis.gameframe.draw.model.Palette;

import java.util.List;

import rx.functions.Action1;


class PalettesAdapter extends RecyclerView.Adapter<PaletteViewHolder> {

    private Historical<Model> modelHistory;

    @Override
    public PaletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PaletteViewHolder(parent, true);
    }

    @Override
    public void onBindViewHolder(PaletteViewHolder holder, int position) {
        holder.clearListeners();
        holder.bind(palettes().get(position));
        holder.setOnClickListener(v -> selectWithHistory(holder.getAdapterPosition()));
        holder.setOnItemDeletedListener(() -> removePalette(holder));
        holder.setOnPaletteEditClickedListener(() -> paletteEdit(holder));
        holder.setDeletable(palettes().size() > 1);
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

    private void modifyPalette(PaletteViewHolder holder, Action1<Palette> paletteModifier) {
        int position = holder.getAdapterPosition();
        progressTime();
        Palette palette = palettes().get(position);
        paletteModifier.call(palette);
        notifyItemChanged(position);
        notifyObservers();
    }

    private void removePalette(PaletteViewHolder holder) {
        int position = holder.getAdapterPosition();
        progressTime();
        if (palettes().get(position).isSelected()) {
            palettes().get(position - 1).setSelected(true);
            notifyItemChanged(position - 1);
        }
        palettes().remove(position);
        notifyItemRemoved(position);
        notifyObservers();
        if (palettes().size() == 1) {
            notifyDataSetChanged();
        }
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
        if (palettes().size() == 2) {
            notifyDataSetChanged();
        }
    }

    private void paletteEdit(PaletteViewHolder holder) {
        Context context = holder.itemView.getContext();
        PaletteSettingsView.show(context, palettes().get(holder.getAdapterPosition()), (ViewGroup) holder.itemView,
                paletteSettings -> modifyPalette(holder, palette -> palette.setTitle(paletteSettings.getTitle())));
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
