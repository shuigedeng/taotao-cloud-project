package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.QSysResource;
import com.taotao.cloud.uc.biz.entity.QSysRole;
import com.taotao.cloud.uc.biz.entity.SysRoleDept;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 角色-部门第三方表Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class SysRoleDeptRepository extends BaseJpaRepository<SysRoleDept, Long> {
    public SysRoleDeptRepository(EntityManager em) {
        super(SysRoleDept.class, em);
    }

    private final static QSysResource SYS_RESOURCE = QSysResource.sysResource;
}
