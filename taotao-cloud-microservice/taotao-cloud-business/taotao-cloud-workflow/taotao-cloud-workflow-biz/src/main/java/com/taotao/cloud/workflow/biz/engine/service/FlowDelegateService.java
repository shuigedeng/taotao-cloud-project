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

package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.common.base.Pagination;
import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import java.util.List;

/** 流程委托 */
public interface FlowDelegateService extends IService<FlowDelegateEntity> {

    /**
     * 列表
     *
     * @param pagination 请求参数
     * @return
     */
    List<FlowDelegateEntity> getList(Pagination pagination);

    /**
     * 列表
     *
     * @return
     */
    List<FlowDelegateEntity> getList();

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    FlowDelegateEntity getInfo(String id);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(FlowDelegateEntity entity);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(FlowDelegateEntity entity);

    /**
     * 获取委托的表单
     *
     * @param userId 被委托人
     * @return
     */
    List<FlowDelegateEntity> getUser(String userId);

    /**
     * 获取委托的表单
     *
     * @param userId 被委托人
     * @param flowId 流程引擎
     * @param creatorUserId 创建人
     * @return
     */
    List<FlowDelegateEntity> getUser(String userId, String flowId, String creatorUserId);

    /**
     * 更新
     *
     * @param id 主键值
     * @param entity 实体对象
     * @return
     */
    boolean update(String id, FlowDelegateEntity entity);
}
