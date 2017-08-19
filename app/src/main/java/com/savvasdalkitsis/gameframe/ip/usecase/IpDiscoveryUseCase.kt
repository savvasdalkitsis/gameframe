package com.savvasdalkitsis.gameframe.ip.usecase

import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class IpDiscoveryUseCase {

    private val processor = BehaviorProcessor.create<IpAddress>()

    fun monitoredIps(): Flowable<IpAddress> = processor

    fun emitMonitoredAddress(ip: IpAddress) {
        processor.onNext(ip)
    }
}
