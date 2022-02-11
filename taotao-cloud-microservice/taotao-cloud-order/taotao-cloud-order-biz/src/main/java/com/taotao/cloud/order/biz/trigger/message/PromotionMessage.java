package com.taotao.cloud.order.biz.trigger.message;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息队列传输促销信息实体
 *
 * @author paulG
 * @since 2020/10/30
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionMessage {

    /**
     * 促销id
     */
    private String promotionId;
    /**
     * 促销类型
     */
    private String promotionType;

    /**
     * 促销状态
     */
    private String promotionStatus;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
