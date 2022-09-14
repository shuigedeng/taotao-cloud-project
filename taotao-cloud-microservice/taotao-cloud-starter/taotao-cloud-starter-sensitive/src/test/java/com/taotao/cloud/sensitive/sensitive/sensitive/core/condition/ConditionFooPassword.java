package com.taotao.cloud.sensitive.sensitive.sensitive.core.condition;


import com.taotao.cloud.sensitive.sensitive.sensitive.api.IContext;

import java.lang.reflect.Field;

/**
 * 让这些 123456 的密码不进行脱敏
 */
public class ConditionFooPassword implements ICondition {
    @Override
    public boolean valid(IContext context) {
        try {
            Field field = context.getCurrentField();
            final Object currentObj = context.getCurrentObject();
            final String name = (String) field.get(currentObj);
            return !name.equals("123456");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
