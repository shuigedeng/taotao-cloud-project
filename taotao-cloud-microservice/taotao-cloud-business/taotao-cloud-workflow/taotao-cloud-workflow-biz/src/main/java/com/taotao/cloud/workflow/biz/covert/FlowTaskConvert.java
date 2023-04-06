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

package com.taotao.cloud.workflow.biz.covert;

import com.taotao.cloud.workflow.biz.common.model.engine.FlowHandleModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowbefore.FlowBeforeListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowcomment.FlowCommentListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowdelegate.FlowDelegateCrForm;
import com.taotao.cloud.workflow.biz.common.model.engine.flowdelegate.FlowDelegateInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowEngineListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.common.model.engine.flowlaunch.FlowLaunchListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowmonitor.FlowMonitorListVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskInfoVO;
import com.taotao.cloud.workflow.biz.common.model.engine.flowtask.FlowTaskListModel;
import com.taotao.cloud.workflow.biz.engine.entity.FlowCommentEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * DeptMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 13:39:18
 */
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlowTaskConvert {

    /** 实例 */
    FlowTaskConvert INSTANCE = Mappers.getMapper(FlowTaskConvert.class);

    FlowBeforeListVO convert(FlowTaskListModel flowTaskListModel);

    List<FlowEngineListVO> convert(List<FlowEngineEntity> list);

    FlowDelegateInfoVO convert(FlowDelegateEntity flowDelegateEntity);

    FlowDelegateEntity convert(FlowDelegateCrForm flowDelegateCrForm);

    List<FlowCommentListVO> convertComment(List<FlowCommentEntity> list);

    FlowCommentEntity convert(FlowCommentForm flowCommentForm);

    FlowCommentInfoVO convert(FlowCommentEntity flowCommentForm);

    FlowLaunchListVO convertLaunch(FlowTaskEntity flowCommentForm);

    FlowTaskInfoVO convert(FlowTaskEntity flowCommentForm);

    FlowMonitorListVO convertMonitor(FlowTaskEntity flowCommentForm);

    FlowModel convert(FlowHandleModel flowCommentForm);
}
