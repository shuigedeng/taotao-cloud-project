package com.taotao.cloud.message.biz.austin.cron.vo;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 每一行csv的记录
 * @author 3y
 * @date 2022/2/9
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrowdInfoVo implements Serializable {

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 接收者id
     */
    private String receiver;

    /**
     * 参数信息
     */
    private Map<String, String> params;
}
