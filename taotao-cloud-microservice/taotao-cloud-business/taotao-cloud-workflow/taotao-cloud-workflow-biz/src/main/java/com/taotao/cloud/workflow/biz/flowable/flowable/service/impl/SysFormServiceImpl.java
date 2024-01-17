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

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.flowable.service.ISysFormService;
import com.ruoyi.system.domain.SysForm;
import com.ruoyi.system.mapper.SysFormMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程表单Service业务层处理
 *
 * @author Tony
 * @since 2021-04-03
 */
@Service
public class SysFormServiceImpl implements ISysFormService {
    @Autowired
    private SysFormMapper sysFormMapper;

    /**
     * 查询流程表单
     *
     * @param formId 流程表单ID
     * @return 流程表单
     */
    @Override
    public SysForm selectSysFormById(Long formId) {
        return sysFormMapper.selectSysFormById(formId);
    }

    /**
     * 查询流程表单列表
     *
     * @param sysForm 流程表单
     * @return 流程表单
     */
    @Override
    public List<SysForm> selectSysFormList(SysForm sysForm) {
        return sysFormMapper.selectSysFormList(sysForm);
    }

    /**
     * 新增流程表单
     *
     * @param sysForm 流程表单
     * @return 结果
     */
    @Override
    public int insertSysForm(SysForm sysForm) {
        sysForm.setCreateTime(DateUtils.getNowDate());
        return sysFormMapper.insertSysForm(sysForm);
    }

    /**
     * 修改流程表单
     *
     * @param sysForm 流程表单
     * @return 结果
     */
    @Override
    public int updateSysForm(SysForm sysForm) {
        sysForm.setUpdateTime(DateUtils.getNowDate());
        return sysFormMapper.updateSysForm(sysForm);
    }

    /**
     * 批量删除流程表单
     *
     * @param formIds 需要删除的流程表单ID
     * @return 结果
     */
    @Override
    public int deleteSysFormByIds(Long[] formIds) {
        return sysFormMapper.deleteSysFormByIds(formIds);
    }

    /**
     * 删除流程表单信息
     *
     * @param formId 流程表单ID
     * @return 结果
     */
    @Override
    public int deleteSysFormById(Long formId) {
        return sysFormMapper.deleteSysFormById(formId);
    }
}
