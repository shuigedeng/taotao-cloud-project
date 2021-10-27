package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.uc.api.dubbo.IDubboCompanyService;
import com.taotao.cloud.uc.api.vo.resource.ResourceQueryBO;
import com.taotao.cloud.uc.biz.entity.SysCompany;
import com.taotao.cloud.uc.biz.mapper.ISysCompanyMapper;
import com.taotao.cloud.uc.biz.repository.inf.ISysCompanyRepository;
import com.taotao.cloud.uc.biz.repository.cls.SysCompanyRepository;
import com.taotao.cloud.uc.biz.service.ISysCompanyService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 岗位表服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Service
@DubboService
public class SysCompanyServiceImpl extends
	BaseSuperServiceImpl<ISysCompanyMapper, SysCompany, SysCompanyRepository, ISysCompanyRepository, Long>
	implements IDubboCompanyService, ISysCompanyService<SysCompany, Long> {

	@Override
	public ResourceQueryBO queryAllId(Long id) {
		SysCompany sysCompany = getById(id);
		LogUtil.info(sysCompany.toString());
		return null;
	}
}
