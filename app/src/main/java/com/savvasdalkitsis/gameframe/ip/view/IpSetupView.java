package com.savvasdalkitsis.gameframe.ip.view;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;

public interface IpSetupView {

    void displayIpAddress(IpAddress ipAddress);

    void errorDiscoveringIpAddress(Throwable throwable);

    void addressSaved(IpAddress ipAddress);

    void displayDiscovering();

    void ipAddressDiscovered(IpAddress ipAddress);

    void tryingAddress(IpAddress ipAddress);

    void displayIdleView();
}
