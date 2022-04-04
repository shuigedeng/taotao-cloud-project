package com.taotao.cloud.distribution.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分销订单
 *
 * 
 * @since 2020-03-14 23:04:56
 */
@Data
@ApiModel(value = "分销订单")
@TableName("li_distribution_order")
@NoArgsConstructor
public class DistributionOrder extends BaseIdEntity {

    private static final long serialVersionUID = 501799944909496507L;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description =  "创建时间", hidden = true)
    private Date createTime;

    /**
     * @see DistributionOrderStatusEnum
     */
    @Schema(description =  "分销订单状态")
    private String distributionOrderStatus;
    @Schema(description =  "购买会员的id")
    private String memberId;
    @Schema(description =  "购买会员的名称")
    private String memberName;
    @Schema(description =  "分销员id")
    private String distributionId;
    @Schema(description =  "分销员名称")
    private String distributionName;
    @Schema(description =  "解冻日期")
    private Date settleCycle;
    @Schema(description =  "提成金额")
    private BigDecimal rebate;
    @Schema(description =  "退款金额")
    private BigDecimal sellBackRebate;
    @Schema(description =  "店铺id")
    private String storeId;
    @Schema(description =  "店铺名称")
    private String storeName;
    @Schema(description =  "订单编号")
    private String orderSn;
    @Schema(description =  "子订单编号")
    private String orderItemSn;
    @Schema(description =  "商品ID")
    private String goodsId;
    @Schema(description =  "商品名称")
    private String goodsName;
    @Schema(description =  "货品ID")
    private String skuId;
    @Schema(description =  "规格")
    private String specs;
    @Schema(description =  "图片")
    private String image;
    @Schema(description =  "商品数量")
    private Integer num;

    public DistributionOrder(StoreFlow storeFlow) {
        distributionOrderStatus = DistributionOrderStatusEnum.WAIT_BILL.name();
        memberId = storeFlow.getMemberId();
        memberName = storeFlow.getMemberName();
        rebate = storeFlow.getDistributionRebate();
        storeId = storeFlow.getStoreId();
        storeName = storeFlow.getStoreName();
        orderSn = storeFlow.getOrderSn();
        orderItemSn = storeFlow.getOrderItemSn();
        goodsId = storeFlow.getGoodsId();
        goodsName = storeFlow.getGoodsName();
        skuId = storeFlow.getSkuId();
        specs = storeFlow.getSpecs();
        image = storeFlow.getImage();
        num = storeFlow.getNum();
    }

}
