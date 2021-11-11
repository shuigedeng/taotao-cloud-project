package com.taotao.cloud.standalone.system.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysUserRole;
import com.taotao.cloud.standalone.system.modules.sys.mapper.SysUserRoleMapper;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {


    @Override
    public boolean save(SysUserRole entity) {
        return super.save(entity);
    }


    @Override
    public List<SysUserRole> selectUserRoleListByUserId(Integer userId) {
        return baseMapper.selectUserRoleListByUserId(userId);
    }
}
