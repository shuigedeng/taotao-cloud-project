package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.uc.biz.entity.SysRoleResource;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * 后台部门表Repository
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/29 18:02
 */
@Repository
public interface ISysRoleResourceRepository extends JpaRepository<SysRoleResource, Long> {

}
