package com.taotao.cloud.file.api.feign.fallback;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.file.api.feign.IFeignFileApi;
import com.taotao.cloud.file.api.feign.response.FeignFileResponse;
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
public class FeignFileApiFallback implements FallbackFactory<IFeignFileApi> {

	@Override
	public IFeignFileApi create(Throwable throwable) {
		LogUtils.info("throwablethrowablethrowablethrowablethrowable");

		return new IFeignFileApi() {
			@Override
			public FeignFileResponse findByCode(String code) {

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
