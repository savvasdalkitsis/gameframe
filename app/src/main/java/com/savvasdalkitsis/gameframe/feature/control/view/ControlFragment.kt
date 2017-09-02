package com.savvasdalkitsis.gameframe.feature.control.view

import android.os.Bundle
import android.support.annotation.ArrayRes
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SeekBar
import butterknife.OnClick
import butterknife.OnItemSelected
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.control.model.*
import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.infra.android.BaseFragment
import com.savvasdalkitsis.gameframe.infra.android.FragmentSelectedListener
import com.savvasdalkitsis.gameframe.infra.android.Snackbars
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector
import kotlinx.android.synthetic.main.fragment_control.*

class ControlFragment : BaseFragment(), ControlView, FragmentSelectedListener {

    private val presenter = PresenterInjector.controlPresenter()
    private lateinit var fab: FloatingActionButton

    override val layoutId: Int
        get() = R.layout.fragment_control

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fab = activity.findViewById(R.id.view_fab_control)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_brightness.setOnSeekBarChangeListener(BrightnessChangedListener())
        view_playback_mode.adapter = adapter(R.array.playback_mode)
        view_cycle_interval.adapter = adapter(R.array.cycle_interval)
        view_display_mode.adapter = adapter(R.array.display_mode)
        view_clock_face.adapter = adapter(R.array.clock_face)
        presenter.bindView(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadIpAddress()
    }

    override fun onFragmentSelected() {
        presenter.loadIpAddress()
        fab.visibility = View.VISIBLE
        fab.setOnClickListener { presenter.togglePower() }
    }

    override fun onFragmentUnselected() {
        fab.visibility = View.GONE
    }

    @OnClick(R.id.view_menu)
    fun menu() {
        presenter.menu()
    }

    @OnClick(R.id.view_next)
    fun next() {
        presenter.next()
    }

    @OnClick(R.id.view_control_setup)
    fun setup() {
        presenter.setup()
    }

    @OnClick(R.id.view_brightness_low)
    fun brightnessLow() {
        view_brightness.incrementProgressBy(-1)
    }

    @OnClick(R.id.view_brightness_high)
    fun brightnessHigh() {
        view_brightness.incrementProgressBy(1)
    }

    @OnItemSelected(R.id.view_playback_mode)
    fun playbackMode(position: Int) {
        presenter.changePlaybackMode(PlaybackMode.from(position))
    }

    @OnItemSelected(R.id.view_cycle_interval)
    fun cycleInterval(position: Int) {
        presenter.changeCycleInterval(CycleInterval.from(position))
    }

    @OnItemSelected(R.id.view_display_mode)
    fun displayMode(position: Int) {
        presenter.changeDisplayMode(DisplayMode.from(position))
    }

    @OnItemSelected(R.id.view_clock_face)
    fun clockFace(position: Int) {
        presenter.changeClockFace(ClockFace.from(position))
    }

    override fun operationSuccess() = Snackbars.success(activity.findViewById(R.id.view_coordinator), R.string.success).show()

    override fun operationFailure(e: Throwable) {
        Log.e(ControlFragment::class.java.name, "Operation failure", e)
        Snackbars.error(activity.findViewById(R.id.view_coordinator), R.string.operation_failed).show()
    }

    override fun ipAddressLoaded(ipAddress: IpAddress) {
        view_ip.text = getString(R.string.game_frame_ip, ipAddress.toString())
        view_control_error.visibility = View.GONE
        view_control_content.visibility = View.VISIBLE
    }

    override fun ipCouldNotBeFound(throwable: Throwable) {
        Log.e(ControlFragment::class.java.name, "Could not find ip", throwable)
        view_control_error.visibility = View.VISIBLE
        view_control_content.visibility = View.GONE
    }

    private fun adapter(@ArrayRes data: Int) =
            ArrayAdapter.createFromResource(context, data, android.R.layout.simple_spinner_item).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

    private inner class BrightnessChangedListener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, level: Int, b: Boolean) {
            presenter.changeBrightness(Brightness.from(level))
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}

        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }
}
