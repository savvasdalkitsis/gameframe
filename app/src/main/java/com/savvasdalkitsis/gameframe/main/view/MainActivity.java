package com.savvasdalkitsis.gameframe.main.view;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewSwitcher;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
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
public class MainActivity extends BaseActivity implements MainView, ColorChooserDialog.ColorCallback {

    private final MainPresenter presenter = PresenterInjector.mainPresenter();
    private final Navigator navigator = NavigatorInjector.navigator();

    @Bind(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @Bind(R.id.fragment_switcher)
    ViewSwitcher fragmentSwitcher;
    @Bind(R.id.view_fab_container)
    View fab;

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
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setup_ip:
                navigator.navigateToIpSetup();
                return true;
            case R.id.action_manage:
                fragmentSwitcher.setDisplayedChild(0);
                notifyAllFragmentsUnselected();
                notifyFragmentSelected(R.id.fragment_manage);
                return true;
            case R.id.action_draw:
                fragmentSwitcher.setDisplayedChild(1);
                notifyFragmentSelected(R.id.fragment_draw);
                return true;
        }
        return false;
    }

    private void notifyAllFragmentsUnselected() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            ((FragmentSelectedListener) fragment).onFragmentUnselected();
        }
    }

    private void notifyFragmentSelected(int fragmentId) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(fragmentId);
        ((FragmentSelectedListener) fragment).onFragmentSelected();
    }

    @Override
    public void ipAddressLoaded(IpAddress ipAddress) {
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void ipCouldNotBeFound(Throwable throwable) {
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        ((ColorChooserDialog.ColorCallback) getSupportFragmentManager().findFragmentById(R.id.fragment_draw)).onColorSelection(dialog, selectedColor);
    }
}
