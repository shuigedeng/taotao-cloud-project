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

package com.taotao.cloud.goods.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.goods.biz.model.vo.StudioCommodityVO;
import com.taotao.cloud.goods.biz.model.entity.Studio;
import com.taotao.cloud.stream.framework.trigger.message.BroadcastMessage;
import com.taotao.boot.webagg.service.BaseSuperService;

/**
 * 直播间业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:01:11
 */
public interface IStudioService extends BaseSuperService<Studio, Long> {

    /**
     * 创建直播间 直播间默认手机直播 默认开启：点赞、商品货架、评论、回放
     *
     * @param studio 直播间
     * @return {@link boolean }
     * @since 2022-04-27 17:01:11
     */
    boolean create(Studio studio);

    /**
     * 修改直播间 直播间默认手机直播
     *
     * @param studio 直播间
     * @return {@link boolean }
     * @since 2022-04-27 17:01:11
     */
    boolean edit(Studio studio);

    /**
     * 获取直播间信息
     *
     * @param id 直播间ID
     * @return {@link StudioCommodityVO }
     * @since 2022-04-27 17:01:11
     */
    StudioCommodityVO getStudioVO(Long id);

    /**
     * 获取直播间回放
     *
     * @param roomId 房间ID
     * @return {@link String }
     * @since 2022-04-27 17:01:11
     */
    String getLiveInfo(Integer roomId);

    /**
     * 推送商品
     *
     * @param roomId 房间ID
     * @param goodsId 商品ID
     * @param storeId 店铺ID
     * @return {@link boolean }
     * @since 2022-04-27 17:01:11
     */
    boolean push(Integer roomId, Long goodsId, Long storeId);

    /**
     * 删除商品
     *
     * @param roomId 店铺ID
     * @param goodsId 商品ID
     * @param storeId
     * @return {@link boolean }
     * @since 2022-04-27 17:01:11
     */
    boolean goodsDeleteInRoom(Integer roomId, Long goodsId, Long storeId);

    /**
     * 获取直播间列表
     *
     * @param PageQuery 分页
     * @param recommend 是否推荐
     * @param status 直播间状态
     * @return {@link IPage }<{@link Studio }>
     * @since 2022-04-27 17:01:12
     */
    IPage<Studio> studioList(PageQuery PageQuery, Integer recommend, String status);

    /**
     * 修改直播间状态
     *
     * @param broadcastMessage 直播间消息
     * @return {@link boolean }
     * @since 2022-04-27 17:01:12
     */
    boolean updateStudioStatus(BroadcastMessage broadcastMessage);
}
