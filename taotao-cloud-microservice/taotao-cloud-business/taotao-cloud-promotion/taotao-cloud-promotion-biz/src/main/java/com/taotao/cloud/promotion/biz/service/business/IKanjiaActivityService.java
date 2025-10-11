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
import com.taotao.cloud.promotion.api.model.page.KanjiaActivityPageQuery;
import com.taotao.cloud.promotion.api.model.query.KanjiaActivitySearchQuery;
import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityVO;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivity;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityLog;

/**
 * 砍价活动参与记录业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:44
 */
public interface IKanjiaActivityService extends IService<KanjiaActivity> {

    /**
     * 获取砍价活动
     *
     * @param kanJiaActivitySearchParams 砍价活动搜索参数
     * @return {@link KanjiaActivity }
     * @since 2022-04-27 16:43:44
     */
    KanjiaActivity getKanjiaActivity(KanjiaActivitySearchQuery kanJiaActivitySearchParams);

    /**
     * 获取砍价活动
     *
     * <p>有值说明是已参加的砍价活动 没有值说明是未参加的砍价活动
     *
     * @param kanJiaActivitySearchParams 砍价活动搜索参数
     * @return {@link KanjiaActivityVO }
     * @since 2022-04-27 16:43:44
     */
    KanjiaActivityVO getKanjiaActivityVO(KanjiaActivitySearchQuery kanJiaActivitySearchParams);

    /**
     * 发起人发起砍价活动
     *
     * @param id 活动ID
     * @return {@link KanjiaActivityLog }
     * @since 2022-04-27 16:43:44
     */
    KanjiaActivityLog add(String id);

    /**
     * 帮砍
     *
     * @param kanJiaActivityId 活动id
     * @return {@link KanjiaActivityLog }
     * @since 2022-04-27 16:43:44
     */
    KanjiaActivityLog helpKanJia(String kanJiaActivityId);

    /**
     * 根据条件查询我参与的砍价活动
     *
     * @param kanJiaActivityPageQuery 砍价活动查询条件
     * @param page 分页对象
     * @return {@link IPage }<{@link KanjiaActivity }>
     * @since 2022-04-27 16:43:45
     */
    IPage<KanjiaActivity> getForPage(KanjiaActivityPageQuery kanJiaActivityPageQuery, PageQuery page);
}
