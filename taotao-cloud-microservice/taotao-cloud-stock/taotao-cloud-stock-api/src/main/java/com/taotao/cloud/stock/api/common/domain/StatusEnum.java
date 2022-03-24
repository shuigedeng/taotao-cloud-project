package com.taotao.cloud.stock.api.common.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 用户状态枚举
 **/
public enum StatusEnum implements ValueObject<StatusEnum> {

    /**
     * 有效
     */
    ENABLE("0","有效"),

    /**
     * 禁用
     */
    DISABLE("1","禁用");


    private String value;

    private String label;

    StatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据匹配value的值获取Label
     *
     * @param value
     * @return
     */
    public static String getLabelByValue(String value){
        if(StringUtils.isBlank(value)) {
            return "";
        }
        for (StatusEnum s : StatusEnum.values()) {
            if(value.equals(s.getValue())){
                return s.getLabel();
            }
        }
        return "";
    }

    /**
     * 获取StatusEnum
     *
     * @param value
     * @return
     */
    public static StatusEnum getStatusEnum(String value){
        if(StringUtils.isBlank(value)) {
            return null;
        }
        for (StatusEnum s : StatusEnum.values()) {
            if(value.equals(s.getValue())){
                return s;
            }
        }
        return null;
    }

    @Override
    public boolean sameValueAs(final StatusEnum other) {
        return this.equals(other);
    }
}
