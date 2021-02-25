package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.SysJob;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 岗位表Repository
 *
 * @author dengtao
 * @date 2020/9/29 18:02
 * @since v1.0
 */
@Repository
public class SysJobRepository extends BaseJpaRepository<SysJob, Long> {
    public SysJobRepository(EntityManager em) {
        super(SysJob.class, em);
    }
}
