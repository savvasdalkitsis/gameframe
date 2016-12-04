package com.savvasdalkitsis.gameframe.injector.ip.repository;

import com.savvasdalkitsis.gameframe.ip.repository.IpRepository;
import com.savvasdalkitsis.gameframe.ip.repository.PreferenceIpRepository;

import static com.savvasdalkitsis.gameframe.injector.rx.RxSharedPreferencesInjector.rxSharedPreferences;

public class IpRepositoryInjector {

    public static IpRepository ipRepository() {
        return new PreferenceIpRepository(rxSharedPreferences());
    }

}
