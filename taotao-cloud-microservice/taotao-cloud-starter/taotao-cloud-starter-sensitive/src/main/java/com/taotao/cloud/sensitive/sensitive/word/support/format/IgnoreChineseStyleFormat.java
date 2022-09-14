package com.taotao.cloud.sensitive.sensitive.word.support.format;


import com.taotao.cloud.sensitive.sensitive.word.api.ICharFormat;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordContext;

/**
 * 忽略大小写
 */
public class IgnoreChineseStyleFormat implements ICharFormat {

    @Override
    public char format(char original, IWordContext context) {
        String string = String.valueOf(original);
        //String simple = ZhConvertBootstrap.newInstance(new CharSegment()).toSimple(string);
        //return simple.charAt(0);
        return string.charAt(0);
    }

}
