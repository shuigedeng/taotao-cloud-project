package com.taotao.cloud.sensitive.sensitive.word.api;

import java.util.List;

/**
 * 数据词接口
 */
public interface IWordData {

    /**
     * 获取对应的敏感词
     * @return 结果
     */
    List<String> getWordData();

}
