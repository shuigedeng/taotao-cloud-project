package com.taotao.cloud.message.biz.austin.web.vo;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


/**
 * 消息模板的Vo
 *
 * @author shuigedeng
 */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateVo {
    /**
     * 返回List列表
     */
    private List<Map<String, Object>> rows;

    /**
     * 总条数
     */
    private Long count;
}
