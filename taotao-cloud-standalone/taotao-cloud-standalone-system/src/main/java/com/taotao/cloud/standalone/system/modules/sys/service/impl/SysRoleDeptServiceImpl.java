package com.taotao.cloud.standalone.system.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysRoleDept;
import com.taotao.cloud.standalone.system.modules.sys.mapper.SysRoleDeptMapper;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysRoleDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色与部门对应关系 服务实现类
 * </p>
 *

 * @since 2019-04-21
 */
@Service
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements ISysRoleDeptService {


    @Override
    public List<SysRoleDept> getRoleDeptIds(int roleId) {
        return baseMapper.selectList(Wrappers.<SysRoleDept>lambdaQuery().select(SysRoleDept::getDeptId).eq(SysRoleDept::getRoleId,roleId));
    }
}
