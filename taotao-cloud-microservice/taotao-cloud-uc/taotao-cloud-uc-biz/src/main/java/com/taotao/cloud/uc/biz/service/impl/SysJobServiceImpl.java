package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.biz.repository.SysJobRepository;
import com.taotao.cloud.uc.biz.service.ISysJobService;
import org.springframework.stereotype.Service;

/**
 * 岗位表服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Service
public class SysJobServiceImpl  implements ISysJobService {
    private final SysJobRepository sysJobRepository;

	public SysJobServiceImpl(SysJobRepository sysJobRepository) {
		this.sysJobRepository = sysJobRepository;
	}
}
