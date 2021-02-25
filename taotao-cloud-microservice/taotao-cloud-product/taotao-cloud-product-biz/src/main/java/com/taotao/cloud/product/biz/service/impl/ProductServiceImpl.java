/**
 * Project Name: my-projects
 * Package Name: com.taotao.cloud.order.biz.service.impl
 * Date: 2020/6/10 16:55
 * Author: dengtao
 */
package com.taotao.cloud.product.biz.service.impl;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.core.utils.BeanUtil;
import com.taotao.cloud.product.api.dto.ProductDTO;
import com.taotao.cloud.product.biz.entity.Product;
import com.taotao.cloud.product.biz.repository.ProductRepository;
import com.taotao.cloud.product.biz.service.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <br>
 *
 * @author dengtao
 * @date 2020/6/10 16:55
 * @since v1.0
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    @Override
    public Product findProductById(Long id) {
        Optional<Product> optionalProductInfo = productRepository.findById(id);
        return optionalProductInfo.orElseThrow(() -> new BusinessException(ResultEnum.PRODUCT_NOT_EXIST));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product saveProduct(ProductDTO productDTO) {
        Product product = Product.builder().build();
        BeanUtil.copyIgnoredNull(productDTO, product);
        return productRepository.saveAndFlush(product);
    }
}
