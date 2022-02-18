package com.taotao.cloud.shardingsphere.algorithm;

import com.taotao.cloud.common.utils.DateUtil;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

/**
 * 按创建时间月份分表
 */
public class CreateTimeShardingTableAlgorithmBak implements PreciseShardingAlgorithm<Long>,
	RangeShardingAlgorithm {


	@Override
	public String doSharding(Collection<String> collection,
		PreciseShardingValue<Long> preciseShardingValue) {
		Long createTime = preciseShardingValue.getValue();
		String value = DateUtil.toString(createTime, "MM");
		Integer month = Integer.valueOf(value);
		//t_order_1,t_order_2~
		return "t_order_" + month;

	}

	@Override
	public Collection<String> doSharding(Collection collection,
		RangeShardingValue rangeShardingValue) {
		Collection<String> collect = new ArrayList<>();
		//因为考虑到 假设2019-05～2020-05
		//这快是没办法处理的，因为分库分表之后，每个库都要进行查询，如果操作为，1-4月，那么2020年数据查询正确了，可是2019年到5-12月数据就查询不到了
		//这里需要做一些性能的浪费，现在看来是没办法处理到
		for (int i = 1; i <= 12; i++) {
			collect.add("t_order_" + i);
		}
		return collect;
	}
}

