package com.savvasdalkitsis.gameframe.ip.view;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;

public interface IpSetupView {

    void displayIpAddress(IpAddress ipAddress);

    void errorLoadingIpAddress(Throwable throwable);

    void addressSaved(IpAddress ipAddress);

    void displayLoading();

    void ipAddressDiscovered(IpAddress ipAddress);

    void tryingAddress(IpAddress ipAddress);
}
