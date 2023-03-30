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

package com.taotao.cloud.workflow.biz.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.workflow.biz.form.entity.WorkContactSheetEntity;
import java.util.List;
import java.util.Map;

/** 工作联系单 */
public interface WorkContactSheetService extends IService<WorkContactSheetEntity> {

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    WorkContactSheetEntity getInfo(String id);

    /**
     * 保存
     *
     * @param id 主键值
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void save(String id, WorkContactSheetEntity entity) throws WorkFlowException;

    /**
     * 提交
     *
     * @param id 主键值
     * @param entity 实体对象
     * @throws WorkFlowException 异常
     */
    void submit(String id, WorkContactSheetEntity entity, Map<String, List<String>> candidateList)
            throws WorkFlowException;

    /**
     * 更改数据
     *
     * @param id 主键值
     * @param data 实体对象
     */
    void data(String id, String data);
}
