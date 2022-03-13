package com.taotao.cloud.order.biz.entity.order;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 交易投诉通信
 *
 * 
 * @since 2020/12/5
 **/
@Data
@TableName("li_order_complaint_communication")
@ApiModel(value = "订单交易投诉通信")
@AllArgsConstructor
@NoArgsConstructor
public class OrderComplaintCommunication extends BaseEntity {

    private static final long serialVersionUID = -2384351827382795547L;

    /**
     * 投诉id
     */
    @Schema(description =  "投诉id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String complainId;
    /**
     * 对话内容
     */
    @Schema(description =  "对话内容")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String content;
    /**
     * 所属，买家/卖家
     */
    @Schema(description =  "所属，买家/卖家")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String owner;
    /**
     * 对话所属名称
     */
    @Schema(description =  "对话所属名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String ownerName;
    /**
     * 对话所属id,卖家id/买家id
     */
    @Schema(description =  "对话所属id,卖家id/买家id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String ownerId;



}
