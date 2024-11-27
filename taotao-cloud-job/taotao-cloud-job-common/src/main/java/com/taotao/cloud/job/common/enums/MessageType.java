package com.taotao.cloud.job.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {
    JOB_CREAT(0),
    JOB_UPDATE(1),
    JOB_DELETE(2);
    private final int v;

    public static MessageType of(int v) {
        for (MessageType is : values()) {
            if (v == is.v) {
                return is;
            }
        }
        throw new IllegalArgumentException("MessageType has no item for value " + v);
    }
}
