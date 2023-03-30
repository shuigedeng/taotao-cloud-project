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

package com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson;

import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.Properties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** 解析引擎 */
@Data
public class ChildNodeList {

    /** 节点属性 */
    private Properties properties = new Properties();
    /** 自定义属性 */
    private Custom custom = new Custom();
    /** 流程节点id */
    private String taskNodeId;
    /** 流程任务id */
    private String taskId;
    /** 下一级定时器属性 */
    private DateProperties timer = new DateProperties();
    /** 分流合流 */
    private String conditionType;
    /** 定时器所有 */
    private List<DateProperties> timerAll = new ArrayList<>();
    /** 分流的id */
    private String type;
    /** 当前节点总人数 */
    private Double total = 0.0;
}
