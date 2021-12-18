package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.api.dubbo.IDubboCompanyService;
import com.taotao.cloud.sys.api.dubbo.IDubboDeptService;
import com.taotao.cloud.sys.biz.entity.Dept;
import com.taotao.cloud.sys.biz.mapper.IDeptMapper;
import com.taotao.cloud.sys.biz.repository.cls.DeptRepository;
import com.taotao.cloud.sys.biz.repository.inf.IDeptRepository;
import com.taotao.cloud.sys.biz.service.IDeptService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * DeptServiceImpl
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
@Service
@DubboService(interfaceClass = IDubboCompanyService.class)
public class DeptServiceImpl extends
	BaseSuperServiceImpl<IDeptMapper, Dept, DeptRepository, IDeptRepository, Long>
	implements IDubboDeptService, IDeptService {

}
