/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.system.biz.entity.RolesDepts;
import com.taotao.cloud.system.biz.mapper.RolesDeptsMapper;
import com.taotao.cloud.system.biz.service.RolesDeptsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author hupeng
 * @date 2020-05-16
 */
@Service
//@CacheConfig(cacheNames = "rolesDepts")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RolesDeptsServiceImpl extends ServiceImpl<RolesDeptsMapper, RolesDepts> implements
	RolesDeptsService {

}
