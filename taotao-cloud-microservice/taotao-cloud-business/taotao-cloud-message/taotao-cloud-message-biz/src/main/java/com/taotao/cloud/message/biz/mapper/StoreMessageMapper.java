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

package com.taotao.cloud.message.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.message.biz.model.entity.StoreMessage;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** 店铺接收到消息发送数据处理层 */
public interface StoreMessageMapper extends MpSuperMapper<StoreMessage, Long> {

    /**
     * 店铺消息分页
     *
     * @param page 分页
     * @param queryWrapper 查询参数
     * @return 店铺消息分页
     */
    @Select(
            """
		select me.title,me.content,me.create_time,sp.store_name,sp.store_id,sp.id,sp.status
		from tt_message me inner join tt_store_message sp on me.id = sp.message_id
		${ew.customSqlSegment}
		""")
    IPage<StoreMessage> queryByParams(
            IPage<StoreMessage> page, @Param(Constants.WRAPPER) Wrapper<StoreMessage> queryWrapper);
}
