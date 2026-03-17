package com.taotao.cloud.message.biz.infrastructure.austin.common.dto.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * <p>
 * 通知栏消息推送
 */
@Data

@AllArgsConstructor
@NoArgsConstructor
public class PushContentModel extends ContentModel {

    private String title;
    private String content;
    private String url;
}
