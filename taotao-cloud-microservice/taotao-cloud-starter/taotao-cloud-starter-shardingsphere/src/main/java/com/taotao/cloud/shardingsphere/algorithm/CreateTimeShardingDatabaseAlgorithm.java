package com.taotao.cloud.shardingsphere.algorithm;

import com.google.common.collect.Range;
import com.taotao.cloud.common.utils.date.DateUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

/**
 * 按创建时间年份分库
 */
public class CreateTimeShardingDatabaseAlgorithm implements StandardShardingAlgorithm<Long> {

	@Override
	public String doSharding(Collection<String> collection,
		PreciseShardingValue<Long> preciseShardingValue) {
		Long createTime = preciseShardingValue.getValue();
		String value = DateUtil.toString(createTime, "yyyy");
		//data2019,data2020
		return "data" + value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<String> doSharding(Collection collection,
		RangeShardingValue rangeShardingValue) {
		Collection<String> collect = new ArrayList<>();
		Range<Integer> valueRange = rangeShardingValue.getValueRange();

		//开始年份结束年份
		String start = DateUtil.toString(valueRange.lowerEndpoint().longValue(), "yyyy");
		String end = DateUtil.toString(valueRange.upperEndpoint().longValue(), "yyyy");
		//循环增加区间的查询条件
		for (int i = Integer.parseInt(start); i <= Integer.parseInt(end); i++) {
			collect.add("data" + i);
		}
		return collect;
	}

	// @Override
	// public void init() {
	//
	// }

	@Override
	public String getType() {
		return null;
	}

	@Override
	public Properties getProps() {
		return null;
	}

	@Override
	public void init(Properties props) {

	}
}

