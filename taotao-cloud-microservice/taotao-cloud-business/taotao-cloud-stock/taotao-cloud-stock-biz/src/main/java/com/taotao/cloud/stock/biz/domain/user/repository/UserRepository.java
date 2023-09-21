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

package com.taotao.cloud.stock.biz.domain.user.repository;

import com.taotao.cloud.stock.biz.domain.model.user.Mobile;
import com.taotao.cloud.stock.biz.domain.model.user.User;
import com.taotao.cloud.stock.biz.domain.model.user.UserId;
import com.taotao.cloud.stock.biz.domain.user.model.entity.User;
import com.taotao.cloud.stock.biz.domain.user.model.vo.Mobile;
import com.taotao.cloud.stock.biz.domain.user.model.vo.UserId;
import java.util.List;

/**
 * 用户-Repository接口
 *
 * @author shuigedeng
 * @since 2021-02-02
 */
public interface UserRepository {

    /**
     * 通过用户ID获取用户
     *
     * @param userId
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.user.User find(com.taotao.cloud.stock.biz.domain.model.user.UserId userId);

    /**
     * 根据手机号获取账号
     *
     * @param mobile
     * @return
     */
    List<com.taotao.cloud.stock.biz.domain.model.user.User> find(Mobile mobile);

    /**
     * 保存
     *
     * @param user
     */
    com.taotao.cloud.stock.biz.domain.model.user.UserId store(User user);

    /**
     * 删除
     *
     * @param userIds
     */
    void remove(List<UserId> userIds);
}
