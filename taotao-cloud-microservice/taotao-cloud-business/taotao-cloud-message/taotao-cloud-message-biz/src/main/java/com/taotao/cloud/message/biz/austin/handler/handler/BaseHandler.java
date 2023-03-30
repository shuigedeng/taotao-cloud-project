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

package com.taotao.cloud.message.biz.austin.handler.handler;

import com.taotao.cloud.message.biz.austin.common.domain.AnchorInfo;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.enums.AnchorState;
import com.taotao.cloud.message.biz.austin.handler.flowcontrol.FlowControlFactory;
import com.taotao.cloud.message.biz.austin.handler.flowcontrol.FlowControlParam;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 3y 发送各个渠道的handler
 */
public abstract class BaseHandler implements Handler {

    @Autowired private HandlerHolder handlerHolder;
    @Autowired private LogUtils logUtils;
    @Autowired private FlowControlFactory flowControlFactory;

    /** 标识渠道的Code 子类初始化的时候指定 */
    protected Integer channelCode;

    /** 限流相关的参数 子类初始化的时候指定 */
    protected FlowControlParam flowControlParam;

    /** 初始化渠道与Handler的映射关系 */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    /**
     * 流量控制
     *
     * @param taskInfo
     */
    public void flowControl(TaskInfo taskInfo) {
        // 只有子类指定了限流参数，才需要限流
        if (Objects.nonNull(flowControlParam)) {
            flowControlFactory.flowControl(taskInfo, flowControlParam);
        }
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {
        flowControl(taskInfo);
        if (handler(taskInfo)) {
            logUtils.print(
                    AnchorInfo.builder()
                            .state(AnchorState.SEND_SUCCESS.getCode())
                            .businessId(taskInfo.getBusinessId())
                            .ids(taskInfo.getReceiver())
                            .build());
            return;
        }
        logUtils.print(
                AnchorInfo.builder()
                        .state(AnchorState.SEND_FAIL.getCode())
                        .businessId(taskInfo.getBusinessId())
                        .ids(taskInfo.getReceiver())
                        .build());
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);
}
