package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.store.api.web.dto.CollectionDTO;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreApiFallback;
import com.taotao.cloud.store.api.web.dto.StoreBankDTO;
import com.taotao.cloud.store.api.web.dto.StoreCompanyDTO;
import com.taotao.cloud.store.api.web.dto.StoreOtherInfoDTO;
import com.taotao.cloud.store.api.web.query.StorePageQuery;
import com.taotao.cloud.store.api.web.vo.StoreVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用店铺模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignStoreService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreApiFallback.class)
public interface IFeignStoreApi {

	@GetMapping(value = "/sotre/info/findSotreById")
	StoreVO findSotreById(@RequestParam Long id);

	@GetMapping(value = "/updateStoreCollectionNum")
	Boolean updateStoreCollectionNum(CollectionDTO collectionDTO);

	@GetMapping(value = "/getStoreDetail")
	StoreVO getStoreDetail();

	@GetMapping(value = "/sotre/findSotreByMemberId")
	StoreVO findSotreByMemberId(@RequestParam Long memberId);

	@GetMapping(value = "/findByConditionPage")
	PageResult<StoreVO> findByConditionPage(StorePageQuery storePageQuery);

	@GetMapping(value = "/applyFirstStep")
	boolean applyFirstStep(StoreCompanyDTO storeCompanyDTO);

	@GetMapping(value = "/applySecondStep")
	boolean applySecondStep(StoreBankDTO storeBankDTO);

	@GetMapping(value = "/applyThirdStep")
	boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO);
}

