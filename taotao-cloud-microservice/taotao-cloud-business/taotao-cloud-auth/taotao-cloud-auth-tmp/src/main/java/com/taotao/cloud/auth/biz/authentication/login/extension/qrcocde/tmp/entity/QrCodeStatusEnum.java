package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.entity;

public enum QrCodeStatusEnum {

    WAITING(0, "待扫描"),

    SCANNED(1, "待确认"),

    CONFIRMED(2, "已确认"),

    INVALID(-1, "二维码已失效");

    private final Integer status;

    private final String message;

    QrCodeStatusEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static QrCodeStatusEnum parse(Integer status) {
        for (QrCodeStatusEnum entityTypeEnum : QrCodeStatusEnum.values()) {
            if (entityTypeEnum.status.equals(status)) {
                return entityTypeEnum;
            }
        }
        return INVALID;
    }
}
