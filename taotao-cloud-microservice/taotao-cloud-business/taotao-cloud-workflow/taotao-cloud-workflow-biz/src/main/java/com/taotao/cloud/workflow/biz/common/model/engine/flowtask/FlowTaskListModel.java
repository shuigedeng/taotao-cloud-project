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

package com.taotao.cloud.workflow.biz.common.model.engine.flowtask;

import java.util.Date;
import lombok.Data;

@Data
public class FlowTaskListModel {

    private String id;
    private String processId;
    private String enCode;
    private String fullName;
    private Integer flowUrgent;
    private String flowId;
    private String flowCode;
    private String flowName;
    private String flowCategory;
    private String flowType;
    private Date startTime;
    private Date endTime;
    private String thisStep;
    private String thisStepId;
    private Integer status;
    private Integer completion;
    private Long creatorUserId;
    private Date creatorTime;
    private String handleId;
    private String nodeName;
    private String lastModifyUserId;
    private Date lastModifyTime;
    private String approversProperties;
    private String description;
    private String flowVersion;
}
