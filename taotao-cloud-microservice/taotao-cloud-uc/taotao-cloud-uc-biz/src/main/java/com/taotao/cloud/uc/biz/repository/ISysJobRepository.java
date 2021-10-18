package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.uc.biz.entity.SysJob;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 岗位表Repository
 *
 * @author shuigedeng
 * @since 2020/9/29 18:02
 * @version 1.0.0
 */
@Repository
public interface ISysJobRepository extends JpaRepository<SysJob, Long> {
}
