package com.taotao.cloud.message.biz.austin.common.dto.model;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * <p>
 * 短信内容模型
 * <p>
 * 在前端填写的时候分开，但最后处理的时候会将url拼接在content上
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class SmsContentModel extends ContentModel {

    /**
     * 短信发送内容
     */
    private String content;

    /**
     * 短信发送链接
     */
    private String url;

}
