package com.taotao.cloud.message.biz.austin.common.dto.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * <p>
 * 通知栏消息推送
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class PushContentModel extends ContentModel {

    private String title;
    private String content;
    private String url;
}
