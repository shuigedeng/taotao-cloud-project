package com.taotao.cloud.store.api.feign.fallback;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.store.api.web.dto.CollectionDTO;
import com.taotao.cloud.store.api.feign.IFeignStoreApi;
import com.taotao.cloud.store.api.web.dto.StoreBankDTO;
import com.taotao.cloud.store.api.web.dto.StoreCompanyDTO;
import com.taotao.cloud.store.api.web.dto.StoreOtherInfoDTO;
import com.taotao.cloud.store.api.web.query.StorePageQuery;
import com.taotao.cloud.store.api.web.vo.StoreVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignStoreApiFallback implements FallbackFactory<IFeignStoreApi> {

	@Override
	public IFeignStoreApi create(Throwable throwable) {
		return new IFeignStoreApi() {

			@Override
			public StoreVO findSotreById(Long id) {
				return null;
			}

			@Override
			public Boolean updateStoreCollectionNum(CollectionDTO collectionDTO) {
				return null;
			}

			@Override
			public StoreVO getStoreDetail() {
				return null;
			}

			@Override
			public StoreVO findSotreByMemberId(Long memberId) {
				return null;
			}

			@Override
			public PageResult<StoreVO> findByConditionPage(StorePageQuery storePageQuery) {
				return null;
			}

			@Override
			public boolean applyFirstStep(StoreCompanyDTO storeCompanyDTO) {
				return false;
			}

			@Override
			public boolean applySecondStep(StoreBankDTO storeBankDTO) {
				return false;
			}

			@Override
			public boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO) {
				return false;
			}
		};
	}
}
