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

package com.taotao.cloud.sys.application.config.aop.point;

import com.taotao.boot.common.utils.log.LogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 积分操作切面
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-31 10:50:13
 */
@Aspect
@Component
public class PointLogAspect {
//
//    @Autowired
//    private IMemberPointsHistoryService memberPointsHistoryService;
//
//    @Autowired
//    private IMemberService memberService;
//
//    @After("@annotation(com.taotao.cloud.member.biz.aop.point.PointLogPoint)")
//    public void doAfter(JoinPoint pjp) {
//        // 参数
//        Object[] obj = pjp.getArgs();
//        try {
//            // 变动积分
//            Long point = 0L;
//            if (obj[0] != null) {
//                point = Long.valueOf(obj[0].toString());
//            }
//            // 变动类型
//            String type = PointTypeEnum.INCREASE.name();
//            if (obj[1] != null) {
//                type = obj[1].toString();
//            }
//            // 会员ID
//            String memberId = "";
//            if (obj[2] != null) {
//                memberId = obj[2].toString();
//            }
//            // 变动积分为0，则直接返回
//            if (point == 0) {
//                return;
//            }
//
//            // 根据会员id查询会员信息
//            Member member = memberService.getById(memberId);
//            if (member != null) {
//                MemberPointsHistory memberPointsHistory = new MemberPointsHistory();
//                memberPointsHistory.setMemberId(member.getId());
//                memberPointsHistory.setMemberName(member.getUsername());
//                memberPointsHistory.setPointType(type);
//
//                memberPointsHistory.setVariablePoint(point);
//                if (type.equals(PointTypeEnum.INCREASE.name())) {
//                    memberPointsHistory.setBeforePoint(member.getPoint() - point);
//                } else {
//                    memberPointsHistory.setBeforePoint(member.getPoint() + point);
//                }
//
//                memberPointsHistory.setPoint(member.getPoint());
//                memberPointsHistory.setContent(obj[3] == null ? "" : obj[3].toString());
//                // 系统
//                memberPointsHistory.setCreateBy(0L);
//                memberPointsHistoryService.save(memberPointsHistory);
//            }
//        } catch (Exception e) {
//            LogUtils.error("积分操作错误", e);
//        }
//    }
}
