package com.taotao.cloud.message.biz.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * <p>
 * <p>
 * 邮件消息体
 */
@Data
@Accessors(chain=true)
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

    /**
     * 邮件附件链接
     */
    private String url;
}
