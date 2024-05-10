package com.taotao.cloud.design.patterns.pipeline.demo;

import lombok.Data;

/**
 * 演示回参
 *
 * @author 
 * @date 2023/08/06 16:33
 */
@Data
public class DemoResp {
    /**
     * 成功标识
     */
    private Boolean success = false;

    /**
     * 结果信息
     */
    private String resultMsg;

    /**
     * 构造方法
     *
     * @param message 消息
     * @return {@link DemoResp}
     */
    public static DemoResp buildRes(String message) {
        DemoResp response = new DemoResp();
        response.setResultMsg(message);
        return response;
    }
}
