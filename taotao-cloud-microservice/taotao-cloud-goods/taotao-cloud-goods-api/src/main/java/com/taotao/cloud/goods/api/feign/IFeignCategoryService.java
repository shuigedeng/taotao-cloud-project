package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.goods.api.feign.fallback.RemoteProductFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignCategoryService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = RemoteProductFallbackImpl.class)
public interface IFeignCategoryService {

	///**
	// * 根据id查询商品信息
	// *
	// * @param id id
	// * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.product.api.vo.ProductVO>
	// * @author shuigedeng
	// * @since 2020/11/20 下午3:23
	// * @version 2022.03
	// */
	//@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	//Result<ProductVO> findProductInfoById(@PathVariable("id") Long id);
	//
	///**
	// * 添加商品信息
	// *
	// * @param productDTO productDTO
	// * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.product.api.vo.ProductVO>
	// * @author shuigedeng
	// * @since 2020/11/20 下午3:23
	// * @version 2022.03
	// */
	//@PostMapping(value = "/product")
	//Result<ProductVO> saveProduct(@RequestBody ProductDTO productDTO);
}

