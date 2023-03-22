package com.taotao.cloud.log.biz.other.server.application.util;

import org.springframework.beans.factory.annotation.Value;

public enum IdWorkerInstance {
    INSTANCE;

    @Value("${idworker.datacent}")
    private long idWorkerDatacent;


    @Value("${idworker.nodeId}")
    private long idWorkerNodeId;

    public IdWorker IdWorkerInstance(){ return new DefaultIdWorker(idWorkerDatacent,idWorkerNodeId);};
}
