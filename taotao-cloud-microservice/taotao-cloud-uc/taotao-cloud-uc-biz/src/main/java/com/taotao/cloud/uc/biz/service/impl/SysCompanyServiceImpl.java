package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.api.service.ISysCompanyService;
import com.taotao.cloud.uc.biz.entity.SysCompany;
import com.taotao.cloud.uc.biz.mapper.SysCompanyMapper;
import com.taotao.cloud.uc.biz.repository.SysCompanyRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 岗位表服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Service
public class SysCompanyServiceImpl extends
	BaseSuperServiceImpl<SysCompanyMapper, SysCompany, SysCompanyRepository, Long>
	implements ISysCompanyService<SysCompany, Long> {

}
