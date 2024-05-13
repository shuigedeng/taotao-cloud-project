package com.taotao.cloud.mq.common.util;

import com.github.houbb.heaven.annotation.CommonEager;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.load.balance.api.ILoadBalance;
import com.github.houbb.load.balance.api.impl.LoadBalanceContext;
import com.github.houbb.load.balance.support.server.IServer;

import java.util.List;
import java.util.Objects;

/**
 * @author shuigedeng
 * @since 2024.05
 */
@CommonEager
public class RandomUtils {

    /**
     * 负载均衡
     *
     * @param list 列表
     * @param key 分片键
     * @return 结果
     * @since 2024.05
     */
    public static <T extends IServer> T loadBalance(final ILoadBalance<T> loadBalance,
                                                    final List<T> list, String key) {
        if(CollectionUtil.isEmpty(list)) {
            return null;
        }

        if(StringUtil.isEmpty(key)) {
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
