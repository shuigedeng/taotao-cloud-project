package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.biz.entity.SysDept;
import com.taotao.cloud.uc.biz.mapper.SysDeptMapper;
import com.taotao.cloud.uc.biz.repository.ISysDeptRepository;
import com.taotao.cloud.uc.biz.repository.impl.SysDeptRepository;
import com.taotao.cloud.uc.biz.service.ISysDeptService;
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
@DubboService
public class SysDeptServiceImpl extends
	BaseSuperServiceImpl<SysDeptMapper, SysDept, SysDeptRepository, ISysDeptRepository, Long>
	implements ISysDeptService<SysDept, Long> {

}
