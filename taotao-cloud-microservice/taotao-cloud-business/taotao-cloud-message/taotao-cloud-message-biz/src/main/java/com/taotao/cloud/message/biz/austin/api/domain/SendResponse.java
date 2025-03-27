package com.taotao.cloud.message.biz.austin.api.domain;

import com.taotao.cloud.message.biz.austin.common.domain.SimpleTaskInfo;
import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;
import lombok.experimental.*;

import java.util.List;


/**
 * 发送接口返回值
 *
 * @author shuigedeng
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SendResponse {
    /**
     * 响应状态
     */
    private String code;
    /**
     * 响应编码
     */
    private String msg;

    /**
     * 实际发送任务列表
     */
    private List<SimpleTaskInfo> data;

}
