package com.taotao.cloud.auth.biz.authentication.authentication.oneClick.mobtech.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class BaseUtils {

    public static boolean isEmpty(Object target) {
        if (target == null) {
            return true;
        }
        if (target instanceof String && target.equals("")) {
            return true;
        } else if (target instanceof Collection) {
            return ((Collection<?>) target).isEmpty();
        } else if (target instanceof Map) {
            return ((Map<?, ?>) target).isEmpty();
        } else if (target.getClass().isArray()) {
            return Array.getLength(target) == 0;
        }
        return false;
    }

}
