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

package com.taotao.cloud.workflow.biz.common.model.engine.flowmessage;

import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode.MsgConfig;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowMessageEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowMessageModel {
    private String title = "";
    private Integer type = FlowMessageEnum.wait.getCode();
    private Integer status;
    private MsgConfig msgConfig = new MsgConfig();
    private List<String> userList = new ArrayList<>();
    private Map<String, Object> data = new HashMap<>();
    private Map<String, String> contMsg = new HashMap<>();
    private String fullName;
    private FlowTaskOperatorRecordEntity recordEntity;
}
