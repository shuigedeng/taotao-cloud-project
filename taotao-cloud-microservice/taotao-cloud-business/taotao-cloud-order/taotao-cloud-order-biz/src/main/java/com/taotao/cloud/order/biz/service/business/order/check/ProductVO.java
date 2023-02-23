package com.taotao.cloud.order.biz.service.business.order.check;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * 商品对象
 */
@Data
@Builder
public class ProductVO {
    /**
     * 商品SKU，唯一
     */
    private Long skuId;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 商品图片路径
     */
    private String imgPath;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 库存
     */
    private Integer stock;
}
