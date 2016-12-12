package com.savvasdalkitsis.gameframe.ip.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import com.savvasdalkitsis.butterknifeaspects.aspects.BindLayout;
import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.infra.view.BaseActivity;
import com.savvasdalkitsis.gameframe.infra.view.Snackbars;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.presenter.IpSetupPresenter;

import butterknife.Bind;
import butterknife.OnClick;

import static com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector.ipSetupPresenter;
import static com.savvasdalkitsis.gameframe.ip.model.IpAddress.Builder.ipAddress;

@BindLayout(R.layout.activity_ip_setup)
public class IpSetupActivity extends BaseActivity implements IpSetupView {

    private final IpSetupPresenter presenter = ipSetupPresenter();

    @Bind(R.id.view_ip_text_view)
    IpTextView ipTextView;
    @Bind(R.id.view_setup_content)
    View content;
    @Bind(R.id.view_setup_title)
    TextView title;
    @Bind(R.id.view_discover)
    View discover;
    @Bind(R.id.view_discover_title)
    View discoverTitle;
    @Bind(R.id.view_setup)
    View fab;
    @Bind(R.id.view_cancel_discover)
    View cancelDiscover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ipTextView.setOnIpChangedListener(ipAddress -> {
            if (!ipTextView.isEnabled()) {
                return;
            }
            showFab(fab, ipAddress.isValid());
        });
        presenter.bindView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unbind();
    }

    @Override
    public void displayIpAddress(IpAddress ipAddress) {
        ipTextView.bind(ipAddress);
    }

    @OnClick(R.id.view_setup)
    public void setup() {
        presenter.setup(ipTextView.getIpAddress());
    }

    @OnClick(R.id.view_cancel_discover)
    public void cancelDiscover() {
        presenter.cancelDiscover();
    }

    @OnClick(R.id.view_discover)
    public void discover() {
        presenter.discoverIp();
    }

    @Override
    public void errorDiscoveringIpAddress(Throwable throwable) {
        Log.e(IpSetupActivity.class.getName(), "Could not load ip address", throwable);
        Snackbars.error(content, R.string.operation_failed).show();
    }

    @Override
    public void addressSaved(IpAddress ipAddress) {
        finish();
    }

    @Override
    public void displayDiscovering() {
        title.setText(R.string.trying_to_find_game_frame);
        scale(discover, 0);
        alpha(discoverTitle, 0);
        ipTextView.setEnabled(false);
        animate(ipTextView)
                .translationY(getResources().getDimensionPixelSize(R.dimen.ip_view_offset))
                .start();

        showFab(fab, false);
        showFab(cancelDiscover, true);
    }

    @Override
    public void ipAddressDiscovered(IpAddress ipAddress) {
        Snackbars.success(content, R.string.game_frame_ip_found).show();
    }

    @Override
    public void tryingAddress(IpAddress ipAddress) {
        ipTextView.bind(ipAddress);
    }

    @Override
    public void displayIdleView() {
        title.setText(R.string.enter_ip_address);
        scale(discover, 1);
        alpha(discoverTitle, 1);
        ipTextView.setEnabled(true);
        animate(ipTextView)
                .translationY(0)
                .start();
        showFab(fab, true);
        showFab(cancelDiscover, false);
    }

    private void scale(View view, float value) {
        animate(view).scaleX(value).scaleY(value).start();
    }

    private void alpha(View view, float value) {
        animate(view).alpha(value).start();
    }

    private ViewPropertyAnimator animate(View view) {
        view.clearAnimation();
        return view.animate().setDuration(200);
    }

    private void showFab(View fab, boolean show) {
        float scale = show ? 1 : 0;
        float rotation = show ? 0 : 45;
        fab.setEnabled(show);
        fab.setClickable(show);
        animate(fab)
                .rotation(rotation)
                .scaleX(scale)
                .scaleY(scale)
                .start();
    }
}
