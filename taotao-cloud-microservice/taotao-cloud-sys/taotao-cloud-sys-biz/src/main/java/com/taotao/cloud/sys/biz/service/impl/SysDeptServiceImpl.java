package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.api.dubbo.IDubboCompanyService;
import com.taotao.cloud.sys.biz.entity.Dept;
import com.taotao.cloud.sys.biz.mapper.ISysDeptMapper;
import com.taotao.cloud.sys.biz.repository.inf.ISysDeptRepository;
import com.taotao.cloud.sys.biz.repository.cls.SysDeptRepository;
import com.taotao.cloud.sys.api.dubbo.IDubboDeptService;
import com.taotao.cloud.sys.biz.service.ISysDeptService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * SysDeptServiceImpl
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
@Service
@DubboService(interfaceClass = IDubboCompanyService.class)
public class SysDeptServiceImpl extends
	BaseSuperServiceImpl<ISysDeptMapper, Dept, SysDeptRepository, ISysDeptRepository, Long>
	implements IDubboDeptService, ISysDeptService {

}
