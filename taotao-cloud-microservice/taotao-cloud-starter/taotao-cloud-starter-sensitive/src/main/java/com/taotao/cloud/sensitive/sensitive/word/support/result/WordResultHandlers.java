package com.taotao.cloud.sensitive.sensitive.word.support.result;

import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordResult;
import com.taotao.cloud.sensitive.sensitive.word.api.IWordResultHandler;

/**
 * 敏感词的结果处理
 */
public final class WordResultHandlers {

    private WordResultHandlers(){}

    /**
     * 不做任何处理
     * @return 结果
     */
    public static IWordResultHandler<IWordResult> raw() {
        return Instances.singleton(WordResultHandlerRaw.class);
    }

    /**
     * 只保留单词
     * @return 结果
     */
    public static IWordResultHandler<String> word() {
        return Instances.singleton(WordResultHandlerWord.class);
    }

}
