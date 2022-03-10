package com.taotao.cloud.core.sensitive.word.support.result;

import com.taotao.cloud.core.sensitive.word.api.IWordResult;
import com.taotao.cloud.core.sensitive.word.api.IWordResultHandler;

/**
 * 只保留单词
 *
 */
public class WordResultHandlerWord implements IWordResultHandler<String> {

    @Override
    public String handle(IWordResult wordResult) {
        if(wordResult == null) {
            return null;
        }
        return wordResult.word();
    }
    
}
