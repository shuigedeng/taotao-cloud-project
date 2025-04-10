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

package com.taotao.cloud.workflow.biz.common.flowable.client.vo;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.*;

/**
 *
 */
@Data
public class DefinitionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2308324118562026040L;
    /** 流程定义id */
    private String definitionId;

    /** 流程名称 */
    private String processName;

    /** 流程key */
    private String processKey;

    /** 部署id */
    private String deploymentId;

    /** 流程定义状态 1激活 2中止 */
    private Boolean suspended;
}
