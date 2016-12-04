package com.savvasdalkitsis.gameframe.ip.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.infra.view.Snackbars;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.presenter.IpSetupPresenter;
import com.shazam.android.aspects.base.activity.AspectAppCompatActivity;

import butterknife.Bind;
import butterknife.OnClick;

import static com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector.ipSetupPresenter;
import static com.savvasdalkitsis.gameframe.ip.model.IpAddress.Builder.ipAddress;

@BindLayout(R.layout.activity_ip_setup)
public class IpSetupActivity extends AspectAppCompatActivity implements IpSetupView {

    private final IpSetupPresenter ipSetupPresenter = ipSetupPresenter();

    @Bind(R.id.view_ip_text_view)
    IpTextView ipTextView;
    @Bind(R.id.view_setup)
    Button setup;
    @Bind(R.id.view_setup_content)
    View content;
    @Bind(R.id.view_setup_progress)
    View progress;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ipTextView.setOnIpChangedListener(ipAddress -> setup.setEnabled(ipAddress.isValid()));
        ipSetupPresenter.bindView(this);
    }

    @Override
    public void displayIpAddress(IpAddress ipAddress) {
        progress.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        ipTextView.bind(ipAddress);
    }

    @OnClick(R.id.view_setup)
    public void setup() {
        ipSetupPresenter.setup(ipTextView.getIpAddress());
    }

    @OnClick(R.id.view_discover)
    public void discover() {
        ipSetupPresenter.discoverIp();
    }

    @Override
    public void errorLoadingIpAddress(Throwable throwable) {
        Log.e(IpSetupActivity.class.getName(), "Could not load ip address", throwable);
        displayIpAddress(ipAddress().build());
        Snackbars.error(findViewById(android.R.id.content), R.string.operation_failed).show();
    }

    @Override
    public void addressSaved(IpAddress ipAddress) {
        finish();
    }

    @Override
    public void displayLoading() {
        progress.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    @Override
    public void ipAddressDiscovered(IpAddress ipAddress) {
        displayIpAddress(ipAddress);
        Snackbars.success(findViewById(android.R.id.content), R.string.game_frame_ip_found).show();
    }
}
