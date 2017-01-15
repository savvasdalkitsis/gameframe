package com.savvasdalkitsis.gameframe.control.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.control.presenter.ControlPresenter;
import com.savvasdalkitsis.gameframe.infra.navigation.Navigator;
import com.savvasdalkitsis.gameframe.infra.view.FragmentSelectedListener;
import com.savvasdalkitsis.gameframe.infra.view.Snackbars;
import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector;
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.model.Brightness;
import com.savvasdalkitsis.gameframe.model.ClockFace;
import com.savvasdalkitsis.gameframe.model.CycleInterval;
import com.savvasdalkitsis.gameframe.model.DisplayMode;
import com.savvasdalkitsis.gameframe.model.PlaybackMode;
import com.shazam.android.aspects.base.fragment.AspectSupportFragment;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;

@BindLayout(R.layout.fragment_control)
public class ControlFragment extends AspectSupportFragment implements ControlView, FragmentSelectedListener {

    private final Navigator navigator = NavigatorInjector.navigator();
    private final ControlPresenter presenter = PresenterInjector.controlPresenter();

    @Bind(R.id.view_brightness)
    SeekBar brightness;
    @Bind(R.id.view_playback_mode)
    Spinner playbackMode;
    @Bind(R.id.view_cycle_interval)
    Spinner cycleInterval;
    @Bind(R.id.view_display_mode)
    Spinner displayMode;
    @Bind(R.id.view_clock_face)
    Spinner clockFace;
    @Bind(R.id.view_ip)
    TextView ip;
    @Bind(R.id.view_control_content)
    View content;
    @Bind(R.id.view_control_error)
    View error;
    FloatingActionButton fab;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.view_fab);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        brightness.setOnSeekBarChangeListener(new BrightnessChangedListener());
        playbackMode.setAdapter(adapter(R.array.playback_mode));
        cycleInterval.setAdapter(adapter(R.array.cycle_interval));
        displayMode.setAdapter(adapter(R.array.display_mode));
        clockFace.setAdapter(adapter(R.array.clock_face));
        presenter.bindView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadIpAddress();
    }

    @Override
    public void onFragmentSelected() {
        presenter.loadIpAddress();
        fab.setImageResource(R.drawable.ic_power_settings_new_white_48px);
        fab.setOnClickListener(v -> presenter.togglePower());
    }

    @Override
    public void onFragmentUnselected() {
    }

    @OnClick(R.id.view_menu)
    public void menu() {
        presenter.menu();
    }

    @OnClick(R.id.view_next)
    public void next() {
        presenter.next();
    }

    @OnClick(R.id.view_control_setup)
    public void setup() {
        navigator.navigateToIpSetup();
    }

    @OnClick(R.id.view_brightness_low)
    public void brightnessLow() {
        brightness.incrementProgressBy(-1);
    }

    @OnClick(R.id.view_brightness_high)
    public void brightnessHigh() {
        brightness.incrementProgressBy(1);
    }

    @OnItemSelected(R.id.view_playback_mode)
    public void playbackMode(int position) {
        presenter.changePlaybackMode(PlaybackMode.from(position));
    }

    @OnItemSelected(R.id.view_cycle_interval)
    public void cycleInterval(int position) {
        presenter.changeCycleInterval(CycleInterval.from(position));
    }

    @OnItemSelected(R.id.view_display_mode)
    public void displayMode(int position) {
        presenter.changeDisplayMode(DisplayMode.from(position));
    }

    @OnItemSelected(R.id.view_clock_face)
    public void clockFace(int position) {
        presenter.changeClockFace(ClockFace.from(position));
    }

    @Override
    public void operationSuccess() {
        Snackbars.success(getActivity().findViewById(R.id.view_coordinator), R.string.success).show();
    }

    @Override
    public void operationFailure(Throwable e) {
        Log.e(ControlFragment.class.getName(), "Operation failure", e);
        Snackbars.error(getActivity().findViewById(R.id.view_coordinator), R.string.operation_failed).show();
    }

    @Override
    public void ipAddressLoaded(IpAddress ipAddress) {
        ip.setText(String.format("Game Frame IP: %s", ipAddress.toString()));
        error.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public void ipCouldNotBeFound(Throwable throwable) {
        Log.e(ControlFragment.class.getName(), "Could not find ip", throwable);
        error.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    @NonNull
    private ArrayAdapter<CharSequence> adapter(int data) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), data, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private class BrightnessChangedListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int level, boolean b) {
            presenter.changeBrightness(Brightness.from(level));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
