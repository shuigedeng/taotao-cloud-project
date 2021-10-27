package com.taotao.cloud.uc.biz.repository.cls;

import com.taotao.cloud.uc.biz.entity.SysCompany;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;


/**
 * 后台部门表Repository
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/29 18:02
 */
@Repository
public class SysCompanyRepository extends BaseSuperRepository<SysCompany, Long> {

	public SysCompanyRepository(EntityManager em) {
		super(SysCompany.class, em);
	}
}
