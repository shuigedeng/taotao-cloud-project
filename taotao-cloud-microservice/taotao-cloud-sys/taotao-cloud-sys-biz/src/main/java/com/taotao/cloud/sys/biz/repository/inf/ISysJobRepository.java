package com.taotao.cloud.sys.biz.repository.inf;

import com.taotao.cloud.sys.biz.entity.Job;
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
public interface ISysJobRepository extends JpaRepository<Job, Long> {
}
