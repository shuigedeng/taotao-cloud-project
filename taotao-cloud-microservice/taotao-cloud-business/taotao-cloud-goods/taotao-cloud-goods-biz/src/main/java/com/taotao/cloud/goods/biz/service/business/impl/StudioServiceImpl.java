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

package com.taotao.cloud.goods.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.common.utils.common.OrikaUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.common.utils.date.DateUtils;
import com.taotao.cloud.goods.api.enums.StudioStatusEnum;
import com.taotao.cloud.goods.biz.model.vo.CommodityVO;
import com.taotao.cloud.goods.biz.model.vo.StudioCommodityVO;
import com.taotao.cloud.goods.biz.mapper.CommodityMapper;
import com.taotao.cloud.goods.biz.mapper.StudioMapper;
import com.taotao.cloud.goods.biz.model.entity.Commodity;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.cloud.goods.biz.model.entity.Studio;
import com.taotao.cloud.goods.biz.model.entity.StudioCommodity;
import com.taotao.cloud.goods.biz.repository.StudioRepository;
import com.taotao.cloud.goods.biz.service.business.GoodsService;
import com.taotao.cloud.goods.biz.service.business.StudioCommodityService;
import com.taotao.cloud.goods.biz.service.business.StudioService;
import com.taotao.cloud.goods.biz.util.WechatLivePlayerUtil;
import com.taotao.cloud.stream.framework.trigger.enums.DelayTypeEnums;
import com.taotao.cloud.stream.framework.trigger.interfaces.TimeTrigger;
import com.taotao.cloud.stream.framework.trigger.message.BroadcastMessage;
import com.taotao.cloud.stream.framework.trigger.model.TimeExecuteConstant;
import com.taotao.cloud.stream.framework.trigger.model.TimeTriggerMsg;
import com.taotao.cloud.stream.framework.trigger.util.DelayQueueTools;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 小程序直播间业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:03:04
 */
