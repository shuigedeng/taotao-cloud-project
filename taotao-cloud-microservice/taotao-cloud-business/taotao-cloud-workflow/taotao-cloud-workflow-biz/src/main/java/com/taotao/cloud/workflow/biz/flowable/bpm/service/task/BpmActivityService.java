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

package com.taotao.cloud.workflow.biz.flowable.bpm.service.task;

import com.taotao.cloud.flowable.biz.bpm.controller.admin.task.vo.activity.BpmActivityRespVO;
import java.util.List;
import org.flowable.engine.history.HistoricActivityInstance;

/**
 * BPM 活动实例 Service 接口
 *
 * @author 芋道源码
 */
public interface BpmActivityService {

    /**
     * 获得指定流程实例的活动实例列表
     *
     * @param processInstanceId 流程实例的编号
     * @return 活动实例列表
     */
    List<BpmActivityRespVO> getActivityListByProcessInstanceId(String processInstanceId);

    /**
     * 获得执行编号对应的活动实例
     *
     * @param executionId 执行编号
     * @return 活动实例
     */
    List<HistoricActivityInstance> getHistoricActivityListByExecutionId(String executionId);
}
