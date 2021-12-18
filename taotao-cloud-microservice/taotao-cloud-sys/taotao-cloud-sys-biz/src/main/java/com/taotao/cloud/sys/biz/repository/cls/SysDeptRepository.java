package com.taotao.cloud.sys.biz.repository.cls;

import com.taotao.cloud.sys.biz.entity.Dept;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


/**
 * 后台部门表Repository
 *
 * @author shuigedeng
 * @since 2020/9/29 18:02
 * @version 1.0.0
 */
@Repository
public class SysDeptRepository extends BaseSuperRepository<Dept, Long> {
    public SysDeptRepository(EntityManager em) {
        super(Dept.class, em);
    }
}
