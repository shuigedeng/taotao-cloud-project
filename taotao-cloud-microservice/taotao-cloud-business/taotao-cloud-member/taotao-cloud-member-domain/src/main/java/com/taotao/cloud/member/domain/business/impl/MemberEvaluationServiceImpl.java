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

package com.taotao.cloud.member.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.enums.SwitchEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.support.tuple.Tuple2;
import com.taotao.cloud.common.support.tuple.Tuple3;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import com.taotao.cloud.member.api.enums.EvaluationGradeEnum;
import com.taotao.cloud.member.sys.model.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.sys.model.page.EvaluationPageQuery;
import com.taotao.cloud.member.sys.model.vo.EvaluationNumberVO;
import com.taotao.cloud.member.biz.mapper.IMemberEvaluationMapper;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.model.entity.MemberEvaluation;
import com.taotao.cloud.member.biz.service.business.IMemberEvaluationService;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import com.taotao.cloud.member.biz.utils.QueryUtils;
import com.taotao.cloud.mq.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.mq.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.mq.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.feign.IFeignOrderApi;
import com.taotao.cloud.order.api.feign.IFeignOrderItemApi;
import com.taotao.cloud.order.api.model.vo.order.OrderItemVO;
import com.taotao.cloud.order.api.model.vo.order.OrderVO;
import com.taotao.cloud.sensitive.word.SensitiveWordsFilter;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员商品评价业务层实现
 *
 * @since 2020-02-25 14:10:16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberEvaluationServiceImpl extends ServiceImpl<IMemberEvaluationMapper, MemberEvaluation>
        implements IMemberEvaluationService {

    /** 会员 */
    @Autowired
    private IMemberService memberService;
    /** 订单 */
    @Autowired
    private IFeignOrderApi orderApi;
    /** 子订单 */
    @Autowired
    private IFeignOrderItemApi orderItemApi;
    /** 商品 */
    @Autowired
    private IFeignGoodsSkuApi goodsSkuApi;
    /** rocketMq */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    /** rocketMq配置 */
    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Resource
    private ThreadPoolExecutor asyncThreadPoolExecutor;

    @Override
    public IPage<MemberEvaluation> managerQuery(EvaluationPageQuery queryParams) {
        // 获取评价分页
        return this.page(queryParams.buildMpPage(), QueryUtils.evaluationQueryWrapper(queryParams));
    }

    @Override
    public IPage<MemberEvaluation> queryPage(EvaluationPageQuery evaluationPageQuery) {
        return this.baseMapper.getMemberEvaluationList(
                evaluationPageQuery.buildMpPage(), QueryUtils.evaluationQueryWrapper(evaluationPageQuery));
    }

    @Override
    public Boolean addMemberEvaluation(MemberEvaluationDTO memberEvaluationDTO) {
        // 获取用户信息
        Member member = memberService.getUserInfo();

        // 获取商品信息
        CompletableFuture<GoodsSkuSpecGalleryVO> future1 = CompletableFuture.supplyAsync(
                () -> goodsSkuApi.getGoodsSkuByIdFromCache(memberEvaluationDTO.getSkuId()), asyncThreadPoolExecutor);
        // 获取订单信息
        CompletableFuture<Tuple2<OrderItemVO, OrderVO>> future2 = CompletableFuture.supplyAsync(
                        () -> orderItemApi.getBySn(memberEvaluationDTO.getOrderItemSn()), asyncThreadPoolExecutor)
                .thenApplyAsync(
                        (orderItem) -> {
                            OrderVO order = orderApi.getBySn(orderItem.orderSn());
                            return new Tuple2<>(orderItem, order);
                        },
                        asyncThreadPoolExecutor);

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(future1, future2);
        CompletableFuture<Tuple3<GoodsSkuSpecGalleryVO, OrderItemVO, OrderVO>> resultCompletableFuture =
                allOfFuture.thenApply(v -> {
                    try {
                        Tuple2<OrderItemVO, OrderVO> join = future2.join();
                        return new Tuple3<>(future1.join(), join._1(), join._2());
                    } catch (Exception e) {
                        LogUtils.error("RemoteDictService.getDictDataAsync Exception" + " dictId = {}", e);
                        throw new RuntimeException(e);
                    }
                });

        Tuple3<GoodsSkuSpecGalleryVO, OrderItemVO, OrderVO> data = resultCompletableFuture.join();
        GoodsSkuSpecGalleryVO goodsSku = data._1();
        OrderItemVO orderItem = data._2();
        OrderVO order = data._3();

        // 检测是否可以添加会员评价
        checkMemberEvaluation(orderItem, order);
        // 新增用户评价
        MemberEvaluation memberEvaluation = new MemberEvaluation(memberEvaluationDTO, goodsSku, member, order);
        // 过滤商品咨询敏感词
        memberEvaluation.setContent(SensitiveWordsFilter.filter(memberEvaluation.getContent()));
        // 添加评价
        this.save(memberEvaluation);

        // 修改订单货物评价状态为已评价
        orderItemApi.updateCommentStatus(orderItem.sn(), CommentStatusEnum.FINISHED);
        // 发送商品评价消息
        String destination =
                rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.GOODS_COMMENT_COMPLETE.name();
        rocketMQTemplate.asyncSend(
                destination, JSONUtil.toJsonStr(memberEvaluation), RocketmqSendCallbackBuilder.commonCallback());
        return true;
    }

    @Override
    public MemberEvaluation queryById(Long id) {
        return this.getById(id);
    }

    @Override
    public Boolean updateStatus(Long id, String status) {
        UpdateWrapper<MemberEvaluation> updateWrapper = Wrappers.update();
        updateWrapper.eq("id", id);
        updateWrapper.set(
                "status", status.equals(SwitchEnum.OPEN.name()) ? SwitchEnum.OPEN.name() : SwitchEnum.CLOSE.name());
        return this.update(updateWrapper);
    }

    @Override
    public Boolean delete(Long id) {
        LambdaUpdateWrapper<MemberEvaluation> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MemberEvaluation::getDelFlag, true);
        updateWrapper.eq(MemberEvaluation::getId, id);
        return this.update(updateWrapper);
    }

    @Override
    public Boolean reply(Long id, String reply, String replyImage) {
        UpdateWrapper<MemberEvaluation> updateWrapper = Wrappers.update();
        updateWrapper.set("reply_status", true);
        updateWrapper.set("reply", reply);
        if (StringUtils.isNotBlank(replyImage)) {
            updateWrapper.set("have_reply_image", true);
            updateWrapper.set("reply_image", replyImage);
        }
        updateWrapper.eq("id", id);
        return this.update(updateWrapper);
    }

    @Override
    public EvaluationNumberVO getEvaluationNumber(Long goodsId) {
        EvaluationNumberVO evaluationNumberVO = new EvaluationNumberVO();
        List<Map<String, Object>> list = this.baseMapper.getEvaluationNumber(goodsId);

        int good = 0;
        int moderate = 0;
        int worse = 0;
        for (Map<String, Object> map : list) {
            if (map.get("grade").equals(EvaluationGradeEnum.GOOD.name())) {
                good = Integer.parseInt(map.get("num").toString());
            } else if (map.get("grade").equals(EvaluationGradeEnum.MODERATE.name())) {
                moderate = Integer.parseInt(map.get("num").toString());
            } else if (map.get("grade").equals(EvaluationGradeEnum.WORSE.name())) {
                worse = Integer.parseInt(map.get("num").toString());
            }
        }
        evaluationNumberVO.setAll(good + moderate + worse);
        evaluationNumberVO.setGood(good);
        evaluationNumberVO.setModerate(moderate);
        evaluationNumberVO.setWorse(worse);
        evaluationNumberVO.setHaveImage(this.count(
                new QueryWrapper<MemberEvaluation>().eq("have_image", 1).eq("goods_id", goodsId)));

        return evaluationNumberVO;
    }

    /**
     * 检测会员评价
     *
     * @param orderItem 子订单
     * @param order 订单
     */
    public void checkMemberEvaluation(OrderItemVO orderItem, OrderVO order) {
        // 根据子订单编号判断是否评价过
        if (orderItem.commentStatus().equals(CommentStatusEnum.FINISHED.name())) {
            throw new BusinessException("已评价");
        }

        // 判断是否是当前会员的订单
        if (!order.orderBase().memberId().equals(SecurityUtils.getUserId())) {
            throw new BusinessException(ResultEnum.ORDER_NOT_USER);
        }
    }
}
