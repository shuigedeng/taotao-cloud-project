package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.uc.biz.entity.SysCompany;
import com.taotao.cloud.uc.biz.entity.SysRoleResource;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;


/**
 * 后台部门表Repository
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/29 18:02
 */
@Repository
public class SysRoleResourceRepository extends BaseSuperRepository<SysRoleResource, Long> {

	public SysRoleResourceRepository(EntityManager em) {
		super(SysRoleResource.class, em);
	}
}
