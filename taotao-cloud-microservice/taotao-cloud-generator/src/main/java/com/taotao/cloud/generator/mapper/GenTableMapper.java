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

package com.taotao.cloud.generator.mapper;

import com.taotao.cloud.generator.entity.GenTable;
import java.util.List;

/**
 * 业务 数据层
 *
 * @author ruoyi
 */
public interface GenTableMapper {
    /**
     * 查询业务列表
     *
     * @param genTable 业务信息
     * @return 业务集合
     */
    public List<GenTable> selectGenTableList(GenTable genTable);

    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    public List<GenTable> selectDbTableList(GenTable genTable);

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    public List<GenTable> selectDbTableListByNames(String[] tableNames);

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    public List<GenTable> selectGenTableAll();

    /**
     * 查询表ID业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    public GenTable selectGenTableById(Long id);

    /**
     * 查询表名称业务信息
     *
     * @param tableName 表名称
     * @return 业务信息
     */
    public GenTable selectGenTableByName(String tableName);

    /**
     * 新增业务
     *
     * @param genTable 业务信息
     * @return 结果
     */
    public int insertGenTable(GenTable genTable);

    /**
     * 修改业务
     *
     * @param genTable 业务信息
     * @return 结果
     */
    public int updateGenTable(GenTable genTable);

    /**
     * 批量删除业务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGenTableByIds(Long[] ids);

    /**
     * 创建表
     *
     * @param sql
     * @return 结果
     */
    public int createTable(String sql);
}
