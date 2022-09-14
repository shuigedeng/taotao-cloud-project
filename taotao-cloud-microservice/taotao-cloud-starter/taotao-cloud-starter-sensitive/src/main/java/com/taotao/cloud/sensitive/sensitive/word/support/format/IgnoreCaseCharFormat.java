package com.taotao.cloud.sensitive.sensitive.word.support.format;


import com.taotao.cloud.sensitive.sensitive.word.api.ICharFormat;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordContext;

/**
 * 忽略大小写
 */
public class IgnoreCaseCharFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        return Character.toLowerCase(original);
    }

}
