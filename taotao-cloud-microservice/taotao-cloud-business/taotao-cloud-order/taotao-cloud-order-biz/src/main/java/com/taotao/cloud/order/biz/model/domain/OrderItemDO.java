/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.biz.domain.order_item;

import java.math.BigDecimal;

/**
 * 订单项添加对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:32:51
 */
public class OrderItemDO {

    /** 订单子编码 */
    private String itemCode;
    /** 商品SPU ID */
    private Long productSpuId;
    /** 商品SPU_CODE */
    private String productSpuCode;
    /** 商品SPU名称 */
    private String productSpuName;
    /** 商品SKU ID */
    private Long productSkuId;
    /** 商品SKU 规格名称 */
    private String productSkuName;
    /** 商品单价 */
    private BigDecimal productPrice = BigDecimal.ZERO;
    /** 购买数量 */
    private Integer num = 1;
    /** 合计金额 */
    private BigDecimal sumAmount = BigDecimal.ZERO;
    /** 商品主图 */
    private String productPicUrl;
    /** 供应商id */
    private Long supplierId;
    /** 供应商名称 */
    private String supplierName;
    /** 超时退货期限 */
    private Integer refundTime;
    /** 退货数量 */
    private Integer rejectCount = 0;
    /** 商品类型 0 普通商品 1 秒杀商品 */
    private Integer type = 0;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Long getProductSpuId() {
        return productSpuId;
    }

    public void setProductSpuId(Long productSpuId) {
        this.productSpuId = productSpuId;
    }

    public String getProductSpuCode() {
        return productSpuCode;
    }

    public void setProductSpuCode(String productSpuCode) {
        this.productSpuCode = productSpuCode;
    }

    public String getProductSpuName() {
        return productSpuName;
    }

    public void setProductSpuName(String productSpuName) {
        this.productSpuName = productSpuName;
    }

    public Long getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(Long productSkuId) {
        this.productSkuId = productSkuId;
    }

    public String getProductSkuName() {
        return productSkuName;
    }

    public void setProductSkuName(String productSkuName) {
        this.productSkuName = productSkuName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getProductPicUrl() {
        return productPicUrl;
    }

    public void setProductPicUrl(String productPicUrl) {
        this.productPicUrl = productPicUrl;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Integer refundTime) {
        this.refundTime = refundTime;
    }

    public Integer getRejectCount() {
        return rejectCount;
    }

    public void setRejectCount(Integer rejectCount) {
        this.rejectCount = rejectCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
