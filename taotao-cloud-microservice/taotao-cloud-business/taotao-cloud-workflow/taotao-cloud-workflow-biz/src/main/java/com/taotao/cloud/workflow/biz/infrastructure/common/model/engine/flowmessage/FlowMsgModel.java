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

package com.taotao.cloud.workflow.biz.infrastructure.common.model.engine.flowmessage;

import com.taotao.cloud.workflow.biz.engine.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/** */
@Data
@NoArgsConstructor
public class FlowMsgModel {
    private String title;
    private FlowEngineEntity engine;
    private FlowTaskEntity taskEntity;
    private FlowTaskNodeEntity taskNodeEntity;
    private List<FlowTaskNodeEntity> nodeList;
    private List<FlowTaskOperatorEntity> operatorList;
    private List<FlowTaskCirculateEntity> circulateList;
    private Map<String, Object> data;
    // 代办 (通知代办)
    private boolean wait = true;
    // 同意
    private boolean approve = false;
    // 拒绝
    private boolean reject = false;
    // 抄送人
    private boolean copy = false;
    // 结束 (通知发起人)
    private boolean end = false;
    // 子流程通知
    private boolean launch = false;
    // 拒绝发起节点
    private boolean start = false;
}
