/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.biz.entity.RoleDept;
import com.taotao.cloud.sys.biz.mapper.ISysRoleDeptMapper;
import com.taotao.cloud.sys.biz.repository.inf.ISysRoleDeptRepository;
import com.taotao.cloud.sys.biz.repository.cls.SysRoleDeptRepository;
import com.taotao.cloud.sys.biz.service.ISysRoleDeptService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/21 09:48
 */
@Service
public class SysRoleDeptServiceImpl extends
	BaseSuperServiceImpl<ISysRoleDeptMapper, RoleDept, SysRoleDeptRepository, ISysRoleDeptRepository, Long>
	implements ISysRoleDeptService {


}
