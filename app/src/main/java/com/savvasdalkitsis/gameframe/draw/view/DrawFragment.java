package com.savvasdalkitsis.gameframe.draw.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.infra.view.FragmentSelectedListener;
import com.savvasdalkitsis.gameframe.view.grid.ColorGrid;
import com.savvasdalkitsis.gameframe.view.grid.LedGridView;
import com.shazam.android.aspects.base.fragment.AspectSupportFragment;

import butterknife.Bind;

@BindLayout(R.layout.fragment_draw)
public class DrawFragment extends AspectSupportFragment implements FragmentSelectedListener {

    @Bind(R.id.view_led_grid_view)
    public LedGridView ledGridView;
    FloatingActionButton fab;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.view_fab);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ColorGrid colorGrid = new ColorGrid();
        colorGrid.setColor(Color.RED, 4, 4);
        colorGrid.setColor(Color.RED, 4, 5);
        colorGrid.setColor(Color.RED, 4, 6);
        colorGrid.setColor(Color.BLUE, 6, 4);
        colorGrid.setColor(Color.BLUE, 6, 5);
        colorGrid.setColor(Color.BLUE, 6, 6);
        ledGridView.display(colorGrid);
    }

    @Override
    public void onFragmentSelected() {
        fab.setOnClickListener(v -> {});
        fab.setImageResource(R.drawable.ic_publish_white_48px);
    }
}
