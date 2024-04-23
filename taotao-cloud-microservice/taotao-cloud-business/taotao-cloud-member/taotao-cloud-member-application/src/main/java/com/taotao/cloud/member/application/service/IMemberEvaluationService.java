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

package com.taotao.cloud.member.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 会员商品评价业务层
 *
 * @since 2020-02-25 14:10:16
 */
public interface IMemberEvaluationService extends IService<MemberEvaluation> {

    /**
     * 查询会员的评价分页列表
     *
     * @param evaluationPageQuery 评价查询
     * @return 评价分页
     */
    IPage<MemberEvaluation> managerQuery(EvaluationPageQuery evaluationPageQuery);

    /**
     * 查询评价分页列表
     *
     * @param evaluationPageQuery 评价查询条件
     * @return 评价分页列表
     */
    IPage<MemberEvaluation> queryPage(EvaluationPageQuery evaluationPageQuery);

    /**
     * 添加会员评价 1.检测用户是否重复评价 2.获取评价相关信息添加评价 3.修改子订单为已评价状态 4.发送用户评价消息修改商品的评价数量以及好评率
     *
     * @param memberEvaluationDTO 评论
     * @return 操作状态
     */
    Boolean addMemberEvaluation(MemberEvaluationDTO memberEvaluationDTO);

    /**
     * 根据ID查询会员评价
     *
     * @param id 评价ID
     * @return 会员评价
     */
    MemberEvaluation queryById(Long id);

    /**
     * 更改评论状态
     *
     * @param id 评价ID
     * @param status 状态
     * @return 会员评价
     */
    Boolean updateStatus(Long id, String status);

    /**
     * 删除评论
     *
     * @param id 评论ID
     * @return 操作状态
     */
    Boolean delete(Long id);

    /**
     * 商家回复评价
     *
     * @param id 评价ID
     * @param reply 回复内容
     * @param replyImage 回复图片
     * @return 操作状态
     */
    Boolean reply(Long id, String reply, String replyImage);

    /**
     * 获取商品评价数量
     *
     * @param goodsId 商品ID
     * @return 评价数量数据
     */
    EvaluationNumberVO getEvaluationNumber(Long goodsId);
}
