package com.taotao.cloud.sensitive.sensitive.sensitive.model.sensitive.system;


import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.SensitiveErrorSystemBuiltIn;

/**
 * 模拟错误的注解使用
 */
public class SensitiveErrorSystemBuiltInModel {

    @SensitiveErrorSystemBuiltIn
    private String errorField;

    public String getErrorField() {
        return errorField;
    }

    public void setErrorField(String errorField) {
        this.errorField = errorField;
    }

    @Override
    public String toString() {
        return "SensitiveErrorSystemBuiltInModel{" +
                "errorField='" + errorField + '\'' +
                '}';
    }

}
