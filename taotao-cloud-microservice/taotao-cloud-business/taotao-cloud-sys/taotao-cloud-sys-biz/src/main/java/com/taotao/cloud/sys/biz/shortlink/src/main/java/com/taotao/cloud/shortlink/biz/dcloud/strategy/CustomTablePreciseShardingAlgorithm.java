package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.strategy;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @Description
 * @Author 刘森飚
 **/

public class CustomTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

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

        //获取逻辑表
        String targetName = availableTargetNames.iterator().next();

        //短链码  A23Ad1
        String value = shardingValue.getValue();

        //获取短链码最后一位
        String codeSuffix =  value.substring(value.length()-1);

        //拼接Actual table
        return targetName+"_"+codeSuffix;
    }
}
