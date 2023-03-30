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

package com.taotao.cloud.workflow.biz.common.model.engine;

import java.util.List;
import java.util.Map;
import lombok.Data;

/** */
@Data
public class FlowHandleModel extends Pagination {
    /** 意见* */
    private String handleOpinion;
    /** 指定人* */
    private String freeApproverUserId;
    /** 表单数据* */
    private Map<String, Object> formData;
    /** 编码* */
    private String enCode;
    /** 自定义抄送人* */
    private String copyIds;
    /** 签名* */
    private String signImg;
    /** 指派节点* */
    private String nodeCode;
    /** 候选人 */
    private Map<String, List<String>> candidateList;
    /** 批量审批id */
    private List<String> ids;
    /** 批量审批类型 0.通过 1.拒绝 2.转办 */
    private Integer batchType;
}
