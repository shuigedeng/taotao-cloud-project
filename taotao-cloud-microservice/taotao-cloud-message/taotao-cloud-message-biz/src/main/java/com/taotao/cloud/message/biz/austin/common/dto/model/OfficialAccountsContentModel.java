package com.taotao.cloud.message.biz.austin.common.dto.model;

import lombok.*;

import java.util.Map;

/**
 * 
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficialAccountsContentModel extends ContentModel {

    /**
     * 模板消息发送的数据
     */
    Map<String, String> map;

    /**
     * 模板消息跳转的url
     */
    String url;

}
