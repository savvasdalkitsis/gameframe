package com.savvasdalkitsis.gameframe.gameframe.api;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommandResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
}
