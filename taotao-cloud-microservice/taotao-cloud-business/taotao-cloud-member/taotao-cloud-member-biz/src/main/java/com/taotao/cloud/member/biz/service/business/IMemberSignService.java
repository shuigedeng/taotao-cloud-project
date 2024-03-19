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

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.sys.model.vo.MemberSignVO;
import com.taotao.cloud.member.biz.model.entity.MemberSign;
import java.util.List;

/** 会员签到业务层 */
public interface IMemberSignService extends IService<MemberSign> {

    /**
     * 会员签到
     *
     * @return 是否签到成功
     */
    Boolean memberSign();

    /**
     * 根据时间查询签到数据 主要是根据年月查询，给会员端日历展示数据
     *
     * @param time 时间 格式 YYYYmm
     * @return 会员签到列表
     */
    List<MemberSignVO> getMonthSignDay(String time);

    /**
     * 会员签到赠送积分
     *
     * @param memberId 会员id
     * @param day 签到天数
     */
    void memberSignSendPoint(Long memberId, Integer day);
}
