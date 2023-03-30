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

package com.taotao.cloud.wechat.biz.mp.convert.statistics;

import cn.iocoder.yudao.module.mp.controller.admin.statistics.vo.MpStatisticsInterfaceSummaryRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.statistics.vo.MpStatisticsUpstreamMessageRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.statistics.vo.MpStatisticsUserCumulateRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.statistics.vo.MpStatisticsUserSummaryRespVO;
import java.util.List;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeInterfaceResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeMsgResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MpStatisticsConvert {

    MpStatisticsConvert INSTANCE = Mappers.getMapper(MpStatisticsConvert.class);

    List<MpStatisticsUserSummaryRespVO> convertList01(List<WxDataCubeUserSummary> list);

    List<MpStatisticsUserCumulateRespVO> convertList02(List<WxDataCubeUserCumulate> list);

    List<MpStatisticsUpstreamMessageRespVO> convertList03(List<WxDataCubeMsgResult> list);

    @Mappings({
        @Mapping(source = "refDate", target = "refDate", dateFormat = "yyyy-MM-dd"),
        @Mapping(source = "msgUser", target = "messageUser"),
        @Mapping(source = "msgCount", target = "messageCount"),
    })
    MpStatisticsUpstreamMessageRespVO convert(WxDataCubeMsgResult bean);

    List<MpStatisticsInterfaceSummaryRespVO> convertList04(List<WxDataCubeInterfaceResult> list);

    @Mapping(source = "refDate", target = "refDate", dateFormat = "yyyy-MM-dd")
    MpStatisticsInterfaceSummaryRespVO convert(WxDataCubeInterfaceResult bean);
}
