package com.taotao.cloud.dubbo.loadbalance;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;

/**
 * ShortestResponseLoadBalance
 * </p>
 * Filter the number of invokers with the shortest response time of
 * success calls and count the weights and quantities of these invokers in last slide window.
 * If there is only one invoker, use the invoker directly;
 * if there are multiple invokers and the weights are not the same, then random according to the total weight;
 * if there are multiple invokers and the same weight, then randomly called.
 */
public class CustomLoadBalance extends AbstractLoadBalance {

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {

		LogUtil.info("CustomLoadBalance doSelect activate ------------------------------");
		LogUtil.info(String.valueOf(invokers.size()));

		return invokers.get(0);

    }
}
