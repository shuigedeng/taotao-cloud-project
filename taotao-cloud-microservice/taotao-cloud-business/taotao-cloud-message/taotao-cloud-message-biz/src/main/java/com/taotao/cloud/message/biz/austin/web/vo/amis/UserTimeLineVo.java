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
public class UserTimeLineVo {

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
         * title 模板名称
         */
        private String title;
        /**
         * detail 发送细节
         */
        private String detail;

        /**
         * 发送类型
         */
        private String sendType;

        /**
         * 模板创建者
         */
        private String creator;

    }
}
