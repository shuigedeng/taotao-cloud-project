package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.QSysResource;
import com.taotao.cloud.uc.biz.entity.QSysRoleResource;
import com.taotao.cloud.uc.biz.entity.SysResource;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 资源表Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class SysResourceRepository extends BaseJpaRepository<SysResource, Long> {
	public SysResourceRepository(EntityManager em) {
		super(SysResource.class, em);
	}

	private final static QSysResource SYS_RESOURCE = QSysResource.sysResource;
	private final static QSysRoleResource SYS_ROLE_RESOURCE = QSysRoleResource.sysRoleResource;

	public List<SysResource> findResourceByRoleIds(Set<Long> roleIds) {
		return jpaQueryFactory.select(SYS_RESOURCE)
			.from(SYS_RESOURCE)
			.innerJoin(SYS_ROLE_RESOURCE)
			.on(SYS_RESOURCE.id.eq(SYS_ROLE_RESOURCE.resourceId))
			.where(SYS_ROLE_RESOURCE.roleId.in(roleIds))
			.fetch();
	}

	public List<SysResource> findResourceByParentId(Long parentId) {
		return jpaQueryFactory.selectFrom(SYS_RESOURCE)
			.where(SYS_RESOURCE.parentId.eq(parentId))
			.fetch();
	}

	public List<Long> selectIdList(List<Long> pidList) {
		return jpaQueryFactory.select(SYS_RESOURCE.id)
			.from(SYS_RESOURCE)
			.where(SYS_RESOURCE.parentId.in(pidList))
			.fetch();
	}

	public List<SysResource> findResourceByIdList(List<Long> idList) {
		return jpaQueryFactory
			.selectFrom(SYS_RESOURCE)
			.where(SYS_RESOURCE.id.in(idList))
			.fetch();
	}

	public Optional<SysResource> findResourceByName(String name) {
		SysResource resource = jpaQueryFactory
			.selectFrom(SYS_RESOURCE)
			.where(SYS_RESOURCE.name.eq(name))
			.fetchOne();
		return Optional.ofNullable(resource);
	}
}
