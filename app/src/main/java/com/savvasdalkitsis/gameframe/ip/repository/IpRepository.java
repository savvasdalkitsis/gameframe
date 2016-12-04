package com.savvasdalkitsis.gameframe.ip.repository;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;

import rx.Observable;

public interface IpRepository {

    Observable<IpAddress> getIpAddress();

    void saveIpAddress(IpAddress ipAddress);
}
