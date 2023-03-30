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

package com.taotao.cloud.wechat.biz.mp.service.statistics;

import java.time.LocalDateTime;
import java.util.List;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeInterfaceResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeMsgResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;

/**
 * 公众号统计 Service 接口
 *
 * @author 芋道源码
 */
public interface MpStatisticsService {

    /**
     * 获取粉丝增减数据
     *
     * @param accountId 公众号账号编号
     * @param date 时间区间
     * @return 粉丝增减数据
     */
    List<WxDataCubeUserSummary> getUserSummary(Long accountId, LocalDateTime[] date);

    /**
     * 获取粉丝累计数据
     *
     * @param accountId 公众号账号编号
     * @param date 时间区间
     * @return 粉丝累计数据
     */
    List<WxDataCubeUserCumulate> getUserCumulate(Long accountId, LocalDateTime[] date);

    /**
     * 获取消息发送概况数据
     *
     * @param accountId 公众号账号编号
     * @param date 时间区间
     * @return 消息发送概况数据
     */
    List<WxDataCubeMsgResult> getUpstreamMessage(Long accountId, LocalDateTime[] date);

    /**
     * 获取接口分析数据
     *
     * @param accountId 公众号账号编号
     * @param date 时间区间
     * @return 接口分析数据
     */
    List<WxDataCubeInterfaceResult> getInterfaceSummary(Long accountId, LocalDateTime[] date);
}
