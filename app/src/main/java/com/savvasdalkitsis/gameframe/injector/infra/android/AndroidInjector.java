package com.savvasdalkitsis.gameframe.injector.infra.android;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.savvasdalkitsis.gameframe.injector.ApplicationInjector;

public class AndroidInjector {

    public static WifiManager wifiManager() {
        return (WifiManager) ApplicationInjector.application().getSystemService(Context.WIFI_SERVICE);
    }
}
