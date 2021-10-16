package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.api.service.ISysDeptService;
import com.taotao.cloud.uc.api.entity.SysDept;
import com.taotao.cloud.uc.biz.mapper.SysDeptMapper;
import com.taotao.cloud.uc.biz.repository.SysDeptRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * SysDeptServiceImpl
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
@Service
public class SysDeptServiceImpl extends
	BaseSuperServiceImpl<SysDeptMapper, SysDept, SysDeptRepository, Long>
	implements ISysDeptService<SysDept, Long> {

}
