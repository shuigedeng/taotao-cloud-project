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

package com.taotao.cloud.bff.api.service.mp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.report.api.model.dto.StatisticsQueryParam;
import com.taotao.cloud.report.api.model.vo.OnlineMemberVO;
import com.taotao.cloud.report.api.model.vo.PlatformViewVO;
import java.util.List;

/** 平台PV统计 */
public interface IPlatformViewService extends IService<PlatformViewData> {

    /**
     * 当前在线人数
     *
     * @return
     */
    Long online();

    /**
     * 会员分布
     *
     * @return
     */
    List<MemberDistributionVO> memberDistribution();

    /**
     * 在线人数记录
     *
     * @return
     */
    List<OnlineMemberVO> statisticsOnline();

    /**
     * 数据查询
     *
     * @param queryParam
     * @return
     */
    List<PlatformViewVO> list(StatisticsQueryParam queryParam);

    /**
     * 查询累计访客数
     *
     * @param queryParam
     * @return
     */
    Integer countUv(StatisticsQueryParam queryParam);
}
