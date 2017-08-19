package com.savvasdalkitsis.gameframe.ip.usecase

import com.savvasdalkitsis.gameframe.ip.model.IpAddress

import rx.Observable
import rx.subjects.BehaviorSubject

class IpDiscoveryUseCase {

    private val subject = BehaviorSubject.create<IpAddress>()

    fun monitoredIps(): Observable<IpAddress> = subject

    fun emitMonitoredAddress(ip: IpAddress) {
        subject.onNext(ip)
    }
}
