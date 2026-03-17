package com.taotao.cloud.message.biz.infrastructure.austin.cron.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 每一行csv的记录
 *
 * @author shuigedeng
 * @date 2022/2/9
 */
@Data

@AllArgsConstructor
@NoArgsConstructor

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
