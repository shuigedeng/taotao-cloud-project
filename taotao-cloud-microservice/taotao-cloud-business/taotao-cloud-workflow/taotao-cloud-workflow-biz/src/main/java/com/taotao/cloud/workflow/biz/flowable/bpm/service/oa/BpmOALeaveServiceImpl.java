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

package com.taotao.cloud.workflow.biz.flowable.bpm.service.oa;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.OA_LEAVE_NOT_EXISTS;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.oa.vo.BpmOALeavePageReqVO;
import com.taotao.cloud.flowable.biz.bpm.convert.oa.BpmOALeaveConvert;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.oa.BpmOALeaveDO;
import com.taotao.cloud.flowable.biz.bpm.dal.mysql.oa.BpmOALeaveMapper;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * OA 请假申请 Service 实现类
 *
 * @author jason
 * @author 芋道源码
 */
@Service
@Validated
public class BpmOALeaveServiceImpl implements BpmOALeaveService {

    /** OA 请假对应的流程定义 KEY */
    public static final String PROCESS_KEY = "oa_leave";

    @Resource
    private BpmOALeaveMapper leaveMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLeave(Long userId, BpmOALeaveCreateReqVO createReqVO) {
        // 插入 OA 请假单
        long day = LocalDateTimeUtil.between(createReqVO.getStartTime(), createReqVO.getEndTime())
                .toDays();
        BpmOALeaveDO leave = BpmOALeaveConvert.INSTANCE
                .convert(createReqVO)
                .setUserId(userId)
                .setDay(day)
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        leaveMapper.insert(leave);

        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("day", day);
        String processInstanceId = processInstanceApi.createProcessInstance(
                userId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables)
                        .setBusinessKey(String.valueOf(leave.getId())));

        // 将工作流的编号，更新到 OA 请假单中
        leaveMapper.updateById(new BpmOALeaveDO().setId(leave.getId()).setProcessInstanceId(processInstanceId));
        return leave.getId();
    }

    @Override
    public void updateLeaveResult(Long id, Integer result) {
        validateLeaveExists(id);
        leaveMapper.updateById(new BpmOALeaveDO().setId(id).setResult(result));
    }

    private void validateLeaveExists(Long id) {
        if (leaveMapper.selectById(id) == null) {
            throw exception(OA_LEAVE_NOT_EXISTS);
        }
    }

    @Override
    public BpmOALeaveDO getLeave(Long id) {
        return leaveMapper.selectById(id);
    }

    @Override
    public PageResult<BpmOALeaveDO> getLeavePage(Long userId, BpmOALeavePageReqVO pageReqVO) {
        return leaveMapper.selectPage(userId, pageReqVO);
    }
}
