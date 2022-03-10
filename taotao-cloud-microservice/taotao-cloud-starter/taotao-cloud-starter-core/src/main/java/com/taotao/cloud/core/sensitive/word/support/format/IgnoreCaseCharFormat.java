package com.taotao.cloud.core.sensitive.word.support.format;

import com.taotao.cloud.core.sensitive.word.api.ICharFormat;
import com.taotao.cloud.core.sensitive.word.api.IWordContext;

/**
 * 忽略大小写
 */
public class IgnoreCaseCharFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        return Character.toLowerCase(original);
    }

}
