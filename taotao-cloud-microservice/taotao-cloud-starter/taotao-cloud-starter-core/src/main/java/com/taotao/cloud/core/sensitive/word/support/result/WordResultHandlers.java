package com.taotao.cloud.core.sensitive.word.support.result;

import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.core.sensitive.word.api.IWordResult;
import com.taotao.cloud.core.sensitive.word.api.IWordResultHandler;

/**
 * 敏感词的结果处理
 */
public final class WordResultHandlers {

    private WordResultHandlers(){}

    /**
     * 不做任何处理
     * @return 结果
     * @since 0.1.0
     */
    public static IWordResultHandler<IWordResult> raw() {
        return Instances.singleton(WordResultHandlerRaw.class);
    }

    /**
     * 只保留单词
     * @return 结果
     * @since 0.1.0
     */
    public static IWordResultHandler<String> word() {
        return Instances.singleton(WordResultHandlerWord.class);
    }

}
