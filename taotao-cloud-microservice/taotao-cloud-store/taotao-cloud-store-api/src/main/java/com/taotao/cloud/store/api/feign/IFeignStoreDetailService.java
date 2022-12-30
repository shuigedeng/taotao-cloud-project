package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreServiceFallbackImpl;
import com.taotao.cloud.store.api.web.vo.StoreAfterSaleAddressVO;
import com.taotao.cloud.store.api.web.vo.StoreBasicInfoVO;
import com.taotao.cloud.store.api.web.vo.StoreDetailInfoVO;
import com.taotao.cloud.store.api.web.vo.StoreDetailVO;
import com.taotao.cloud.store.api.web.vo.StoreOtherVO;
import com.taotao.cloud.store.api.web.vo.StoreVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用店铺模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignStoreDetailService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreServiceFallbackImpl.class)
public interface IFeignStoreDetailService {

	@GetMapping(value = "/sotre/info/id/{id:[0-9]*}")
	StoreVO findSotreById(@RequestParam String id);

	@GetMapping(value = "/sotre/info/id/{id:[0-9]*}")
	StoreAfterSaleAddressVO getStoreAfterSaleAddressDTO(@RequestParam Long storeId);

	@GetMapping(value = "/get/detail/{storeId}")
	StoreDetailInfoVO getStoreDetailVO(@PathVariable Long storeId);
	@GetMapping(value = "/getStoreBasicInfoDTO")
	StoreBasicInfoVO getStoreBasicInfoDTO(String id);
	@GetMapping(value = "/getStoreOtherVO")
	StoreOtherVO getStoreOtherVO(String id);

	StoreDetailVO getStoreDetailVOByMemberId(Long userId);
}

