package com.taotao.cloud.sys.api.feign.fallback;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.feign.IFeignDictService;
import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignDictFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignDictServiceFallback implements FallbackFactory<IFeignDictService> {
	@Override
	public IFeignDictService create(Throwable throwable) {
		LogUtils.info("throwablethrowablethrowablethrowablethrowable");

		return new IFeignDictService() {
			@Override
			public FeignDictResponse findByCode(String code) {

				if (StrUtil.isNotBlank(RootContext.getXID())) {
					try {
						GlobalTransactionContext.reload(RootContext.getXID()).rollback();
					} catch (TransactionException e) {
						LogUtils.error(e);
					}
				}

				return null;
			}
		};
	}
}
