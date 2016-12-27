package com.savvasdalkitsis.gameframe.draw.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.draw.presenter.DrawPresenter;
import com.savvasdalkitsis.gameframe.grid.model.ColorGrid;
import com.savvasdalkitsis.gameframe.grid.view.GridTouchedListener;
import com.savvasdalkitsis.gameframe.grid.view.LedGridView;
import com.savvasdalkitsis.gameframe.infra.view.FragmentSelectedListener;
import com.savvasdalkitsis.gameframe.infra.view.Snackbars;
import com.shazam.android.aspects.base.fragment.AspectSupportFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;

import static com.savvasdalkitsis.gameframe.draw.model.Palette.Builder.palette;
import static com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector.drawPresenter;

@BindLayout(R.layout.fragment_draw)
public class DrawFragment extends AspectSupportFragment implements FragmentSelectedListener,
        SwatchSelectedListener, GridTouchedListener, DrawView, ColorChooserDialog.ColorCallback,
        ToolSelectedListener {

    @Bind(R.id.view_draw_led_grid_view)
    public LedGridView ledGridView;
    @Bind(R.id.view_draw_palette)
    public PaletteView paletteView;
    @Bind(R.id.view_draw_layers)
    public RecyclerView layersList;
    FloatingActionButton fab;
    @Bind(R.id.view_draw_drawer)
    public DrawerLayout drawer;
    private View fabProgress;
    private final DrawPresenter presenter = drawPresenter();
    @Nullable
    private SwatchView swatchToModify;
    private DrawingTool drawingTool;
    private int color;
    private final List<ToolView> toolsViews = new ArrayList<>();
    private LayersAdapter layers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.bindView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.view_fab);
        fabProgress = getActivity().findViewById(R.id.view_fab_progress);
        layersList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        layersList.setHasFixedSize(true);
        layers = new LayersAdapter();
        layers.onChange().subscribe(this::renderLayers);
        layersList.setAdapter(layers);
        renderLayers(layers.getLayers());
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                setFabState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                setFabState();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ledGridView.setOnGridTouchedListener(this);
        paletteView.bind(palette()
                .colors(getResources().getIntArray(R.array.palette))
                .build());
        paletteView.setOnSwatchSelectedListener(this);
        addTools(view,
                R.id.view_draw_pencil,
                R.id.view_draw_fill,
                R.id.view_draw_clear,
                R.id.view_draw_erase,
                R.id.view_draw_move
        );
        withAllTools(tool -> tool.setToolSelectedListener(this));

        toolsViews.get(0).performClick();
    }

    @Override
    public void onFragmentSelected() {
        setFabState();
    }

    @Override
    public void onSwatchSelected(int color) {
        this.color = color;
    }

    @Override
    public void onSwatchLongPressed(SwatchView swatch) {
        swatchToModify = swatch;
        new ColorChooserDialog.Builder((AppCompatActivity & ColorChooserDialog.ColorCallback) getContext(), R.string.change_color)
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if (swatchToModify != null) {
            swatchToModify.bind(selectedColor);
        }
    }

    @Override
    public void onGridTouchedListener(int startColumn, int startRow, int column, int row) {
        drawingTool.drawOn(layers.getSelectedLayer(), startColumn, startRow, column, row, color);
        renderLayers(layers.getLayers());
    }

    @Override
    public void onGridTouchFinished() {
        drawingTool.finishStroke(layers.getSelectedLayer());
    }

    @Override
    public void onToolSelected(DrawingTool drawingTool) {
        this.drawingTool = drawingTool;
        withAllTools(tool -> tool.setAlpha(0.2f));
    }

    @Override
    public void askForFileName(Action1<String> nameSelected) {
        new MaterialDialog.Builder(getActivity())
                .input(R.string.name_of_drawing, 0, false, (dialog, input) -> nameSelected.call(input.toString()))
                .title(R.string.enter_name_for_drawing)
                .positiveText(R.string.upload)
                .negativeText(android.R.string.cancel)
                .build()
                .show();
    }

    @Override
    public void fileUploaded() {
        Snackbars.success(coordinator(), R.string.success).show();
        scaleProgress(0);
    }

    @Override
    public void failedToUpload(Throwable e) {
        Log.e(DrawPresenter.class.getName(), "Error uploading to game frame", e);
        Snackbars.error(coordinator(), R.string.operation_failed).show();
        scaleProgress(0);
    }

    @Override
    public void displayUploading() {
        scaleProgress(1);
    }

    @Override
    public void drawingAlreadyExists(String name, ColorGrid colorGrid, Throwable e) {
        Log.e(DrawPresenter.class.getName(), "Drawing already exists", e);
        Snackbars.actionError(coordinator(), R.string.already_exists, R.string.replace,
                view -> presenter.replaceDrawing(name, colorGrid)).show();
        scaleProgress(0);
    }

    @Override
    public void failedToDelete(Throwable e) {
        Log.e(DrawPresenter.class.getName(), "Failed to delete drawing", e);
        Snackbars.error(coordinator(), R.string.operation_failed).show();
    }

    @SuppressLint("RtlHardcoded")
    @OnClick(R.id.view_draw_open_layers)
    public void openLayers() {
        drawer.openDrawer(Gravity.RIGHT);
    }

    @SuppressLint("RtlHardcoded")
    private void setFabState() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            fab.setOnClickListener(v -> layers.addNewLayer());
            fab.setImageResource(R.drawable.ic_add_white_48px);
        } else {
            fab.setOnClickListener(v -> presenter.upload(ledGridView.getColorGrid()));
            fab.setImageResource(R.drawable.ic_publish_white_48px);
        }
    }

    private void renderLayers(List<Layer> layers) {
        Observable.from(layers)
                .subscribe(layer -> layer.renderOn(ledGridView));
        ledGridView.invalidate();
    }

    private void scaleProgress(int value) {
        fabProgress.animate().scaleX(value).scaleY(value).start();
    }

    private View coordinator() {
        return getActivity().findViewById(R.id.view_coordinator);
    }

    private void withAllTools(Action1<ToolView> action) {
        Observable.from(toolsViews).subscribe(action);
    }

    private void addTools(View view, @IdRes int... viewResIds) {
        for (int viewResId : viewResIds) {
            toolsViews.add((ToolView) view.findViewById(viewResId));
        }
    }
}
