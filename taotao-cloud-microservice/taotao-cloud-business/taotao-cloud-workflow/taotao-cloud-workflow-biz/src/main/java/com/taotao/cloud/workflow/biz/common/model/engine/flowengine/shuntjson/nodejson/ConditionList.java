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

import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.ProperCond;
import java.util.List;
import lombok.Data;

/** 解析引擎 */
@Data
public class ConditionList {
    /** 条件* */
    private List<ProperCond> conditions;
    /** 条件节点id* */
    private String nodeId;
    /** 上一节点id* */
    private String prevId;
    /** 1.先判断分流节点 2.在判断孩子节点 3.最后获取子节点* */
    /** 判断是否有分流节点* */
    private Boolean flow = false;
    /** 分流的节点id* */
    private String flowId;
    /** 判断是否有子节点* */
    private Boolean child;
    /** 条件成功id* */
    private String childNodeId;
    /** 子节点id* */
    private String firstId;
    /** 判断是否其他条件* */
    private Boolean isDefault;
    /** 名称* */
    private String title;
}
