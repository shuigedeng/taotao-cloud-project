package com.taotao.cloud.core.async.callback;

import com.taotao.cloud.core.async.wrapper.WorkerWrapper;

import java.util.List;

public class DefaultGroupCallback implements IGroupCallback {
    @Override
    public void success(List<WorkerWrapper> workerWrappers) {

    }

    @Override
    public void failure(List<WorkerWrapper> workerWrappers, Exception e) {

    }
}
