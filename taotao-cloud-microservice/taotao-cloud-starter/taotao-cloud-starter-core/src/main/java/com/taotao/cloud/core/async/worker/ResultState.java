package com.taotao.cloud.core.async.worker;

/**
 * 结果状态
 */
public enum ResultState {
    SUCCESS,
    TIMEOUT,
    EXCEPTION,
    DEFAULT  //默认状态
}
