package com.taotao.cloud.sys.biz.tools.name.service;

import java.util.Set;

/**
 * 英语翻译
 */
public interface Translate extends ToolName {

    /**
     * 执行翻译链
     * @param translateCharSequence
     */
    void doTranslate(TranslateCharSequence translateCharSequence);

}
