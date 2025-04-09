package com.taotao.cloud.ccsr.common.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS(2000, "success"),
    REDIRECT(3002, "Request redirect"),
    GROUP_NOT_FOUND(4001, "Raft group not found"),
    INSTANCE_NOT_FOUND(4041, "Service instance not found"),
    DATA_NOT_EXIST(4042, "Data not exist"),
    NO_LEADER(4002, "No leader"),
    SYSTEM_ERROR(5000, "System error"),
    UNKNOWN_ERROR(5001, "Unknown error"),
    SERIALIZATION_ERROR(5002, "Serialization error"),
    REQUEST_TIMEOUT(5003, "Request timeout"),
    PARAM_INVALID(5004, "Param invalid"),
    CLIENT_ERROR(5005, "Client error"),
    ;

    private final int code;

    private final String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
