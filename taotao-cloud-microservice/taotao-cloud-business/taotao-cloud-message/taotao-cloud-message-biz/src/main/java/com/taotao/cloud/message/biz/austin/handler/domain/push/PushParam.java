package com.taotao.cloud.message.biz.austin.handler.domain.push;


import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * push的参数
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class PushParam {

    /**
     * 调用 接口时需要的token
     */
    private String token;

    /**
     * 调用接口时需要的appId
     */
    private String appId;

    /**
     * 消息模板的信息
     */
    private TaskInfo taskInfo;

}
