package com.savvasdalkitsis.gameframe.feature.ip.usecase

import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class IpDiscoveryUseCase {

    private val processor = BehaviorProcessor.create<IpAddress>()

    fun monitoredIps(): Flowable<IpAddress> = processor

    fun emitMonitoredAddress(ip: IpAddress) {
        processor.onNext(ip)
    }
}
