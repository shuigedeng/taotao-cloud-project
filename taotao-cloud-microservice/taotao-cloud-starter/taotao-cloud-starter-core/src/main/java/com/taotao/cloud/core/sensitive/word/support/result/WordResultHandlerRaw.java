package com.taotao.cloud.core.sensitive.word.support.result;

import com.taotao.cloud.core.sensitive.word.api.IWordResult;
import com.taotao.cloud.core.sensitive.word.api.IWordResultHandler;

/**
 * 不做任何处理
 */
public class WordResultHandlerRaw implements IWordResultHandler<IWordResult> {

    @Override
    public IWordResult handle(IWordResult wordResult) {
        return wordResult;
    }

}
