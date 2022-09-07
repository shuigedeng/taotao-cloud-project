package com.taotao.cloud.sys.biz.service.dubbo;

import com.taotao.cloud.sys.api.dubbo.IDubboJobService;
import com.taotao.cloud.sys.biz.mapper.IJobMapper;
import com.taotao.cloud.sys.biz.model.entity.system.Position;
import com.taotao.cloud.sys.biz.repository.cls.JobRepository;
import com.taotao.cloud.sys.biz.repository.inf.IJobRepository;
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
@DubboService(interfaceClass =IDubboJobService.class , validation = "true")
public class DubboJobServiceImpl extends
	BaseSuperServiceImpl<IJobMapper, Position,JobRepository, IJobRepository, Long>
	implements IDubboJobService {


}
