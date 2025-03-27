package com.taotao.cloud.message.biz.austin.web.vo.amis;


import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shuigedeng
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class SmsTimeLineVo {

    /**
     * items
     */
    private List<ItemsVO> items;

    /**
     * ItemsVO
     */
    @Data
    @Accessors(chain=true)
    public static class ItemsVO {
        /**
         * 业务ID
         */
        private String businessId;
        /**
         * detail 发送内容
         */
        private String content;

        /**
         * 发送状态
         */
        private String sendType;

        /**
         * 回执状态
         */
        private String receiveType;

        /**
         * 回执报告
         */
        private String receiveContent;

        /**
         * 发送时间
         */
        private String sendTime;

        /**
         * 回执时间
         */
        private String receiveTime;


    }
}
