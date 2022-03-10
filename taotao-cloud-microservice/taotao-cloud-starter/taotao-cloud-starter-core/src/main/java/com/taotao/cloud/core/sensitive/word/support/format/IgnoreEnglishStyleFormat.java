package com.taotao.cloud.core.sensitive.word.support.format;
import com.taotao.cloud.core.sensitive.word.api.ICharFormat;
import com.taotao.cloud.core.sensitive.word.api.IWordContext;
import com.taotao.cloud.core.sensitive.word.utils.CharUtils;

/**
 * 忽略英文的各种格式
 */
public class IgnoreEnglishStyleFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        return CharUtils.getMappingChar(original);
    }

}
