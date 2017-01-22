package com.savvasdalkitsis.gameframe.draw.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.Palette;

class PaletteViewHolder extends RecyclerView.ViewHolder {

    private final PaletteView paletteView;
    private final TextView title;
    private final View edit;
    private final View delete;
    private final View controls;

    PaletteViewHolder(ViewGroup parent, boolean editable) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_palette_view, parent, false));
        title = (TextView) itemView.findViewById(R.id.view_palette_title);
        paletteView = (PaletteView) itemView.findViewById(R.id.view_palette_thumbnail);
        paletteView.setThumbnailMode();
        edit = itemView.findViewById(R.id.view_palette_edit);
        delete = itemView.findViewById(R.id.view_palette_delete);
        controls = itemView.findViewById(R.id.view_palette_controls);
        setEditable(editable);
    }

    private void setEditable(boolean editable) {
        controls.setVisibility(editable ? View.VISIBLE : View.GONE);
    }

    void setDeletable(boolean deletable) {
        delete.setVisibility(deletable ? View.VISIBLE : View.GONE);
    }

    public void bind(Palette palette) {
        title.setText(palette.getTitle());
        paletteView.bind(palette);
    }

    void clearListeners() {
        setOnClickListener(null);
        setOnItemDeletedListener(null);
    }

    void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }

    void setOnItemDeletedListener(OnItemDeletedListener onItemDeletedListener) {
        delete.setOnClickListener(v -> onItemDeletedListener.onItemDeleted());
    }

    void setOnPaletteEditClickedListener(OnPaletteEditClickedListener onPaletteEditClickedListener) {
        edit.setOnClickListener(v -> onPaletteEditClickedListener.onPaletteEditClicked());
    }
}
