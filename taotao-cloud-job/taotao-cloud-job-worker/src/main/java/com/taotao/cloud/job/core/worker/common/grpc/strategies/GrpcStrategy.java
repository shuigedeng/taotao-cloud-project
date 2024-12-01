package com.taotao.cloud.job.core.worker.common.grpc.strategies;


import com.taotao.cloud.job.core.worker.common.constant.TransportTypeEnum;
import io.grpc.ManagedChannel;

public interface GrpcStrategy<T> {

    /**
     * for different type of stub
     */
    void init();

    Object execute(Object params);

    TransportTypeEnum getTypeEnumFromStrategyClass();


}

