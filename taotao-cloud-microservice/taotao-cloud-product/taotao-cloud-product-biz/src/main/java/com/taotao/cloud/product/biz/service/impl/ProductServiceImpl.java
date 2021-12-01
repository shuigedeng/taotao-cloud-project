/**
 * Project Name: my-projects
 * Package Name: com.taotao.cloud.order.biz.service.impl
 * Date: 2020/6/10 16:55
 * Author: shuigedeng
 */
package com.taotao.cloud.product.biz.service.impl;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.bean.BeanUtil;
import com.taotao.cloud.product.api.dto.ProductDTO;
import com.taotao.cloud.product.biz.entity.Product;
import com.taotao.cloud.product.biz.repository.ProductSuperRepository;
import com.taotao.cloud.product.biz.service.IProductService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 *
 * @author shuigedeng
 * @since 2020/6/10 16:55
 * @version 1.0.0
 */
@Service
public class ProductServiceImpl implements IProductService {

    private final ProductSuperRepository productRepository;

	public ProductServiceImpl(
		ProductSuperRepository productRepository) {
		this.productRepository = productRepository;
	}

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
