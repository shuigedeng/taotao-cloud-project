package com.taotao.cloud.core.sensitive.word.utils;

import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.core.sensitive.word.api.ICharFormat;
import com.taotao.cloud.core.sensitive.word.api.IWordContext;
import com.taotao.cloud.core.sensitive.word.support.format.CharFormatChain;

/**
 * 内部格式化工具类
 */
public final class InnerFormatUtils {

    private InnerFormatUtils(){}

    /**
     * 格式化
     * @param original 原始
     * @param context 上下文
     * @return 结果
     * @since 0.1.1
     */
    public static String format(String original,  IWordContext context) {
        if(StringUtils.isEmpty(original)) {
            return original;
        }

        StringBuilder stringBuilder = new StringBuilder();
        ICharFormat charFormat = Instances.singleton(CharFormatChain.class);
        char[] chars = original.toCharArray();
        for(char c : chars) {
            char cf = charFormat.format(c, context);
            stringBuilder.append(cf);
        }

        return stringBuilder.toString();
    }

}
