package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.biz.entity.SysJob;
import com.taotao.cloud.uc.biz.mapper.ISysJobMapper;
import com.taotao.cloud.uc.biz.repository.inf.ISysJobRepository;
import com.taotao.cloud.uc.biz.repository.cls.SysJobRepository;
import com.taotao.cloud.uc.biz.service.ISysJobService;
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
public class SysJobServiceImpl  extends
	BaseSuperServiceImpl<ISysJobMapper, SysJob,SysJobRepository, ISysJobRepository, Long>
	implements ISysJobService<SysJob, Long> {
}
