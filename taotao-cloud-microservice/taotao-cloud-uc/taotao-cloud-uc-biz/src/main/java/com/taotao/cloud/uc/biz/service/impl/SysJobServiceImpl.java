package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.api.entity.SysJob;
import com.taotao.cloud.uc.biz.mapper.SysJobMapper;
import com.taotao.cloud.uc.biz.repository.SysJobRepository;
import com.taotao.cloud.uc.api.service.ISysJobService;
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
public class SysJobServiceImpl  extends
	BaseSuperServiceImpl<SysJobMapper, SysJob,SysJobRepository, Long>
	implements ISysJobService<SysJob, Long> {
}
