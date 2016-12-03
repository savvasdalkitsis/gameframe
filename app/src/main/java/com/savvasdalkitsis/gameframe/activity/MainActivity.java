package com.savvasdalkitsis.gameframe.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.view.grid.ColorGrid;
import com.savvasdalkitsis.gameframe.view.grid.LedGridView;
import com.shazam.android.aspects.base.activity.AspectAppCompatActivity;

import butterknife.Bind;

@BindLayout(R.layout.activity_main)
public class MainActivity extends AspectAppCompatActivity {

    @Bind(R.id.view_led_grid_view)
    public LedGridView ledGridView;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ColorGrid colorGrid = new ColorGrid();
        colorGrid.setColor(Color.RED, 4, 4);
        colorGrid.setColor(Color.RED, 4, 5);
        colorGrid.setColor(Color.RED, 4, 6);
        colorGrid.setColor(Color.BLUE, 6, 4);
        colorGrid.setColor(Color.BLUE, 6, 5);
        colorGrid.setColor(Color.BLUE, 6, 6);
        ledGridView.display(colorGrid);
    }
}
