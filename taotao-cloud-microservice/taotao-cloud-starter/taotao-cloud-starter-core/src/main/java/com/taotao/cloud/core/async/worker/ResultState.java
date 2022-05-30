package com.taotao.cloud.core.async.worker;

/**
 * 结果状态
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:25:20
 */
public enum ResultState {
    SUCCESS,
    TIMEOUT,
    EXCEPTION,
    DEFAULT  //默认状态
}
