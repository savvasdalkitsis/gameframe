package com.savvasdalkitsis.gameframe.infra.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.savvasdalkitsis.gameframe.GameFrameApplication;
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider;
import com.savvasdalkitsis.gameframe.ip.view.IpSetupActivity;

public class ApplicationNavigator implements Navigator {

    private final GameFrameApplication application;
    private TopActivityProvider topActivityProvider;

    public ApplicationNavigator(TopActivityProvider topActivityProvider, GameFrameApplication application) {
        this.topActivityProvider = topActivityProvider;
        this.application = application;
    }

    @Override
    public void navigateToIpSetup() {
        start(createIntent(IpSetupActivity.class));
    }

    private void start(Intent intent) {
        getContext().startActivity(intent);
    }

    private Intent createIntent(Class<?> cls) {
        Context context = getContext();
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    private Context getContext() {
        Activity topActivity = topActivityProvider.getTopActivity();
        if (topActivity != null) {
            return topActivity;
        }
        return application;
    }
}
