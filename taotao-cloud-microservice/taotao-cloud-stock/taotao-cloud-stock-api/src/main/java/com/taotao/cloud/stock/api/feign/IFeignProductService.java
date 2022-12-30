package com.taotao.cloud.stock.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.stock.api.dto.ProductDTO;
import com.taotao.cloud.stock.api.feign.fallback.FeignProductFallback;
import com.taotao.cloud.stock.api.vo.ProductVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "RemoteProductService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignProductFallback.class)
public interface IFeignProductService {

	/**
	 * 根据id查询商品信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.product.api.vo.ProductVO>
	 * @author shuigedeng
	 * @since 2020/11/20 下午3:23
	 * @version 2022.03
	 */
	@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	ProductVO findProductInfoById(@PathVariable("id") Long id);

	/**
	 * 添加商品信息
	 *
	 * @param productDTO productDTO
	 * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.product.api.vo.ProductVO>
	 * @author shuigedeng
	 * @since 2020/11/20 下午3:23
	 * @version 2022.03
	 */
	@PostMapping(value = "/product")
ProductVO saveProduct(@RequestBody ProductDTO productDTO);
}

