package com.taotao.cloud.mq.common.util;


import com.taotao.cloud.mq.common.balance.ILoadBalance;
import com.taotao.cloud.mq.common.balance.IServer;
import com.taotao.cloud.mq.common.balance.impl.LoadBalanceContext;
import com.xkzhangsan.time.utils.CollectionUtil;
import com.xkzhangsan.time.utils.StringUtil;

import java.util.List;
import java.util.Objects;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class RandomUtils {

	/**
	 * 负载均衡
	 *
	 * @param list 列表
	 * @param key  分片键
	 * @return 结果
	 * @since 2024.05
	 */
	public static <T extends IServer> T loadBalance(final ILoadBalance<T> loadBalance,
		final List<T> list, String key) {
		if (CollectionUtil.isEmpty(list)) {
			return null;
		}

		if (StringUtil.isEmpty(key)) {
			LoadBalanceContext<T> loadBalanceContext = LoadBalanceContext.<T>newInstance()
				.servers(list);
			return loadBalance.select(loadBalanceContext);
		}

		// 获取 code
		int hashCode = Objects.hash(key);
		int index = hashCode % list.size();
		return list.get(index);
	}

}
