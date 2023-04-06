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

package com.taotao.cloud.message.biz.austin.support.dao;

import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 渠道账号信息 Dao
 *
 * @author 3y
 */
public interface ChannelAccountDao extends JpaRepository<ChannelAccount, Long> {

    /**
     * 查询 列表
     *
     * @param deleted 0：未删除 1：删除
     * @param channelType 渠道值
     * @param creator 创建者
     * @return
     */
    List<ChannelAccount> findAllByIsDeletedEqualsAndCreatorEqualsAndSendChannelEquals(
            Integer deleted, String creator, Integer channelType);

    /**
     * 查询 列表
     *
     * @param deleted 0：未删除 1：删除
     * @param channelType 渠道值
     * @return
     */
    List<ChannelAccount> findAllByIsDeletedEqualsAndSendChannelEquals(Integer deleted, Integer channelType);

    /**
     * 根据创建者检索相关的记录
     *
     * @param creator
     * @return
     */
    List<ChannelAccount> findAllByCreatorEquals(String creator);

    /**
     * 统计未删除的条数
     *
     * @param deleted
     * @return
     */
    Long countByIsDeletedEquals(Integer deleted);
}
