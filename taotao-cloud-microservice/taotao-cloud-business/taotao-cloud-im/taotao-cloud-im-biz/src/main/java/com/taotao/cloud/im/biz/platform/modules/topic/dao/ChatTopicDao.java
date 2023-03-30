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

package com.taotao.cloud.im.biz.platform.modules.topic.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.topic.domain.ChatTopic;
import com.platform.modules.topic.vo.TopicVo04;
import java.util.List;
import org.springframework.stereotype.Repository;

/** 主题 数据库访问层 q3z3 */
@Repository
public interface ChatTopicDao extends BaseDao<ChatTopic> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatTopic> queryList(ChatTopic chatTopic);

    /** 查询 */
    List<TopicVo04> topicList(Long userId);
}
