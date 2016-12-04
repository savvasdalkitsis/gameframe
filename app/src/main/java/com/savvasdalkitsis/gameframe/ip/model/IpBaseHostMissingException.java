package com.savvasdalkitsis.gameframe.ip.model;

import java.io.IOException;

public class IpBaseHostMissingException extends IOException {

    public IpBaseHostMissingException(String message) {
        super(message);
    }

    public IpBaseHostMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
