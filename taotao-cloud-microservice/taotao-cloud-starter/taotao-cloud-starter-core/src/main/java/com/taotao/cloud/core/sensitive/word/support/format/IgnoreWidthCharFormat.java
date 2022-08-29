package com.taotao.cloud.core.sensitive.word.support.format;

import com.taotao.cloud.common.utils.lang.CharUtils;
import com.taotao.cloud.core.sensitive.word.api.ICharFormat;
import com.taotao.cloud.core.sensitive.word.api.IWordContext;

/**
 * 格式化责任链
 */
public class IgnoreWidthCharFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        return CharUtils.toHalfWidth(original);
    }

}
