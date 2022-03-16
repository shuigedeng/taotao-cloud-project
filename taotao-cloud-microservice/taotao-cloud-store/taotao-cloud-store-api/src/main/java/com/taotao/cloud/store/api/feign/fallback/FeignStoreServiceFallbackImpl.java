package com.taotao.cloud.store.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.store.api.dto.CollectionDTO;
import com.taotao.cloud.store.api.feign.IFeignStoreService;
import com.taotao.cloud.store.api.vo.StoreVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignStoreServiceFallbackImpl implements FallbackFactory<IFeignStoreService> {

	@Override
	public IFeignStoreService create(Throwable throwable) {
		return new IFeignStoreService() {

			@Override
			public Result<StoreVO> findSotreById(String id) {
				return null;
			}

			@Override
			public Result<Boolean> updateStoreCollectionNum(
				CollectionDTO collectionDTO) {
				return null;
			}
		};
	}
}
