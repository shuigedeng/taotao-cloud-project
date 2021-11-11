package com.taotao.cloud.standalone.system.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysRoleDept;

import java.util.List;

/**
 * <p>
 * 角色与部门对应关系 服务类
 * </p>
 *

 * @since 2019-04-21
 */
public interface ISysRoleDeptService extends IService<SysRoleDept> {

    /**
     * 根据角色id查询部门ids
     * @param roleId
     * @return
     */
    List<SysRoleDept> getRoleDeptIds(int roleId);
}
