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

package com.taotao.cloud.workflow.biz.flowable.bpm.service.definition;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.form.BpmFormPageReqVO;
import com.taotao.cloud.flowable.biz.bpm.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import com.taotao.cloud.flowable.biz.bpm.dal.dataobject.definition.BpmFormDO;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

/**
 * 动态表单 Service 接口
 *
 * @author @风里雾里
 */
public interface BpmFormService {

    /**
     * 创建动态表单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createForm(@Valid BpmFormCreateReqVO createReqVO);

    /**
     * 更新动态表单
     *
     * @param updateReqVO 更新信息
     */
    void updateForm(@Valid BpmFormUpdateReqVO updateReqVO);

    /**
     * 删除动态表单
     *
     * @param id 编号
     */
    void deleteForm(Long id);

    /**
     * 获得动态表单
     *
     * @param id 编号
     * @return 动态表单
     */
    BpmFormDO getForm(Long id);

    /**
     * 获得动态表单列表
     *
     * @return 动态表单列表
     */
    List<BpmFormDO> getFormList();

    /**
     * 获得动态表单列表
     *
     * @param ids 编号
     * @return 动态表单列表
     */
    List<BpmFormDO> getFormList(Collection<Long> ids);

    /**
     * 获得动态表单 Map
     *
     * @param ids 编号
     * @return 动态表单 Map
     */
    default Map<Long, BpmFormDO> getFormMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return CollectionUtils.convertMap(this.getFormList(ids), BpmFormDO::getId);
    }

    /**
     * 获得动态表单分页
     *
     * @param pageReqVO 分页查询
     * @return 动态表单分页
     */
    PageResult<BpmFormDO> getFormPage(BpmFormPageReqVO pageReqVO);

    /**
     * 校验流程表单已配置
     *
     * @param configStr configStr 字段
     * @return 流程表单
     */
    BpmFormDO checkFormConfig(String configStr);
}
