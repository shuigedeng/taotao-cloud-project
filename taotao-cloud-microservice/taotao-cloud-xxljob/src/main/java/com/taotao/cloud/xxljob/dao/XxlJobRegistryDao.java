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

import com.taotao.cloud.xxljob.core.model.XxlJobRegistry;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobRegistryDao {

    public List<Integer> findDead(@Param("timeout") int timeout, @Param("nowTime") Date nowTime);

    public int removeDead(@Param("ids") List<Integer> ids);

    public List<XxlJobRegistry> findAll(
            @Param("timeout") int timeout, @Param("nowTime") Date nowTime);

    public int registrySaveOrUpdate(
            @Param("registryGroup") String registryGroup,
            @Param("registryKey") String registryKey,
            @Param("registryValue") String registryValue,
            @Param("updateTime") Date updateTime);

    /*public int registryUpdate(@Param("registryGroup") String registryGroup,
                              @Param("registryKey") String registryKey,
                              @Param("registryValue") String registryValue,
                              @Param("updateTime") Date updateTime);

    public int registrySave(@Param("registryGroup") String registryGroup,
                            @Param("registryKey") String registryKey,
                            @Param("registryValue") String registryValue,
                            @Param("updateTime") Date updateTime);*/

    public int registryDelete(
            @Param("registryGroup") String registryGroup,
            @Param("registryKey") String registryKey,
            @Param("registryValue") String registryValue);
}
