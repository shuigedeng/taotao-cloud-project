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

package com.taotao.cloud.workflow.biz.common.database.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.taotao.cloud.workflow.biz.common.util.DateUtil;
import com.taotao.cloud.workflow.biz.common.util.UserProvider;
import com.taotao.cloud.workflow.biz.common.util.context.SpringContext;
import java.util.Date;
import org.springframework.stereotype.Component;

/** MybatisPlus配置类 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    private UserProvider userProvider;

    @Override
    public void insertFill(MetaObject metaObject) {
        userProvider = SpringContext.getBean(UserProvider.class);
        UserInfo userInfo = userProvider.get();
        Object enabledMark = this.getFieldValByName("enabledMark", metaObject);
        Object creatorUserId = this.getFieldValByName("creatorUserId", metaObject);
        Object creatorTime = this.getFieldValByName("creatorTime", metaObject);
        Object creatorUser = this.getFieldValByName("creatorUser", metaObject);
        if (enabledMark == null) {
            this.setFieldValByName("enabledMark", 1, metaObject);
        }
        if (creatorUserId == null) {
            this.setFieldValByName("creatorUserId", userInfo.getUserId(), metaObject);
        }
        if (creatorTime == null) {
            this.setFieldValByName("creatorTime", DateUtil.getNowDate(), metaObject);
        }
        if (creatorUser == null) {
            this.setFieldValByName("creatorUser", userInfo.getUserId(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        userProvider = SpringContext.getBean(UserProvider.class);
        UserInfo userInfo = userProvider.get();
        this.setFieldValByName("lastModifyTime", new Date(), metaObject);
        this.setFieldValByName("lastModifyUserId", userInfo.getUserId(), metaObject);
        this.setFieldValByName("lastModifyUser", userInfo.getUserId(), metaObject);
    }
}
