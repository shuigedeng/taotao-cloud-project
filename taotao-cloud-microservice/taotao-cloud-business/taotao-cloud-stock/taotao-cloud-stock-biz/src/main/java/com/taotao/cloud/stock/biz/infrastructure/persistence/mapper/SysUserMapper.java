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

package com.taotao.cloud.stock.biz.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper
 *
 * @author shuigedeng
 * @since 2021-01-23
 */
@Mapper
public interface SysUserMapper extends MpSuperMapper<SysUserDO> {

    /**
     * 分页查询
     *
     * @param page
     * @param params
     * @return
     */
    IPage<SysUserDO> queryPage(IPage<SysUserDO> page, @Param("params") Map<String, Object> params);

    /**
     * 查询用户
     *
     * @param params
     * @return
     */
    SysUserDO queryUser(@Param("params") Map<String, Object> params);

    /**
     * 查询用户(不包含租户)
     *
     * @param params
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysUserDO> queryUserNoTenant(@Param("params") Map<String, Object> params);
}
