package com.taotao.cloud.stock.biz.domain.role.repository;

import com.taotao.cloud.stock.biz.domain.model.role.Role;
import com.taotao.cloud.stock.biz.domain.model.role.RoleCode;
import com.taotao.cloud.stock.biz.domain.model.role.RoleId;
import com.taotao.cloud.stock.biz.domain.model.role.RoleName;
import com.taotao.cloud.stock.biz.domain.role.model.entity.Role;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleCode;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleId;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleName;
import java.util.List;

/**
 * 角色-Repository接口
 *
 * @author shuigedeng
 * @date 2021-02-14
 **/
public interface RoleRepository {

    /**
     * 获取角色
     *
     * @param roleId
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.role.Role find(
		    com.taotao.cloud.stock.biz.domain.model.role.RoleId roleId);

    /**
     * 获取角色
     *
     * @param roleName
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.role.Role find(RoleName roleName);

    /**
     * 获取角色
     *
     * @param roleCode
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.role.Role find(RoleCode roleCode);

    /**
     * 保存
     *
     * @param role
     */
    com.taotao.cloud.stock.biz.domain.model.role.RoleId store(Role role);

    /**
     * 删除
     *
     * @param roleIds
     */
    void remove(List<RoleId> roleIds);

}
