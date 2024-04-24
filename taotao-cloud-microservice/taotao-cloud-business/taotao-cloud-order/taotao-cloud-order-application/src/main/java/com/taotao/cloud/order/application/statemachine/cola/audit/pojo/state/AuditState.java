package com.taotao.cloud.order.application.statemachine.cola.audit.pojo.state;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @date 2023/7/12 16:03
 */
public enum AuditState {

    /**
     * 已申请
     */
    APPLY("APPLY", "已申请"),
    /**
     * 爸爸同意
     */
    DAD_PASS("DAD_PASS", "爸爸同意"),
    /**
     * 妈妈同意
     */
    MOM_PASS("MOM_PASS", "妈妈同意"),
    /**
     * 爸爸不同意
     */
    DAD_REJ("DAD_REJ", "爸爸不同意"),
    /**
     * 妈妈不同意
     */
    MOM_REJ("MOM_REJ", "妈妈不同意"),
    /**
     * 已完成
     */
    DONE("DONE", "已完成");

    private static final Map<String, AuditState> CODE_MAP = new ConcurrentHashMap<>();

    static {
        for (AuditState auditState : EnumSet.allOf(AuditState.class)) {
            CODE_MAP.put(auditState.getCode(), auditState);
        }
    }

    public static AuditState getEnumsByCode(String code) {
        return CODE_MAP.get(code);
    }

    /**
     * code
     */
    private String code;

    /**
     * desc
     */
    private String desc;

    AuditState(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
