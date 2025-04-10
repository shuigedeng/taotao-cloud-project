package com.taotao.cloud.cache.listener;

import com.taotao.cloud.cache.api.ICacheRemoveListener;
import com.taotao.cloud.cache.api.ICacheRemoveListenerContext;

/**
 * @author shuigedeng
 * @since 2024.06
 */
public class MyRemoveListener<K, V> implements ICacheRemoveListener<K, V> {

	@Override
	public void listen(ICacheRemoveListenerContext<K, V> context) {
		System.out.println("【删除提示】可恶，我竟然被删除了！" + context.key());
	}

}
