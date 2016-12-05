package com.savvasdalkitsis.gameframe.main.view;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;

public interface MainView {

    void ipAddressLoaded(IpAddress ipAddress);
    void ipCouldNotBeFound(Throwable throwable);
}
