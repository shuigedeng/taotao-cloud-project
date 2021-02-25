package com.taotao.cloud.order.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 售后退款操作记录表
 *
 * @author dengtao
 * @date 2020/4/30 15:49
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Table(name = "tt_order_refund_req_record")
@org.hibernate.annotations.Table(appliesTo = "tt_order_refund_req_record", comment = "售后退款操作记录表")
public class OrderRefundReqRecord extends BaseEntity {

    private String orderCode;

    private String itemCode;

    /**
     * 标题
     */
    private String title;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人名称
     */
    private String createName;

    /**
     * 操作人名称
     */
    private Long createId;

    /**
     * 操作人昵称
     */
    private String createNick;

    /**
     * 扩展信息
     */
    private String ext;

    private Short reqRecordType;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

}
