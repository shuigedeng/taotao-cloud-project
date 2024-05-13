package com.taotao.cloud.rpc.common.common.support.status.service;

/**
 * 状态管理
 *
 * <p> project: rpc-StatusManager </p>
 * <p> create on 2019/10/30 20:48 </p>
 *
 * @author Administrator
 * @since 0.1.3
 */
public interface StatusManager {

    /**
     * 获取状态编码
     * @return 状态编码
     * @since 0.1.3
     */
    int status();

    /**
     * 设置状态编码
     * @param code 编码
     * @return this
     * @since 0.1.3
     */
    StatusManager status(final int code);

}
