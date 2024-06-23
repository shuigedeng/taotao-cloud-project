package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.strategy;

import net.xdclass.enums.BizCodeEnum;
import net.xdclass.exception.BizException;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @Description
 * @Author 刘森飚
 **/

public class CustomDBPreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     * @param availableTargetNames 数据源集合
     *                             在分库时值为所有分片库的集合 databaseNames
     *                             分表时为对应分片库中所有分片表的集合 tablesNames
     * @param shardingValue        分片属性，包括
     *                             logicTableName 为逻辑表，
     *                             columnName 分片健（字段），
     *                             value 为从 SQL 中解析出的分片健的值
     * @return
     */

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {

        //获取短链码第一位，即库位
        String codePrefix = shardingValue.getValue().substring(0, 1);

        for (String targetName : availableTargetNames) {
            //获取库名的最后一位，真实配置的ds
            //ds0,ds1,dsa -> 0,1,a
            String targetNameSuffix = targetName.substring(targetName.length() - 1);

            //如果一致则返回
            if (codePrefix.equals(targetNameSuffix)) {
                return targetName;
            }
        }

        //抛异常
        throw new BizException(BizCodeEnum.DB_ROUTE_NOT_FOUND);

    }
}
