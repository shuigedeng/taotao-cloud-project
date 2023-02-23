package com.taotao.cloud.stock.api.common.util.validator;

import com.xtoon.boot.common.util.exception.XTException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 * @author shuigedeng
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new XTException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new XTException(message);
        }
    }
}
