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

package com.taotao.cloud.member.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.member.sys.model.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;

/**
 * 会员积分历史业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-31 14:16:21
 */
public interface IMemberPointsHistoryService extends IService<MemberPointsHistory> {

    /**
     * 获取会员积分VO
     *
     * @param memberId 会员ID
     * @return {@link MemberPointsHistoryVO }
     * @since 2022-05-31 14:16:21
     */
    MemberPointsHistoryVO getMemberPointsHistoryVO(Long memberId);

    /**
     * 通过页面
     *
     * @param pageQuery 页面参数
     * @return {@link IPage }<{@link MemberPointsHistory }>
     * @since 2022-05-31 14:16:22
     */
    IPage<MemberPointsHistory> pageQuery(PageQuery pageQuery);

    /**
     * 会员积分历史
     *
     * @param pageQuery 分页
     * @param memberId 会员ID
     * @param memberName 会员名称
     * @return {@link IPage }<{@link MemberPointsHistory }>
     * @since 2022-05-31 14:16:22
     */
    IPage<MemberPointsHistory> memberPointsHistoryPageQuery(PageQuery pageQuery, Long memberId, String memberName);
}
