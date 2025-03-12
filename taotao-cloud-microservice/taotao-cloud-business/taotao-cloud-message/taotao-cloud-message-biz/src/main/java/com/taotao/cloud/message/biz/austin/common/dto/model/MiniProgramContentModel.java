package com.taotao.cloud.message.biz.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author shuigedeng
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class MiniProgramContentModel extends ContentModel {
    /**
     * 模板消息发送的数据
     */
    private Map<String, String> miniProgramParam;

    /**
     * 模板Id
     */
    private String templateId;

    /**
     * 跳转链接
     */
    private String page;

}
