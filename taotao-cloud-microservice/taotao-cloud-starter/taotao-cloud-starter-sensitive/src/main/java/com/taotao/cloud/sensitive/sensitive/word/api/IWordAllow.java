package com.taotao.cloud.sensitive.sensitive.word.api;

import java.util.List;

/**
 * 允许的内容-返回的内容不被当做敏感词
 */
public interface IWordAllow {

    /**
     * 获取结果
     * @return 结果
     */
    List<String> allow();

}
