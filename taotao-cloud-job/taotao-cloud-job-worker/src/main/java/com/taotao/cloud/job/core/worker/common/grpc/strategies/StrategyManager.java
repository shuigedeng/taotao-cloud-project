package com.taotao.cloud.job.core.worker.common.grpc.strategies;

import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.common.exception.TtcJobException;
import com.taotao.cloud.worker.common.constant.TransportTypeEnum;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class StrategyManager {

    public static Map<TransportTypeEnum, GrpcStrategy<?>> strategyMap = new HashMap<>();

    public static <T> void registerCausa(
           TransportTypeEnum ruleType, GrpcStrategy<?> strategy) {
        strategyMap.put(ruleType, strategy);
    }

    @SuppressWarnings("unchecked")
    public static <T> GrpcStrategy<T> match(TransportTypeEnum ruleType) {

        if (!strategyMap.containsKey(ruleType)) {
            throw new TtcJobException("There's no strategy defined for this type: \"");
        }

        return (GrpcStrategy<T>) strategyMap.get(ruleType);
    }

}
