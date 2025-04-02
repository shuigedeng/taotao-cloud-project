package com.taotao.cloud.job.worker.common.grpc.strategies;


import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;

public class StrategyCaller {

    public static Object call(TransportTypeEnum rule, Object params){
        GrpcStrategy<Object> match = StrategyManager.match(rule);
        return match.execute(params);
    }

}