@Service
public class StudioServiceImpl
        extends BaseSuperServiceImpl< Studio, Long, StudioMapper,StudioRepository, StudioRepository>
        implements StudioService {

    @Autowired
    private WechatLivePlayerUtil wechatLivePlayerUtil;

    @Autowired
    private StudioCommodityService studioCommodityService;

    @Resource
    private CommodityMapper commodityMapper;

    @Autowired
    private TimeTrigger timeTrigger;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Autowired
    private GoodsService goodsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(Studio studio) {
        studio.setStoreId(SecurityUtils.getCurrentUser().getStoreId());
        // 创建小程序直播
        Map<String, String> roomMap = wechatLivePlayerUtil.create(studio);
        studio.setRoomId(Convert.toInt(roomMap.get("roomId")));
        studio.setQrCodeUrl(roomMap.get("qrcodeUrl"));
        studio.setStatus(StudioStatusEnum.NEW.name());
        // 直播间添加成功发送直播间开启、关闭延时任务
        if (this.save(studio)) {
            // 直播开启延时任务
            BroadcastMessage broadcastMessage = new BroadcastMessage(studio.getId(), StudioStatusEnum.START.name());
            TimeTriggerMsg timeTriggerMsg = new TimeTriggerMsg(
                    TimeExecuteConstant.BROADCAST_EXECUTOR,
                    Long.parseLong(studio.getStartTime()) * 1000L,
                    broadcastMessage,
                    DelayQueueTools.wrapperUniqueKey(DelayTypeEnums.BROADCAST, String.valueOf(studio.getId())),
                    rocketmqCustomProperties.getPromotionTopic());

            // 发送促销活动开始的延时任务
            this.timeTrigger.addDelay(timeTriggerMsg);

            // 直播结束延时任务
            broadcastMessage = new BroadcastMessage(studio.getId(), StudioStatusEnum.END.name());
            timeTriggerMsg = new TimeTriggerMsg(
                    TimeExecuteConstant.BROADCAST_EXECUTOR,
                    Long.parseLong(studio.getEndTime()) * 1000L,
                    broadcastMessage,
                    DelayQueueTools.wrapperUniqueKey(DelayTypeEnums.BROADCAST, String.valueOf(studio.getId())),
                    rocketmqCustomProperties.getPromotionTopic());

            // 发送促销活动开始的延时任务
            this.timeTrigger.addDelay(timeTriggerMsg);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(Studio studio) {
        Studio oldStudio = this.getById(studio.getId());
        wechatLivePlayerUtil.editRoom(studio);
        if (this.updateById(studio)) {
            // 发送更新延时任务
            // 直播间开始
            BroadcastMessage broadcastMessage = new BroadcastMessage(studio.getId(), StudioStatusEnum.START.name());
            this.timeTrigger.edit(
                    TimeExecuteConstant.BROADCAST_EXECUTOR,
                    broadcastMessage,
                    Long.parseLong(oldStudio.getStartTime()) * 1000L,
                    Long.parseLong(studio.getStartTime()) * 1000L,
                    DelayQueueTools.wrapperUniqueKey(DelayTypeEnums.BROADCAST, String.valueOf(studio.getId())),
                    DateUtils.getDelayTime(Long.parseLong(studio.getStartTime())),
                    rocketmqCustomProperties.getPromotionTopic());

            // 直播间结束
            broadcastMessage = new BroadcastMessage(studio.getId(), StudioStatusEnum.START.name());
            this.timeTrigger.edit(
                    TimeExecuteConstant.BROADCAST_EXECUTOR,
                    broadcastMessage,
                    Long.parseLong(oldStudio.getEndTime()) * 1000L,
                    Long.parseLong(studio.getEndTime()) * 1000L,
                    DelayQueueTools.wrapperUniqueKey(DelayTypeEnums.BROADCAST, String.valueOf(studio.getId())),
                    DateUtils.getDelayTime(Long.parseLong(studio.getEndTime())),
                    rocketmqCustomProperties.getPromotionTopic());
        }
        return true;
    }

    @Override
    public StudioCommodityVO getStudioVO(Long id) {
        StudioCommodityVO studioCommodityVO = new StudioCommodityVO();
        Studio studio = this.getById(id);
        // 获取直播间信息
        BeanUtils.copyProperties(studio, studioCommodityVO);
        // 获取直播间商品信息
        List<Commodity> commodities = commodityMapper.getCommodityByRoomId(studioCommodityVO.getRoomId());
        studioCommodityVO.setCommodityList(OrikaUtils.converts(commodities, CommodityVO.class));
        return studioCommodityVO;
    }

    @Override
    public String getLiveInfo(Integer roomId) {
        Studio studio = this.getByRoomId(roomId);
        // 获取直播间并判断回放内容是否为空，如果为空则获取直播间回放并保存
        if (studio.getMediaUrl() != null) {
            return studio.getMediaUrl();
        } else {
            String mediaUrl = wechatLivePlayerUtil.getLiveInfo(roomId);
            studio.setMediaUrl(mediaUrl);
            this.save(studio);
            return mediaUrl;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean push(Integer roomId, Long goodsId, Long storeId) {
        // 判断直播间是否已添加商品
        if (studioCommodityService.getOne(new LambdaQueryWrapper<StudioCommodity>()
                        .eq(StudioCommodity::getRoomId, roomId)
                        .eq(StudioCommodity::getGoodsId, goodsId))
                != null) {
            throw new BusinessException(ResultEnum.STODIO_GOODS_EXIST_ERROR);
        }

        Goods goods = goodsService.getOne(
                new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodsId).eq(Goods::getStoreId, storeId));
        if (goods == null) {
            throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
        }

        // 调用微信接口添加直播间商品并进行记录
        if (boolean.TRUE.equals(wechatLivePlayerUtil.pushGoods(roomId, goodsId))) {
            // studioCommodityService.save(new StudioCommodity(roomId, goodsId));
            // 添加直播间商品数量
            Studio studio = this.getByRoomId(roomId);
            studio.setRoomGoodsNum(studio.getRoomGoodsNum() != null ? studio.getRoomGoodsNum() + 1 : 1);
            // 设置直播间默认的商品（前台展示）只展示两个
            if (studio.getRoomGoodsNum() < 3) {
                studio.setRoomGoodsList(JSONUtil.toJsonStr(commodityMapper.getSimpleCommodityByRoomId(roomId)));
            }
            return this.updateById(studio);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean goodsDeleteInRoom(Integer roomId, Long goodsId, Long storeId) {
        Goods goods = goodsService.getOne(
                new LambdaQueryWrapper<Goods>().eq(Goods::getId, goodsId).eq(Goods::getStoreId, storeId));
        if (goods == null) {
            throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
        }
        // 调用微信接口删除直播间商品并进行记录
        if (boolean.TRUE.equals(wechatLivePlayerUtil.goodsDeleteInRoom(roomId, goodsId))) {
            studioCommodityService.remove(
                    new QueryWrapper<StudioCommodity>().eq("room_id", roomId).eq("goods_id", goodsId));
            // 减少直播间商品数量
            Studio studio = this.getByRoomId(roomId);
            studio.setRoomGoodsNum(studio.getRoomGoodsNum() - 1);
            // 设置直播间默认的商品（前台展示）只展示两个
            if (studio.getRoomGoodsNum() < 3) {
                studio.setRoomGoodsList(JSONUtil.toJsonStr(commodityMapper.getSimpleCommodityByRoomId(roomId)));
            }
            return this.updateById(studio);
        }
        return false;
    }

    @Override
    public IPage<Studio> studioList(PageQuery PageQuery, Integer recommend, String status) {
        QueryWrapper<Studio> queryWrapper = new QueryWrapper<Studio>()
                .eq(recommend != null, "recommend", true)
                .eq(status != null, "status", status)
                .orderByDesc("create_time");
        // if (UserContext.getCurrentUser() != null && UserContext.getCurrentUser().getRole()
        //	.equals(UserEnums.STORE)) {
        //	queryWrapper.eq("store_id", UserContext.getCurrentUser().getStoreId());
        // }
        return this.page(PageQuery.buildMpPage(), queryWrapper);
    }

    @Override
    public boolean updateStudioStatus(BroadcastMessage broadcastMessage) {
        return this.update(new LambdaUpdateWrapper<Studio>()
                .eq(Studio::getId, broadcastMessage.getStudioId())
                .set(Studio::getStatus, broadcastMessage.getStatus()));
    }

    /**
     * 根据直播间ID获取直播间
     *
     * @param roomId 直播间ID
     * @return 直播间
     */
    private Studio getByRoomId(Integer roomId) {
        return this.getOne(new LambdaQueryWrapper<Studio>().eq(Studio::getRoomId, roomId));
    }
}
