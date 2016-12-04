package com.savvasdalkitsis.gameframe.control.view;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;

public interface ControlView {

    void operationSuccess();

    void operationFailure(Throwable e);

    void ipAddressLoaded(IpAddress ipAddress);

    void ipCouldNotBeFound(Throwable throwable);
}
