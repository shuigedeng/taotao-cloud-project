package com.taotao.cloud.message.biz.austin.common.dto.model;

import lombok.*;

/**
 * 
 * <p>
 * <p>
 * 邮件消息体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailContentModel extends ContentModel {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容(可写入HTML)
     */
    private String content;


}
