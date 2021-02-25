package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.QSysUser;
import com.taotao.cloud.uc.biz.entity.QSysUserRole;
import com.taotao.cloud.uc.biz.entity.SysUserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 用户-角色第三方表Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class SysUserRoleRepository extends BaseJpaRepository<SysUserRole, Long> {
    public SysUserRoleRepository(EntityManager em) {
        super(SysUserRole.class, em);
    }

    private final static QSysUserRole SYS_USER_ROLE = QSysUserRole.sysUserRole;
}
