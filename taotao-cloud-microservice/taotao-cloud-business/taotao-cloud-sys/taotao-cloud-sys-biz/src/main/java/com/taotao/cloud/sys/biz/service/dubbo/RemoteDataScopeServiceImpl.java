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

package com.taotao.cloud.sys.biz.service.dubbo;

import com.taotao.cloud.sys.api.dubbo.RemoteDataScopeService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 数据权限 实现
 *
 * <p>注意: 此Service内不允许调用标注`数据权限`注解的方法 例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解
 * 会出现循环解析的问题
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteDataScopeServiceImpl implements RemoteDataScopeService {

    @Override
    public String getRoleCustom(Long roleId) {
        return null;
    }

    @Override
    public String getDeptAndChild(Long deptId) {
        return null;
    }

    // private final SysRoleDeptMapper roleDeptMapper;
    // private final SysDeptMapper deptMapper;
    //
    // @Override
    // public String getRoleCustom(Long roleId) {
    //	List<SysRoleDept> list = roleDeptMapper.selectList(
    //		new LambdaQueryWrapper<SysRoleDept>()
    //			.select(SysRoleDept::getDeptId)
    //			.eq(SysRoleDept::getRoleId, roleId));
    //	if (CollUtil.isNotEmpty(list)) {
    //		return StreamUtils.join(list, rd -> Convert.toStr(rd.getDeptId()));
    //	}
    //	return null;
    // }
    //
    // @Override
    // public String getDeptAndChild(Long deptId) {
    //	List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
    //		.select(SysDept::getDeptId)
    //		.apply(DataBaseHelper.findInSet(deptId, "ancestors")));
    //	List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
    //	ids.add(deptId);
    //	List<SysDept> list = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
    //		.select(SysDept::getDeptId)
    //		.in(SysDept::getDeptId, ids));
    //	if (CollUtil.isNotEmpty(list)) {
    //		return StreamUtils.join(list, d -> Convert.toStr(d.getDeptId()));
    //	}
    //	return null;
    // }

}
