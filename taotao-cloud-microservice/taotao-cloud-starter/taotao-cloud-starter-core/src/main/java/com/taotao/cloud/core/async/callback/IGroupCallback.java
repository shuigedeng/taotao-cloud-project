package com.taotao.cloud.core.async.callback;

import com.taotao.cloud.core.async.wrapper.WorkerWrapper;

import java.util.List;

/**
 * 如果是异步执行整组的话，可以用这个组回调。不推荐使用
 */
public interface IGroupCallback {
    /**
     * 成功后，可以从wrapper里去getWorkResult
     */
    void success(List<WorkerWrapper> workerWrappers);
    /**
     * 失败了，也可以从wrapper里去getWorkResult
     */
    void failure(List<WorkerWrapper> workerWrappers, Exception e);
}
