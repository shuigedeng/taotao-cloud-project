package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.biz.entity.Dept;
import com.taotao.cloud.sys.biz.mapper.IDeptMapper;
import com.taotao.cloud.sys.biz.repository.cls.DeptRepository;
import com.taotao.cloud.sys.biz.repository.inf.IDeptRepository;
import com.taotao.cloud.sys.biz.service.IDeptService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * DeptServiceImpl
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
@Service
public class DeptServiceImpl extends
	BaseSuperServiceImpl<IDeptMapper, Dept, DeptRepository, IDeptRepository, Long>
	implements IDeptService {

}
