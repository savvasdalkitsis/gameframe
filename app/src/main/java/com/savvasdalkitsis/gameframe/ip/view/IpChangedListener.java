package com.savvasdalkitsis.gameframe.ip.view;

import com.savvasdalkitsis.gameframe.ip.model.IpAddress;

public interface IpChangedListener {

    void onIpChangedListener(IpAddress ipAddress);

    IpChangedListener NO_OP = ipAddress -> {};
}
