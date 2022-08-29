package com.taotao.cloud.shardingsphere.algorithm;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Range;
import com.taotao.cloud.common.utils.date.DateUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * 按创建时间月份分表
 */
public class CreateTimeShardingTableAlgorithm implements StandardShardingAlgorithm<Long>{

	@Override
	public String doSharding(Collection<String> collection,
		PreciseShardingValue<Long> preciseShardingValue) {
		Long createTime = preciseShardingValue.getValue();
		String monthValue = DateUtils.toString(createTime, "MM");
		String yearValue = DateUtils.toString(createTime, "yyyy");
		Integer month = Integer.valueOf(monthValue);
		Integer year = Integer.valueOf(yearValue);
		//tt_order_1,tt_order_2~
		return "tt_order_" + year + "_" + month;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<String> doSharding(Collection collection,
		RangeShardingValue rangeShardingValue) {
		Collection<String> collect = new ArrayList<>();
		Range<Integer> valueRange = rangeShardingValue.getValueRange();

		Integer startMonth = Convert.toInt(
			DateUtils.toString(valueRange.lowerEndpoint().longValue(), "MM"));
		Integer endMonth = Convert.toInt(
			DateUtils.toString(valueRange.upperEndpoint().longValue(), "MM"));
		Integer startYear = Convert.toInt(
			DateUtils.toString(valueRange.lowerEndpoint().longValue(), "yyyy"));
		Integer endYear = Convert.toInt(
			DateUtils.toString(valueRange.upperEndpoint().longValue(), "yyyy"));

		//如果是同一年查询
		//2020-1~2020-2
		if (startYear.equals(endYear)) {
			for (Integer i = startYear; i <= endYear; i++) {
				for (Integer j = startMonth; j <= endMonth; j++) {
					collect.add("tt_order_" + i + "_" + j);
				}
			}
		}
		//2020-1~2021-2
		else {
			for (Integer i = startYear; i <= endYear; i++) {
				//如果是第一年
				if (i.equals(startYear)) {
					//计算从 开始月份 到 今年到12月
					for (Integer j = startMonth; j <= 12; j++) {
						collect.add("tt_order_" + i + "_" + j);
					}
				}
				//如果是最后一年
				else if (i.equals(endYear)) {
					//计算从 1月 到 最后一年结束月份
					for (Integer j = 1; j <= endMonth; j++) {
						collect.add("tt_order_" + i + "_" + j);
					}
				}
				//中间年份处理
				else {
					//中间年份，每个月都要进行查询处理
					for (int j = 1; j <= 12; j++) {
						collect.add("tt_order_" + i + "_" + j);
					}
				}
			}
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

