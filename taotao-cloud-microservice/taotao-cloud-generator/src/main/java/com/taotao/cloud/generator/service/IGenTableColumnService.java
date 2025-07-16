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

package com.taotao.cloud.generator.service;

import com.taotao.cloud.generator.entity.GenTableColumn;
import java.util.List;

/**
 * 业务字段 服务层
 *
 * @author ruoyi
 */
public interface IGenTableColumnService {
    /**
     * 查询业务字段列表
     *
     * @param genTableColumn 业务字段信息
     * @return 业务字段集合
     */
    public List<GenTableColumn> selectGenTableColumnListByTableId(GenTableColumn genTableColumn);

    /**
     * 新增业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    public int insertGenTableColumn(GenTableColumn genTableColumn);

    /**
     * 修改业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    public int updateGenTableColumn(GenTableColumn genTableColumn);

    /**
     * 删除业务字段信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGenTableColumnByIds(String ids);
}
