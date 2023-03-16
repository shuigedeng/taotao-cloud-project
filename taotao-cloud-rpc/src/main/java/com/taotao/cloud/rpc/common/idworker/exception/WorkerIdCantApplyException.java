package com.taotao.cloud.rpc.common.idworker.exception;

/**
 * 机器码节点无法申请，因为节点数已满足最大值
 */
public class WorkerIdCantApplyException extends RuntimeException {
    public WorkerIdCantApplyException(String message) {
        super(message);
    }
}
