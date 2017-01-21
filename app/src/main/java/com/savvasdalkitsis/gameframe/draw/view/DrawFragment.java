package com.savvasdalkitsis.gameframe.draw.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.composition.usecase.BlendUseCase;
import com.savvasdalkitsis.gameframe.draw.model.DrawingTool;
import com.savvasdalkitsis.gameframe.draw.model.Historical;
import com.savvasdalkitsis.gameframe.draw.model.Layer;
import com.savvasdalkitsis.gameframe.draw.model.Model;
import com.savvasdalkitsis.gameframe.draw.presenter.DrawPresenter;
import com.savvasdalkitsis.gameframe.grid.model.Grid;
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

import static com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector.drawPresenter;
import static com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.blendUseCase;

@BindLayout(R.layout.fragment_draw)
public class DrawFragment extends AspectSupportFragment implements FragmentSelectedListener,
        SwatchSelectedListener, GridTouchedListener, DrawView, ColorChooserDialog.ColorCallback,
        ToolSelectedListener {

    @Bind(R.id.view_draw_led_grid_view)
    public LedGridView ledGridView;
    @Bind(R.id.view_draw_palette)
    public PaletteView paletteView;
    @Bind(R.id.view_draw_layers)
    public LayersView layersList;
    FloatingActionButton fab;
    @Bind(R.id.view_draw_drawer)
    public DrawerLayout drawer;
    private View fabProgress;
    private final DrawPresenter presenter = drawPresenter();
    private final BlendUseCase blendUseCase = blendUseCase();
    @Nullable
    private SwatchView swatchToModify;
    private DrawingTool drawingTool;
    private final List<ToolView> toolsViews = new ArrayList<>();
    private Historical<Model> modelHistory = new Historical<>(new Model());
    private boolean selected;
    private SwatchView activeSwatch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.bindView(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.view_fab);
        fabProgress = getActivity().findViewById(R.id.view_fab_progress);
        layersList.bind(modelHistory);
        modelHistory.observe().subscribe(render());

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
        modelHistory.observe().subscribe(model -> paletteView.bind(model.getPalette()));
        paletteView.setOnSwatchSelectedListener(this);
        addTools(view);
        withAllTools(tool -> tool.setToolSelectedListener(this));

        toolsViews.get(0).performClick();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (selected) {
            inflater.inflate(R.menu.menu_draw, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_undo:
                modelHistory.stepBackInTime();
                return true;
            case R.id.menu_redo:
                modelHistory.stepForwardInTime();
                return true;
        }
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        showMenuItemEnabled(menu, R.id.menu_undo, modelHistory.hasPast());
        showMenuItemEnabled(menu, R.id.menu_redo, modelHistory.hasFuture());
    }

    @Override
    public void onFragmentSelected() {
        selected = true;
        setFabState();
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentUnselected() {
        selected = false;
        invalidateOptionsMenu();
    }

    @Override
    public void onSwatchSelected(SwatchView swatchView) {
        this.activeSwatch = swatchView;
    }

    @Override
    public void onSwatchLongPressed(SwatchView swatch) {
        swatchToModify = swatch;
        new ColorChooserDialog.Builder((AppCompatActivity & ColorChooserDialog.ColorCallback) getContext(), R.string.change_color)
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if (swatchToModify != null && swatchToModify.getColor() != selectedColor) {
            modelHistory.progressTimeWithoutAnnouncing();
            modelHistory.present().getPalette().changeColor(swatchToModify.getIndex(), selectedColor);
            modelHistory.collapsePresentWithPastIfTheSame();
            modelHistory.announcePresent();
        }
    }

    @Override
    public void onGridTouchStarted() {
        modelHistory.progressTime();
    }

    @Override
    public void onGridTouch(int startColumn, int startRow, int column, int row) {
        drawingTool.drawOn(modelHistory.present().getSelectedLayer(), startColumn, startRow, column, row, activeSwatch.getColor());
        modelHistory.announcePresent();
    }

    @Override
    public void onGridTouchFinished() {
        drawingTool.finishStroke(modelHistory.present().getSelectedLayer());
        modelHistory.collapsePresentWithPastIfTheSame();
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
    public void drawingAlreadyExists(String name, Grid colorGrid, Throwable e) {
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
            fab.setOnClickListener(v -> layersList.addNewLayer());
            fab.setImageResource(R.drawable.ic_add_white_48px);
        } else {
            fab.setOnClickListener(v -> presenter.upload(ledGridView.getColorGrid()));
            fab.setImageResource(R.drawable.ic_publish_white_48px);
        }
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

    private void addTools(View view) {
        ViewGroup tools = (ViewGroup) view.findViewById(R.id.view_draw_tools);
        for (int i = 0; i < tools.getChildCount(); i++) {
            toolsViews.add((ToolView) tools.getChildAt(i));
        }
    }

    private Action1<? super Model> render() {
        return model -> {
            for (Layer layer : model.getLayers()) {
                blendUseCase.renderOn(layer, ledGridView);
            }
            ledGridView.invalidate();
            invalidateOptionsMenu();
        };
    }

    private void invalidateOptionsMenu() {
        getActivity().invalidateOptionsMenu();
    }

    private void showMenuItemEnabled(Menu menu, int menuId, boolean enabled) {
        MenuItem item = menu.findItem(menuId);
        if (item != null) {
            item.getIcon().setAlpha(enabled ? 255 : 125);
        }
    }
}
