package com.taotao.cloud.product.biz.controller;

import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.product.api.dto.ProductDTO;
import com.taotao.cloud.product.api.vo.ProductVO;
import com.taotao.cloud.product.biz.entity.Product;
import com.taotao.cloud.product.biz.mapper.ProductMapper;
import com.taotao.cloud.product.biz.service.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品管理API
 *
 * @author dengtao
 * @date 2020/4/30 11:03
 */
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/product")
@Api(value = "商品管理API", tags = {"商品管理API"})
public class ProductController {

	private final IProductService productInfoService;

	@ApiOperation("根据id查询商品信息")
	@RequestOperateLog(description = "根据id查询商品信息")
	@PreAuthorize("hasAuthority('product:info:id')")
	@GetMapping(value = "/info/id/{id:[0-9]*}")
	Result<ProductVO> findProductInfoById(@PathVariable("id") Long id) {
		Product product = productInfoService.findProductById(id);
		ProductVO vo = ProductMapper.INSTANCE.productToProductVO(product);
		return Result.succeed(vo);
	}

	@ApiOperation("添加商品信息")
	@RequestOperateLog(description = "添加商品信息")
	@PreAuthorize("hasAuthority('product:info:save')")
	@PostMapping
	Result<ProductVO> saveProduct(@Validated @RequestBody ProductDTO productDTO) {
		Product product = productInfoService.saveProduct(productDTO);
		ProductVO vo = ProductMapper.INSTANCE.productToProductVO(product);
		return Result.succeed(vo);
	}

}

