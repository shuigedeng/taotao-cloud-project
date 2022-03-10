package com.taotao.cloud.core.sensitive.word.support.format;

import com.taotao.cloud.core.sensitive.word.api.ICharFormat;
import com.taotao.cloud.core.sensitive.word.api.IWordContext;
import com.taotao.cloud.core.sensitive.word.utils.NumUtils;

/**
 * 忽略数字的样式
 */
public class IgnoreNumStyleCharFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        return NumUtils.getMappingChar(original);
    }

}
