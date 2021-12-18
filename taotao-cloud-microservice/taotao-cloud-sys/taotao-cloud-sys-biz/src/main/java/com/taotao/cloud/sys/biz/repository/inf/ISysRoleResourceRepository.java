package com.taotao.cloud.sys.biz.repository.inf;

import com.taotao.cloud.sys.biz.entity.RoleResource;
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
public interface ISysRoleResourceRepository extends JpaRepository<RoleResource, Long> {

}
