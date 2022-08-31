package com.taotao.cloud.office.easypoi.test.en;

/**
 * @author by jueyue on 18-4-2.
 */
public enum StatusEnum {

    Init(0, "初始化"),
    Ready(1, "正常"),
    ChangePassword(2, "需要修改密码"),
    Frozen(4, "冻结"),
    Disabled(64, "禁用");
    private final Integer _code;
    private final String _message;

    StatusEnum(Integer code, String message) {
        _code = code;
        _message = message;
    }

    public Integer getValue() {
        return _code;
    }

    public String getMessage() {
        return _message;
    }

    public static StatusEnum getByMessage(String message){
        StatusEnum[] arr =  StatusEnum.values();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]._message.equals(message)){
                return arr[i];
            }
        }
        return null;
    }
}
