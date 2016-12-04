package com.savvasdalkitsis.gameframe.ip.repository;

import com.savvasdalkitsis.gameframe.infra.preferences.RxSharedPreferences;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;
import com.savvasdalkitsis.gameframe.ip.model.IpBaseHostMissingException;

import rx.Observable;

public class PreferenceIpRepository implements IpRepository {

    private static final String PREF_IP_BASE = "pref_ip_base";
    private RxSharedPreferences preferences;

    public PreferenceIpRepository(RxSharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Observable<IpAddress> getIpAddress() {
        return preferences.getString(PREF_IP_BASE)
                .map(IpAddress::parse)
                .flatMap(ipAddress -> {
                    if (ipAddress.isValid()) {
                        return Observable.just(ipAddress);
                    } else {
                        return Observable.error(new IllegalStateException("saved ip address was not valid: " + ipAddress.toString()));
                    }
                })
                .onErrorResumeNext(error -> Observable.error(new IpBaseHostMissingException("Error trying to read ip address from repository", error)));
    }

    @Override
    public void saveIpAddress(IpAddress ipAddress) {
        preferences.setString(PREF_IP_BASE, ipAddress.toString());
    }

}
