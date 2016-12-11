package com.savvasdalkitsis.gameframe.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewSwitcher;

import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.infra.navigation.Navigator;
import com.savvasdalkitsis.gameframe.infra.view.BaseActivity;
import com.savvasdalkitsis.gameframe.infra.view.FragmentSelectedListener;
import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector;
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.main.presenter.MainPresenter;

import butterknife.Bind;

@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainView {

    private final MainPresenter presenter = PresenterInjector.mainPresenter();
    private final Navigator navigator = NavigatorInjector.navigator();

    @Bind(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @Bind(R.id.fragment_switcher)
    ViewSwitcher fragmentSwitcher;
    @Bind(R.id.view_fab)
    FloatingActionButton fab;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);
        notifyFragmentSelected(R.id.fragment_manage);
        presenter.bindView(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setLogo(R.drawable.ic_logo);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadIpAddress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.control_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setup_ip :
                navigator.navigateToIpSetup();
                return true;
            case R.id.action_manage:
                fragmentSwitcher.setDisplayedChild(0);
                notifyFragmentSelected(R.id.fragment_manage);
                return true;
            case R.id.action_draw:
                fragmentSwitcher.setDisplayedChild(1);
                notifyFragmentSelected(R.id.fragment_draw);
                return true;
        }
        return false;
    }

    private void notifyFragmentSelected(int fragmentId) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(fragmentId);
        ((FragmentSelectedListener) fragment).onFragmentSelected();
    }

    @Override
    public void ipAddressLoaded(IpAddress ipAddress) {
        fab.animate().scaleX(1).scaleY(1).start();
    }

    @Override
    public void ipCouldNotBeFound(Throwable throwable) {
        fab.animate().scaleX(0).scaleY(0).start();
    }
}
