package com.taotao.cloud.product.biz.service;


import com.taotao.cloud.product.api.dto.ProductDTO;
import com.taotao.cloud.product.biz.entity.Product;

/**
 * 订单管理service
 *
 * @author dengtao
 * @date 2020/4/30 11:03
 */
public interface IProductService {

    /**
     * 根据id查询商品信息
     *
     * @param id
     * @return com.taotao.cloud.product.biz.entity.ProductInfo
     * @author dengtao
     * @date 2020/10/23 09:10
     * @since v1.0
     */
    Product findProductById(Long id);

    /**
     * 添加商品信息
     *
     * @param productDTO
     * @return com.taotao.cloud.product.biz.entity.ProductInfo
     * @author dengtao
     * @date 2020/10/23 09:23
     * @since v1.0
     */
    Product saveProduct(ProductDTO productDTO);
}

