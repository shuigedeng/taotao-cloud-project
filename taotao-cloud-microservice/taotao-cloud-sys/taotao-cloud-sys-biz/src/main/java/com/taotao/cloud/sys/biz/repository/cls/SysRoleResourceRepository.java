package com.taotao.cloud.sys.biz.repository.cls;

import com.taotao.cloud.sys.biz.entity.RoleResource;
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
public class SysRoleResourceRepository extends BaseSuperRepository<RoleResource, Long> {

	public SysRoleResourceRepository(EntityManager em) {
		super(RoleResource.class, em);
	}
}
