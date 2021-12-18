package com.taotao.cloud.system.biz.repository.inf;

import com.taotao.cloud.uc.biz.entity.SysJob;
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
