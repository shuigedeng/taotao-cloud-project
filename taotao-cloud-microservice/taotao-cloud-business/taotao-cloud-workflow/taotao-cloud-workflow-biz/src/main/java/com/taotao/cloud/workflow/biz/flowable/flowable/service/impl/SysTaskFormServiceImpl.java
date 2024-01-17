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

package com.taotao.cloud.workflow.biz.flowable.flowable.service.impl;

import com.ruoyi.flowable.service.ISysTaskFormService;
import com.ruoyi.system.domain.SysTaskForm;
import com.ruoyi.system.mapper.SysTaskFormMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程任务关联单Service业务层处理
 *
 * @author Tony
 * @since 2021-04-03
 */
@Service
public class SysTaskFormServiceImpl implements ISysTaskFormService {
    @Autowired
    private SysTaskFormMapper sysTaskFormMapper;

    /**
     * 查询流程任务关联单
     *
     * @param id 流程任务关联单ID
     * @return 流程任务关联单
     */
    @Override
    public SysTaskForm selectSysTaskFormById(Long id) {
        return sysTaskFormMapper.selectSysTaskFormById(id);
    }

    /**
     * 查询流程任务关联单列表
     *
     * @param sysTaskForm 流程任务关联单
     * @return 流程任务关联单
     */
    @Override
    public List<SysTaskForm> selectSysTaskFormList(SysTaskForm sysTaskForm) {
        return sysTaskFormMapper.selectSysTaskFormList(sysTaskForm);
    }

    /**
     * 新增流程任务关联单
     *
     * @param sysTaskForm 流程任务关联单
     * @return 结果
     */
    @Override
    public int insertSysTaskForm(SysTaskForm sysTaskForm) {
        return sysTaskFormMapper.insertSysTaskForm(sysTaskForm);
    }

    /**
     * 修改流程任务关联单
     *
     * @param sysTaskForm 流程任务关联单
     * @return 结果
     */
    @Override
    public int updateSysTaskForm(SysTaskForm sysTaskForm) {
        return sysTaskFormMapper.updateSysTaskForm(sysTaskForm);
    }

    /**
     * 批量删除流程任务关联单
     *
     * @param ids 需要删除的流程任务关联单ID
     * @return 结果
     */
    @Override
    public int deleteSysTaskFormByIds(Long[] ids) {
        return sysTaskFormMapper.deleteSysTaskFormByIds(ids);
    }

    /**
     * 删除流程任务关联单信息
     *
     * @param id 流程任务关联单ID
     * @return 结果
     */
    @Override
    public int deleteSysTaskFormById(Long id) {
        return sysTaskFormMapper.deleteSysTaskFormById(id);
    }
}
