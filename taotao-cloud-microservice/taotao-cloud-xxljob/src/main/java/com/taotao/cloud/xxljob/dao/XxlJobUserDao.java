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

package com.taotao.cloud.xxljob.dao;

import com.taotao.cloud.xxljob.core.model.XxlJobUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */
@Mapper
public interface XxlJobUserDao {

    public List<XxlJobUser> pageList(
            @Param("offset") int offset,
            @Param("pagesize") int pagesize,
            @Param("username") String username,
            @Param("role") int role);

    public int pageListCount(
            @Param("offset") int offset,
            @Param("pagesize") int pagesize,
            @Param("username") String username,
            @Param("role") int role);

    public XxlJobUser loadByUserName(@Param("username") String username);

    public int save(XxlJobUser xxlJobUser);

    public int update(XxlJobUser xxlJobUser);

    public int delete(@Param("id") int id);
}
