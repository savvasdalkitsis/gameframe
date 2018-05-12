/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
package com.savvasdalkitsis.gameframe.feature.workspace.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.internal.NavigationMenu
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.savvasdalkitsis.gameframe.feature.history.usecase.HistoryUseCase
import com.savvasdalkitsis.gameframe.feature.message.Snackbars
import com.savvasdalkitsis.gameframe.feature.workspace.R
import com.savvasdalkitsis.gameframe.feature.workspace.element.grid.model.Grid
import com.savvasdalkitsis.gameframe.feature.workspace.element.layer.model.Layer
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.model.Palette
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.view.AddNewPaletteSelectedListener
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.view.AddPaletteView
import com.savvasdalkitsis.gameframe.feature.workspace.element.palette.view.PaletteView
import com.savvasdalkitsis.gameframe.feature.workspace.element.swatch.view.SwatchSelectedListener
import com.savvasdalkitsis.gameframe.feature.workspace.element.swatch.view.SwatchView
import com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model.Tools
import com.savvasdalkitsis.gameframe.feature.workspace.element.tools.view.ToolSelectedListener
import com.savvasdalkitsis.gameframe.feature.workspace.injector.WorkspaceInjector.workspacePresenter
import com.savvasdalkitsis.gameframe.feature.workspace.model.WorkspaceModel
import com.savvasdalkitsis.gameframe.feature.workspace.presenter.WorkspacePresenter
import com.savvasdalkitsis.gameframe.infra.android.BaseFragment
import com.savvasdalkitsis.gameframe.infra.android.FragmentSelectedListener
import com.savvasdalkitsis.gameframe.kotlin.Action
import com.savvasdalkitsis.gameframe.kotlin.TypeAction
import com.savvasdalkitsis.gameframe.kotlin.gone
import com.savvasdalkitsis.gameframe.kotlin.visible
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.github.yavski.fabspeeddial.CustomFabSpeedDial
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter

@SuppressLint("RtlHardcoded")
private const val GRAVITY_PALETTES = Gravity.LEFT
@SuppressLint("RtlHardcoded")
private const val GRAVITY_LAYERS = Gravity.RIGHT

