package com.taotao.cloud.core.sensitive.word.api;

import java.util.List;

/**
 * 拒绝出现的数据-返回的内容被当做是敏感词
 */
public interface IWordDeny {

    /**
     * 获取结果
     * @return 结果
     * @since 0.0.13
     */
    List<String> deny();

}
