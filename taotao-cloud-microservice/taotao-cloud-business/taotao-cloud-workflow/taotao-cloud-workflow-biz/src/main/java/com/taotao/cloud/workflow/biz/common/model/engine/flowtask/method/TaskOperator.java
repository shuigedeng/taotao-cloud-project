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

package com.taotao.cloud.workflow.biz.common.model.engine.flowtask.method;

import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.nodejson.ChildNodeList;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import lombok.Data;
import lombok.experimental.*;

/** */
@Data
public class TaskOperator {
    /** 当前节点数据 */
    private ChildNodeList childNode;
    /** 引擎实例 */
    private FlowTaskEntity taskEntity;
    /** 提交数据 */
    private FlowModel flowModel;
    /** true记录 false不记录 */
    private Boolean details;
    /** 经办id */
    private String id;
}
