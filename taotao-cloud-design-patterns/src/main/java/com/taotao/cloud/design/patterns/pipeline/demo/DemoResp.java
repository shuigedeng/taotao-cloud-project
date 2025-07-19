/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
