package com.taotao.cloud.rpc.common.common.rpc.domain;

import java.io.Serializable;

/**
 * 序列化相关处理
 * @author shuigedeng
 * @since 2024.06
 */
public interface BaseRpc extends Serializable {

    /**
     * 获取唯一标识号
     * （1）用来唯一标识一次调用，便于获取该调用对应的响应信息。
     * @return 唯一标识号
     */
    String seqId();

    /**
     * 设置唯一标识号
     * @param traceId 唯一标识号
     * @return this
     */
    BaseRpc seqId(final String traceId);

}
