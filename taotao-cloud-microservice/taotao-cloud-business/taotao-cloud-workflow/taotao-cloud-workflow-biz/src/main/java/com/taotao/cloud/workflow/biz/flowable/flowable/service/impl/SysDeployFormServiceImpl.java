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

import com.ruoyi.flowable.service.ISysDeployFormService;
import com.ruoyi.system.domain.SysDeployForm;
import com.ruoyi.system.domain.SysForm;
import com.ruoyi.system.mapper.SysDeployFormMapper;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程实例关联表单Service业务层处理
 *
 * @author Tony
 * @since 2021-04-03
 */
@Service
public class SysDeployFormServiceImpl implements ISysDeployFormService {
    @Autowired
    private SysDeployFormMapper sysDeployFormMapper;

    /**
     * 查询流程实例关联表单
     *
     * @param id 流程实例关联表单ID
     * @return 流程实例关联表单
     */
    @Override
    public SysDeployForm selectSysDeployFormById(Long id) {
        return sysDeployFormMapper.selectSysDeployFormById(id);
    }

    /**
     * 查询流程实例关联表单列表
     *
     * @param sysDeployForm 流程实例关联表单
     * @return 流程实例关联表单
     */
    @Override
    public List<SysDeployForm> selectSysDeployFormList(SysDeployForm sysDeployForm) {
        return sysDeployFormMapper.selectSysDeployFormList(sysDeployForm);
    }

    /**
     * 新增流程实例关联表单
     *
     * @param sysDeployForm 流程实例关联表单
     * @return 结果
     */
    @Override
    public int insertSysDeployForm(SysDeployForm sysDeployForm) {
        SysForm sysForm = sysDeployFormMapper.selectSysDeployFormByDeployId(sysDeployForm.getDeployId());
        if (Objects.isNull(sysForm)) {
            return sysDeployFormMapper.insertSysDeployForm(sysDeployForm);
        } else {
            return 1;
        }
    }

    /**
     * 修改流程实例关联表单
     *
     * @param sysDeployForm 流程实例关联表单
     * @return 结果
     */
    @Override
    public int updateSysDeployForm(SysDeployForm sysDeployForm) {
        return sysDeployFormMapper.updateSysDeployForm(sysDeployForm);
    }

    /**
     * 批量删除流程实例关联表单
     *
     * @param ids 需要删除的流程实例关联表单ID
     * @return 结果
     */
    @Override
    public int deleteSysDeployFormByIds(Long[] ids) {
        return sysDeployFormMapper.deleteSysDeployFormByIds(ids);
    }

    /**
     * 删除流程实例关联表单信息
     *
     * @param id 流程实例关联表单ID
     * @return 结果
     */
    @Override
    public int deleteSysDeployFormById(Long id) {
        return sysDeployFormMapper.deleteSysDeployFormById(id);
    }

    /**
     * 查询流程挂着的表单
     *
     * @param deployId
     * @return
     */
    @Override
    public SysForm selectSysDeployFormByDeployId(String deployId) {
        return sysDeployFormMapper.selectSysDeployFormByDeployId(deployId);
    }
}
