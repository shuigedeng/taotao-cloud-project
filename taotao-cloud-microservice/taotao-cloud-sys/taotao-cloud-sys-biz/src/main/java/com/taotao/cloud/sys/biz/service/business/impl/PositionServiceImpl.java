package com.taotao.cloud.sys.biz.service.business.impl;

import com.taotao.cloud.sys.biz.model.entity.system.Position;
import com.taotao.cloud.sys.biz.mapper.IJobMapper;
import com.taotao.cloud.sys.biz.repository.inf.IJobRepository;
import com.taotao.cloud.sys.biz.repository.cls.JobRepository;
import com.taotao.cloud.sys.biz.service.business.IPositionService;
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
public class PositionServiceImpl extends
	BaseSuperServiceImpl<IJobMapper, Position,JobRepository, IJobRepository, Long>
	implements IPositionService {


}
