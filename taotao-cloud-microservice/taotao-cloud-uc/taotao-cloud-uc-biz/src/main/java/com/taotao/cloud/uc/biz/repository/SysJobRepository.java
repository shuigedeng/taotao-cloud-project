package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.uc.biz.entity.SysJob;
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
public class SysJobRepository extends BaseSuperRepository<SysJob, Long> {
    public SysJobRepository(EntityManager em) {
        super(SysJob.class, em);
    }
}
