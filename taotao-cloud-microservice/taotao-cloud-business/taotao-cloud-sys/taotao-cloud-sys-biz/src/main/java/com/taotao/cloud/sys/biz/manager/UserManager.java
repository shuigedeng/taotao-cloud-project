/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.sys.biz.manager;

import com.taotao.cloud.sys.biz.mapper.IUserMapper;
import com.taotao.cloud.sys.biz.model.entity.system.User;
import com.taotao.cloud.sys.biz.repository.UserRepository;
import com.taotao.cloud.sys.biz.repository.IUserRepository;
import com.taotao.boot.web.annotation.Manager;
import com.taotao.boot.webagg.manager.BaseSuperManager;
import lombok.*;

/**
 * 用户管理器
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-08-18 14:55:41
 */
@Manager
@AllArgsConstructor
public class UserManager extends BaseSuperManager<User, Long, IUserMapper, UserRepository, IUserRepository> {

    // @Transactional(rollbackFor = Throwable.class)
    // public void upOrDown(DepartmentEntity departmentEntity ,DepartmentEntity swapEntity){
    //	Long departmentSort = departmentEntity.getSort();
    //	departmentEntity.setSort(swapEntity.getSort());
    //	departmentDao.updateById(departmentEntity);
    //	swapEntity.setSort(departmentSort);
    //	departmentDao.updateById(swapEntity);
    // }
}
