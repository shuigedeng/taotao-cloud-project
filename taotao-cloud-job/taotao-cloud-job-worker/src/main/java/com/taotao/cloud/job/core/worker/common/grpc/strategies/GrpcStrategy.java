package com.taotao.cloud.job.core.worker.common.grpc.strategies;


import io.grpc.ManagedChannel;
import com.taotao.cloud.worker.common.constant.TransportTypeEnum;

public interface GrpcStrategy<T> {

    /**
     * for different type of stub
     */
    void init();

    Object execute(Object params);

    TransportTypeEnum getTypeEnumFromStrategyClass();


}

