package com.savvasdalkitsis.gameframe.draw.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.Palettes;

public class AddPaletteView extends RecyclerView {

    private SimplePalettesAdapter palettes = new SimplePalettesAdapter(Palettes.preLoaded());

    public AddPaletteView(Context context) {
        super(context);
    }

    public AddPaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddPaletteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    static void show(Context context, ViewGroup root, AddNewPaletteSelectedListener addNewPaletteSelectedListener) {
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                .setTitle(R.string.add_new_palette)
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {})
                .create();
        AddPaletteView addPaletteView = (AddPaletteView) LayoutInflater.from(context).inflate(R.layout.view_add_palette_view, root, false);
        addPaletteView.setOnAddNewPaletteSelectedListener(palette -> {
            dialog.dismiss();
            addNewPaletteSelectedListener.onAddNewPalletSelected(palette);
        });
        dialog.setView(addPaletteView);
        dialog.show();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setHasFixedSize(true);
        setAdapter(palettes);
    }

    public void setOnAddNewPaletteSelectedListener(AddNewPaletteSelectedListener onAddNewPaletteSelectedListener) {
        palettes.setOnAddNewPaletteSelectedListener(onAddNewPaletteSelectedListener);
    }
}
