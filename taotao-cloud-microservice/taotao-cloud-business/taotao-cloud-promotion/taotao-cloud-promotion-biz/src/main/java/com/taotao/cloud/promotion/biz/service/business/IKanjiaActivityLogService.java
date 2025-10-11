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

package com.taotao.cloud.promotion.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityDTO;
import com.taotao.cloud.promotion.api.model.page.KanJiaActivityLogPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityLog;

/**
 * 砍价活动日志业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:40
 */
public interface IKanjiaActivityLogService extends IService<KanjiaActivityLog> {

    /**
     * 根据砍价参与记录id查询砍价记录
     *
     * @param kanJiaActivityLogPageQuery 砍价活动帮砍信息
     * @param pageVO 分页信息
     * @return {@link IPage }<{@link KanjiaActivityLog }>
     * @since 2022-04-27 16:43:40
     */
    IPage<KanjiaActivityLog> getForPage(KanJiaActivityLogPageQuery kanJiaActivityLogPageQuery, PageQuery pageVO);

    /**
     * 砍一刀
     *
     * @param kanJiaActivityDTO 砍价记录
     * @return {@link KanjiaActivityLog }
     * @since 2022-04-27 16:43:40
     */
    KanjiaActivityLog addKanJiaActivityLog(KanjiaActivityDTO kanJiaActivityDTO);

	KanjiaActivityLog queryKanjiaActivityLog(Long id, Long userId);
}
