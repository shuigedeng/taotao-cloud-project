package com.taotao.cloud.sensitive.sensitive.word.support.format;


import com.taotao.cloud.sensitive.sensitive.word.api.ICharFormat;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordContext;
import com.taotao.cloud.sensitive.sensitive.word.utils.NumUtils;

/**
 * 忽略数字的样式
 */
public class IgnoreNumStyleCharFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        return NumUtils.getMappingChar(original);
    }

}
