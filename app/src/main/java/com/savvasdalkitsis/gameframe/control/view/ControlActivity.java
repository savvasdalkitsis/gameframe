package com.savvasdalkitsis.gameframe.control.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.control.presenter.ControlPresenter;
import com.savvasdalkitsis.gameframe.infra.navigation.Navigator;
import com.savvasdalkitsis.gameframe.infra.view.BaseActivity;
import com.savvasdalkitsis.gameframe.infra.view.Snackbars;
import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector;
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector;
import com.savvasdalkitsis.gameframe.model.Brightness;
import com.savvasdalkitsis.gameframe.model.ClockFace;
import com.savvasdalkitsis.gameframe.model.CycleInterval;
import com.savvasdalkitsis.gameframe.model.DisplayMode;
import com.savvasdalkitsis.gameframe.model.PlaybackMode;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemSelected;

@BindLayout(R.layout.activity_control)
public class ControlActivity extends BaseActivity implements ControlView {

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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        brightness.setOnSeekBarChangeListener(new BrightnessChangedListener());
        playbackMode.setAdapter(adapter(R.array.playback_mode));
        cycleInterval.setAdapter(adapter(R.array.cycle_interval));
        displayMode.setAdapter(adapter(R.array.display_mode));
        clockFace.setAdapter(adapter(R.array.clock_face));

        presenter.bindView(this);
    }

    @OnClick(R.id.view_power)
    public void power() {
        presenter.togglePower();
    }

    @OnClick(R.id.view_menu)
    public void menu() {
        presenter.menu();
    }

    @OnClick(R.id.view_next)
    public void next() {
        presenter.next();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setup_ip :
                navigator.navigateToIpSetup();
                return true;
        }
        return false;
    }

    @Override
    public void operationSuccess() {
        Snackbars.success(findViewById(android.R.id.content), R.string.success).show();
    }

    @Override
    public void operationFailure(Throwable e) {
        Snackbars.error(findViewById(android.R.id.content), R.string.operation_failed).show();
    }

    @NonNull
    private ArrayAdapter<CharSequence> adapter(int data) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, data, android.R.layout.simple_spinner_item);
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
