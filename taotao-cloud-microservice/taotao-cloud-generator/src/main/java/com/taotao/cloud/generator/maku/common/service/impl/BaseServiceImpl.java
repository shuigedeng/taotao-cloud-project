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

package com.taotao.cloud.generator.maku.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.BaseService;

/**
 * 基础服务类，所有Service都要继承
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T>
        implements BaseService<T> {

    /**
     * 获取分页对象
     *
     * @param query 分页参数
     */
    protected IPage<T> getPage(Query query) {
        Page<T> page = new Page<>(query.getPage(), query.getLimit());
        page.addOrder(OrderItem.desc("id"));
        return page;
    }

    protected QueryWrapper<T> getWrapper(Query query) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(query.getCode()), "code", query.getCode());
        wrapper.like(StrUtil.isNotBlank(query.getTableName()), "table_name", query.getTableName());
        wrapper.like(StrUtil.isNotBlank(query.getAttrType()), "attr_type", query.getAttrType());
        wrapper.like(
                StrUtil.isNotBlank(query.getColumnType()), "column_type", query.getColumnType());
        wrapper.like(StrUtil.isNotBlank(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StrUtil.isNotBlank(query.getDbType()), "db_type", query.getDbType());
        wrapper.like(
                StrUtil.isNotBlank(query.getProjectName()), "project_name", query.getProjectName());
        return wrapper;
    }
}
