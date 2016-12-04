package com.savvasdalkitsis.gameframe.control.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.control.presenter.ControlPresenter;
import com.savvasdalkitsis.gameframe.infra.navigation.Navigator;
import com.savvasdalkitsis.gameframe.infra.view.Snackbars;
import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector;
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector;
import com.shazam.android.aspects.base.activity.AspectAppCompatActivity;

import butterknife.OnClick;

@BindLayout(R.layout.activity_main)
public class ControlActivity extends AspectAppCompatActivity implements ControlView {

    private final Navigator navigator = NavigatorInjector.navigator();
    private final ControlPresenter presenter = PresenterInjector.controlPresenter();

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
}
