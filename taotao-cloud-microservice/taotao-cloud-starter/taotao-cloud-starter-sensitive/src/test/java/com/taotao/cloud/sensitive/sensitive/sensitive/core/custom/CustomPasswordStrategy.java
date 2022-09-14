package com.taotao.cloud.sensitive.sensitive.sensitive.core.custom;


import com.taotao.cloud.sensitive.sensitive.sensitive.api.IContext;
import com.taotao.cloud.sensitive.sensitive.sensitive.api.IStrategy;

public class CustomPasswordStrategy implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return "**********************";
    }

}
