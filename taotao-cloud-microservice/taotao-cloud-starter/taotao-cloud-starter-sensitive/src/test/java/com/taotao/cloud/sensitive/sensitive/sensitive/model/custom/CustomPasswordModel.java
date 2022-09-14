package com.taotao.cloud.sensitive.sensitive.sensitive.model.custom;


import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.custom.SensitiveCustomPasswordCondition;
import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.custom.SensitiveCustomPasswordStrategy;
import com.taotao.cloud.sensitive.sensitive.sensitive.annotation.strategy.SensitiveStrategyPassword;

public class CustomPasswordModel {

    @SensitiveCustomPasswordCondition
    @SensitiveCustomPasswordStrategy
    private String password;

    @SensitiveCustomPasswordCondition
    @SensitiveStrategyPassword
    private String fooPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFooPassword() {
        return fooPassword;
    }

    public void setFooPassword(String fooPassword) {
        this.fooPassword = fooPassword;
    }

    @Override
    public String toString() {
        return "CustomPasswordModel{" +
                "password='" + password + '\'' +
                ", fooPassword='" + fooPassword + '\'' +
                '}';
    }
}
