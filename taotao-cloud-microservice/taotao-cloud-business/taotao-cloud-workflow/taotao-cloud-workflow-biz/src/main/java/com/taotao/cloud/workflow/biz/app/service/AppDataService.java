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

package com.taotao.cloud.workflow.biz.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import taotao.cloud.workflow.biz.engine.model.flowengine.FlowPagination;
import taotao.cloud.workflow.biz.entity.AppDataEntity;
import taotao.cloud.workflow.biz.model.AppDataListAllVO;
import taotao.cloud.workflow.biz.model.AppFlowListAllVO;

/**
 * app常用数据
 *
 * @author 
 * 
 *  
 * @since 2021-08-08
 */
public interface AppDataService extends IService<AppDataEntity> {

    /**
     * 列表
     *
     * @param type 类型
     * @return
     */
    List<AppDataEntity> getList(String type);

    /**
     * 列表
     *
     * @return
     */
    List<AppDataEntity> getList();

    /**
     * 信息
     *
     * @param objectId 对象主键
     * @return
     */
    AppDataEntity getInfo(String objectId);

    /**
     * 验证名称
     *
     * @param objectId 对象主键
     * @return
     */
    boolean isExistByObjectId(String objectId);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(AppDataEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(AppDataEntity entity);

    /**
     * 删除
     *
     * @param objectId 应用主键
     */
    void delete(String objectId);

    /**
     * 流程所有应用
     *
     * @param pagination
     * @return
     */
    List<AppFlowListAllVO> getFlowList(FlowPagination pagination);

    /**
     * 流程所有应用
     *
     * @param type 类型
     * @return
     */
    List<AppDataListAllVO> getDataList(String type);
}
