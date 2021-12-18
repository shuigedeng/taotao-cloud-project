package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.biz.entity.Job;
import com.taotao.cloud.sys.biz.mapper.ISysJobMapper;
import com.taotao.cloud.sys.biz.repository.inf.ISysJobRepository;
import com.taotao.cloud.sys.biz.repository.cls.SysJobRepository;
import com.taotao.cloud.sys.api.dubbo.IDubboJobService;
import com.taotao.cloud.sys.biz.service.ISysJobService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 岗位表服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Service
@DubboService(interfaceClass =IDubboJobService.class )
public class SysJobServiceImpl  extends
	BaseSuperServiceImpl<ISysJobMapper, Job,SysJobRepository, ISysJobRepository, Long>
	implements IDubboJobService, ISysJobService {

	@Autowired
	private ISysJobService sysJobService;


}
