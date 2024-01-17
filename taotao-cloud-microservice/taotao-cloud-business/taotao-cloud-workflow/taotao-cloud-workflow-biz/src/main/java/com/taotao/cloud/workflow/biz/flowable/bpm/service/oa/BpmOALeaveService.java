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

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.oa.vo.BpmOALeavePageReqVO;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.oa.BpmOALeaveDO;
import jakarta.validation.Valid;

/**
 * 请假申请 Service 接口
 *
 * @author jason
 * @author 芋道源码
 */
public interface BpmOALeaveService {

    /**
     * 创建请假申请
     *
     * @param userId 用户编号
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLeave(Long userId, @Valid BpmOALeaveCreateReqVO createReqVO);

    /**
     * 更新请假申请的状态
     *
     * @param id 编号
     * @param result 结果
     */
    void updateLeaveResult(Long id, Integer result);

    /**
     * 获得请假申请
     *
     * @param id 编号
     * @return 请假申请
     */
    BpmOALeaveDO getLeave(Long id);

    /**
     * 获得请假申请分页
     *
     * @param userId 用户编号
     * @param pageReqVO 分页查询
     * @return 请假申请分页
     */
    PageResult<BpmOALeaveDO> getLeavePage(Long userId, BpmOALeavePageReqVO pageReqVO);
}
