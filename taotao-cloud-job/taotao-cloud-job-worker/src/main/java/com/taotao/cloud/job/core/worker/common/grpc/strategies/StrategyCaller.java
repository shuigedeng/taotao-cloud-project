package com.taotao.cloud.job.core.worker.common.grpc.strategies;

import com.taotao.cloud.worker.common.constant.TransportTypeEnum;

public class StrategyCaller {

    public static Object call(TransportTypeEnum rule, Object params){
        GrpcStrategy<Object> match = StrategyManager.match(rule);
        return match.execute(params);
    }

}
