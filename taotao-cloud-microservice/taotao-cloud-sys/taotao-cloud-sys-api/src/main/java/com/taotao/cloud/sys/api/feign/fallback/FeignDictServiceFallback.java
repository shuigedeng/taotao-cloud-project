package com.taotao.cloud.sys.api.feign.fallback;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.sys.api.feign.IFeignDictService;
import com.taotao.cloud.sys.api.feign.response.FeignDictRes;
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
		LogUtil.info("throwablethrowablethrowablethrowablethrowable");

		return new IFeignDictService() {
			@Override
			public FeignDictRes findByCode(String code) {

				if(StrUtil.isNotBlank(RootContext.getXID())){
					try {
						GlobalTransactionContext.reload(RootContext.getXID()).rollback();
					} catch (TransactionException e) {
						LogUtil.error(e);
					}
				}

				return null;
			}
		};
	}
}
