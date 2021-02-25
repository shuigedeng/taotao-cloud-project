package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.QSysRole;
import com.taotao.cloud.uc.biz.entity.QSysUserRole;
import com.taotao.cloud.uc.biz.entity.SysRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

/**
 * 角色表Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class SysRoleRepository extends BaseJpaRepository<SysRole, Long> {
    public SysRoleRepository(EntityManager em) {
        super(SysRole.class, em);
    }

    private final static QSysRole SYS_ROLE = QSysRole.sysRole;
    private final static QSysUserRole SYS_USER_ROLE = QSysUserRole.sysUserRole;

    public List<SysRole> findRoleByUserIds(Set<Long> userIds) {
        return jpaQueryFactory.select(SYS_ROLE)
                .from(SYS_ROLE)
                .innerJoin(SYS_USER_ROLE)
                .on(SYS_ROLE.id.eq(SYS_USER_ROLE.roleId))
                .where(SYS_USER_ROLE.userId.in(userIds))
                .fetch();
    }

    public List<SysRole> findRoleByCodes(Set<String> codes) {
        return jpaQueryFactory.selectFrom(SYS_ROLE)
                .where(SYS_ROLE.code.in(codes))
                .fetch();
    }
}
