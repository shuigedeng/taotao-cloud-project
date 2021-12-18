package com.taotao.cloud.sys.biz.repository.cls;

import com.taotao.cloud.sys.biz.entity.Job;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 岗位表Repository
 *
 * @author shuigedeng
 * @since 2020/9/29 18:02
 * @version 1.0.0
 */
@Repository
public class SysJobRepository extends BaseSuperRepository<Job, Long> {
    public SysJobRepository(EntityManager em) {
        super(Job.class, em);
    }
}
