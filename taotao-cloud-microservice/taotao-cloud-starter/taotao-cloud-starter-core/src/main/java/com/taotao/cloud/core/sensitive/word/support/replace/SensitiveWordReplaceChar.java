package com.taotao.cloud.core.sensitive.word.support.replace;

import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.util.lang.CharUtil;
import com.taotao.cloud.core.sensitive.word.api.ISensitiveWordReplace;
import com.taotao.cloud.core.sensitive.word.api.ISensitiveWordReplaceContext;

/**
 * 指定字符的替换策略
 */
@ThreadSafe
public class SensitiveWordReplaceChar implements ISensitiveWordReplace {

    private final char replaceChar;

    public SensitiveWordReplaceChar(char replaceChar) {
        this.replaceChar = replaceChar;
    }

    @Override
    public String replace(ISensitiveWordReplaceContext context) {
        int wordLength = context.wordLength();

        return CharUtil.repeat(replaceChar, wordLength);
    }

}
