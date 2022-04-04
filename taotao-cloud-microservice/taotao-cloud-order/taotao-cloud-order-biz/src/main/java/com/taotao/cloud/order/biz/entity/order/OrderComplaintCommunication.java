package com.taotao.cloud.order.biz.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 订单交易投诉通信表
 *
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = OrderComplaintCommunication.TABLE_NAME)
@TableName(OrderComplaintCommunication.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderComplaintCommunication.TABLE_NAME, comment = "订单交易投诉通信表")
public class OrderComplaintCommunication extends BaseSuperEntity<OrderInfo, Long> {

	public static final String TABLE_NAME = "tt_order_complaint_communication";

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
