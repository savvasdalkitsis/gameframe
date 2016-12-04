package com.savvasdalkitsis.gameframe.ip.usecase;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class IpDiscoveryUseCase {

    private BehaviorSubject<IpAddress> subject = BehaviorSubject.create();

    public Observable<IpAddress> monitoredIps() {
        return subject;
    }

    public void emitMonitoredAddress(IpAddress ip) {
        subject.onNext(ip);
    }
}
