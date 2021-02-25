package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.SysRoleResource;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 角色-资源第三方表Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class SysRoleResourceRepository extends BaseJpaRepository<SysRoleResource, Long> {
    public SysRoleResourceRepository(EntityManager em) {
        super(SysRoleResource.class, em);
    }
}