class WorkspaceFragment : BaseFragment<WorkspaceView<Menu>, WorkspacePresenter<Menu, View>>(),
        FragmentSelectedListener,
        SwatchSelectedListener, WorkspaceView<Menu>,
        ColorChooserDialog.ColorCallback, ToolSelectedListener {

    private lateinit var fab: CustomFabSpeedDial
    private lateinit var drawer: DrawerLayout
    override val presenter = workspacePresenter()
    override val view = this
    private var swatchToModify: SwatchView? = null
    private var selected: Boolean = false
    private var activeSwatch: SwatchView? = null

    override val layoutId: Int
        get() = R.layout.fragment_workspace

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fab = activity!!.findViewById(R.id.view_fab_workspace)
        view_draw_tools.setOnToolSelectedListener(this)
        view_draw_tools_current.bind(Tools.defaultTool())

        view_draw_sliding_up_panel.addPanelSlideListener(object : SlidingUpPanelLayout.SimplePanelSlideListener() {
            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                val scale = if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) 0f else 1f
                fab.visible()
                fab.animate().scaleY(scale).scaleX(scale)
                        .withEndAction { if (scale == 0f) fab.gone() }
                        .start()
            }
        })
        drawer.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                setFabState()
            }

            override fun onDrawerClosed(drawerView: View) {
                setFabState()
            }
        })

        view_draw_open_layers.setOnClickListener {
            drawer.openDrawer(GRAVITY_LAYERS)
        }
        view_draw_open_palette.setOnClickListener {
            drawer.openDrawer(GRAVITY_PALETTES)
        }
        listOf(view_draw_tools_change, view_draw_tools_current).forEach {
            it.setOnClickListener {
                view_draw_sliding_up_panel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }
        }
        listOf(view_draw_add_palette, view_draw_add_palette_title).forEach {
            it.setOnClickListener {
                addNewPalette()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawer = view.findViewById(R.id.view_draw_drawer)
        view_draw_led_grid_view.setOnGridTouchedListener(presenter)
    }

    override fun wifiNotEnabledError(e: Throwable) {
        Snackbars.actionError(coordinator(), R.string.wifi_not_enabled, R.string.enable) {
            presenter.enableWifi()
        }
    }

    override fun onResume() {
        super.onResume()
        setFabState()
        presenter.bindGrid(view_draw_led_grid_view)
    }

    override fun onPause() {
        super.onPause()
        presenter.paused()
    }

    override fun bindPalette(selectedPalette: Palette) {
        val paletteView = drawer.findViewById<PaletteView>(R.id.view_draw_palette)
        paletteView.bind(selectedPalette)
        paletteView.setOnSwatchSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        if (selected) {
            inflater.inflate(R.menu.menu_workspace, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_borders -> {
            presenter.selectedOptionBorders()
            true
        }
        R.id.menu_undo -> {
            presenter.selectedOptionUndo()
            true
        }
        R.id.menu_redo -> {
            presenter.selectedOptionRedo()
            true
        }
        else -> false
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        presenter.prepareOptions(menu)
    }

    override fun displayLayoutBordersEnabled(options: Menu) {
        options.findItem(R.id.menu_borders)?.setIcon(R.drawable.ic_border_outer_white_48px)
    }

    override fun displayLayoutBordersDisabled(options: Menu) {
        options.findItem(R.id.menu_borders)?.setIcon(R.drawable.ic_border_clear_white_48px)
    }

    override fun enableUndo(options: Menu) {
        options[R.id.menu_undo]?.alpha(255)
    }

    override fun disableUndo(options: Menu) {
        options[R.id.menu_undo]?.alpha(125)
    }

    override fun enableRedo(options: Menu) {
        options[R.id.menu_redo]?.alpha(255)
    }

    override fun disableRedo(options: Menu) {
        options[R.id.menu_redo]?.alpha(125)
    }

    private operator fun Menu.get(id: Int): MenuItem? = findItem(id)

    private fun MenuItem.alpha(alpha: Int) {
        icon.alpha = alpha
    }

    override fun onFragmentSelected() {
        selected = true
        fab.visible()
        invalidateOptionsMenu()
    }

    override fun onFragmentUnselected() {
        selected = false
        fab.gone()
        invalidateOptionsMenu()
    }

    override fun observe(history: HistoryUseCase<WorkspaceModel>) {
        view_draw_layers.bind(history) { drawer.closeDrawer(GRAVITY_LAYERS) }
        view_draw_palettes.bind(history) { drawer.closeDrawer(GRAVITY_PALETTES) }
    }

    override fun onSwatchSelected(swatchView: SwatchView) {
        this.activeSwatch = swatchView
    }

    override fun onSwatchLongPressed(swatch: SwatchView) {
        swatchToModify = swatch
        ColorChooserDialog.Builder(context!!, R.string.change_color)
                .show(activity)
    }

    override fun onColorSelection(dialog: ColorChooserDialog, @ColorInt selectedColor: Int) {
        swatchToModify?.let {
            presenter.changeColor(it.color, selectedColor, it.index)
        }
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {}

    override fun drawLayer(layer: Layer, startColumn: Int, startRow: Int, column: Int, row: Int) {
        activeSwatch?.let {
            view_draw_tools_current.drawingTool?.drawOn(layer, startColumn, startRow, column, row, it.color)
        }
    }

    override fun finishStroke(layer: Layer) {
        view_draw_tools_current.drawingTool?.finishStroke(layer)
    }

    @SuppressLint("SetTextI18n")
    override fun onToolSelected(tool: Tools) {
        view_draw_tools_current.bind(tool)
        view_draw_sliding_up_panel.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        view_draw_tools_change.text = "${tool.label}:"
    }

    override fun askForFileName(positiveText: Int, nameEntered: TypeAction<String>, cancelAction: Action) {
        MaterialDialog.Builder(activity!!)
                .input(R.string.name_of_drawing, 0, false) { _, input -> nameEntered(input.toString()) }
                .title(R.string.enter_name_for_drawing)
                .positiveText(positiveText)
                .negativeText(android.R.string.cancel)
                .onNegative { _, _ -> cancelAction() }
                .canceledOnTouchOutside(true)
                .cancelListener { cancelAction() }
                .build()
                .show()
    }

    override fun displayProgress() {
        fab.startProgress()
    }

    override fun stopProgress() {
        fab.stopProgress(R.drawable.ic_import_export_white_48px)

    }

    override fun drawingAlreadyExists(name: String, colorGrid: Grid, e: Throwable) {
        Log.e(WorkspacePresenter::class.java.name, "Drawing already exists", e)
        Snackbars.actionError(coordinator(), R.string.already_exists, R.string.replace,
                { presenter.replaceDrawing(name, colorGrid) })
        stopProgress()
    }

    override fun displaySelectedPalette(paletteName: String) {
        view_draw_palette_name.text = paletteName
    }

    fun addNewPalette() {
        AddPaletteView.show(context!!, drawer, object : AddNewPaletteSelectedListener {
            override fun onAddNewPalletSelected(palette: Palette) {
                view_draw_palettes.addNewPalette(palette)
            }
        })
    }

    private fun setFabState() = with(fab) {
        when {
            drawer.isDrawerOpen(GRAVITY_LAYERS) -> {
                setMenuListener(addNewLayerOperation())
                setImageResource(R.drawable.ic_add_white_48px)
            }
            drawer.isDrawerOpen(GRAVITY_PALETTES) -> {
                setMenuListener(addNewPaletteOperation())
                setImageResource(R.drawable.ic_add_white_48px)
            }
            else -> {
                setMenuListener(standardOperation())
                setImageResource(R.drawable.ic_import_export_white_48px)
            }
        }
    }

    private fun addNewPaletteOperation() = object : SimpleMenuListenerAdapter() {
        override fun onPrepareMenu(navigationMenu: NavigationMenu?): Boolean {
            addNewPalette()
            return false
        }
    }

    private fun addNewLayerOperation() = object : SimpleMenuListenerAdapter() {
        override fun onPrepareMenu(navigationMenu: NavigationMenu?): Boolean {
            view_draw_layers.addNewLayer()
            return false
        }
    }

    private fun standardOperation() = object : SimpleMenuListenerAdapter() {
        override fun onMenuItemSelected(menuItem: MenuItem) = when (menuItem.itemId) {
            R.id.operation_upload -> {
                presenter.upload(view_draw_led_grid_view.colorGrid)
                true
            }
            R.id.operation_save -> {
                presenter.saveWorkspace()
                true
            }
            R.id.operation_open -> {
                presenter.loadProject()
                true
            }
            R.id.operation_delete -> {
                presenter.deleteProjects()
                true
            }
            R.id.operation_new -> {
                presenter.createNewProject()
                true
            }
            R.id.operation_export -> {
                presenter.exportImage(view_draw_led_grid_view)
                true
            }
            else -> false
        }
    }

    override fun askForApprovalToDismissChanges() {
        MaterialDialog.Builder(context!!)
                .title(R.string.dismiss_changes_question)
                .content(R.string.unsaved_changes)
                .positiveText(R.string.dismiss)
                .negativeText(R.string.do_not_dismiss)
                .onPositive { _, _ -> presenter.createNewProject(true) }
                .show()
    }

    override fun askForProjectToLoad(projectNames: List<String>) {
        MaterialDialog.Builder(context!!)
                .title(R.string.load_project)
                .items(projectNames)
                .itemsCallbackSingleChoice(-1) { _, _, which, _ ->
                    presenter.loadProject(projectNames[which])
                    true
                }
                .show()
    }

    override fun displayUnsupportedVersion() {
        MaterialDialog.Builder(context!!)
                .title(R.string.unsupported_version)
                .content(R.string.unsupported_version_description)
                .positiveText(R.string.play_store)
                .negativeText(R.string.cancel)
                .onPositive { _, _ -> presenter.takeUserToPlayStore() }
                .show()
    }

    override fun askForProjectsToDelete(projectNames: List<String>) {
        MaterialDialog.Builder(context!!)
                .title(R.string.delete_projects)
                .items(projectNames)
                .itemsCallbackMultiChoice(null) { _, which, _ ->
                    presenter.deleteProjects(which.map { projectNames[it] })
                    true
                }
                .positiveText(R.string.delete)
                .show()
    }

    override fun displayNoSavedProjectsExist() {
        Snackbars.error(coordinator(), R.string.no_saved_projects)
        stopProgress()
    }

    override fun displayProjectName(name: String) {
        view_draw_project_name.text = name
    }

    override fun displaySelectedLayerName(layerName: String) {
        view_draw_layer_name.text = layerName
    }

    private fun coordinator() = activity!!.findViewById<View>(R.id.view_coordinator)

    override fun displayBoundaries(col: Int, row: Int) {
        view_draw_led_grid_view.displayBoundaries(col, row)
    }

    override fun clearBoundaries() {
        view_draw_led_grid_view.clearBoundaries()
    }

    override fun rendered() {
        view_draw_led_grid_view.invalidate()
        invalidateOptionsMenu()
    }

    private fun invalidateOptionsMenu() {
        activity!!.invalidateOptionsMenu()
    }

    override fun showSuccess() {
        Snackbars.success(coordinator(), R.string.success)
        stopProgress()
    }

    override fun operationFailed(e: Throwable) {
        Log.e(WorkspacePresenter::class.java.name, "Workspace operation failed", e)
        Snackbars.error(coordinator(), R.string.operation_failed)
        stopProgress()
    }
}
