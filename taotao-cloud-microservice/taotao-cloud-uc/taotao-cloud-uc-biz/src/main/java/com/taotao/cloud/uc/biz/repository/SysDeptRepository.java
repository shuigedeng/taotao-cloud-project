package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.data.jpa.repository.BaseJpaRepository;
import com.taotao.cloud.uc.biz.entity.SysDept;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


/**
 * 后台部门表Repository
 *
 * @author dengtao
 * @since 2020/9/29 18:02
 * @version 1.0.0
 */
@Repository
public class SysDeptRepository extends BaseJpaRepository<SysDept, Long> {
    public SysDeptRepository(EntityManager em) {
        super(SysDept.class, em);
    }
}
