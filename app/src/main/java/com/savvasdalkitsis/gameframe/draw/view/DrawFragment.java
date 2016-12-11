package com.savvasdalkitsis.gameframe.draw.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder;
import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingMode;
import com.savvasdalkitsis.gameframe.grid.view.GridTouchedListener;
import com.savvasdalkitsis.gameframe.infra.view.FragmentSelectedListener;
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.view.LedGridView;
import com.savvasdalkitsis.gameframe.infra.view.Snackbars;
import com.shazam.android.aspects.base.fragment.AspectSupportFragment;

import butterknife.Bind;

import static com.savvasdalkitsis.gameframe.draw.model.Palette.Builder.palette;

@BindLayout(R.layout.fragment_draw)
public class DrawFragment extends AspectSupportFragment implements FragmentSelectedListener,
        SwatchSelectedListener, GridTouchedListener {

    @Bind(R.id.view_draw_led_grid_view)
    public LedGridView ledGridView;
    @Bind(R.id.view_draw_palette)
    public PaletteView paletteView;
    FloatingActionButton fab;
    @Bind(R.id.view_draw_pencil)
    View pencil;
    @Bind(R.id.view_draw_fill)
    View fill;
    private DrawingMode drawingMode;
    private int color;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.view_fab);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ledGridView.setOnGridTouchedListener(this);
        paletteView.bind(palette()
                .colors(getResources().getIntArray(R.array.palette))
                .build());
        paletteView.setOnSwatchSelectedListener(this);
        fill.setOnClickListener(v -> {
            fill.setAlpha(1);
            pencil.setAlpha(0.2f);
            drawingMode = DrawingMode.FILL;
        });
        pencil.setOnClickListener(v -> {
            pencil.setAlpha(1);
            fill.setAlpha(0.2f);
            drawingMode = DrawingMode.PENCIL;
        });
        pencil.performClick();
    }

    @Override
    public void onFragmentSelected() {
        fab.setOnClickListener(v -> Snackbars.progress(getActivity().findViewById(R.id.view_coordinator), R.string.sending).show());
        fab.setImageResource(R.drawable.ic_publish_white_48px);
    }

    @Override
    public void onSwatchSelected(int color) {
        this.color = color;
    }

    @Override
    public void onGridTouchedListener(int column, int row) {
        ColorGrid colorGrid = ledGridView.getColorGrid();
        ledGridView.display(colorGrid);
        switch (drawingMode) {
            case FILL:
                colorGrid.fill(color);
                break;
            case PENCIL:
                colorGrid.setColor(color, column, row);
                break;
        }
        ledGridView.invalidate();
    }
}
