package com.taotao.cloud.sensitive.sensitive.word.support.format;

import com.taotao.cloud.sensitive.sensitive.word.api.ICharFormat;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordContext;
import com.taotao.cloud.sensitive.sensitive.word.utils.CharUtils;

/**
 * 忽略英文的各种格式
 */
public class IgnoreEnglishStyleFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        return CharUtils.getMappingChar(original);
    }

}
