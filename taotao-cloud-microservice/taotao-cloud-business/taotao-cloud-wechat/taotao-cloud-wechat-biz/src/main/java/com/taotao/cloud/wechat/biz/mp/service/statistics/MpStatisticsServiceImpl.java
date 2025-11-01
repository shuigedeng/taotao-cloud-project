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

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.mp.enums.ErrorCodeConstants.*;

import cn.iocoder.yudao.module.mp.framework.mp.core.MpServiceFactory;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeInterfaceResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeMsgResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 公众号统计 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class MpStatisticsServiceImpl implements MpStatisticsService {

    @Resource
    @Lazy // 延迟加载，解决循环依赖的问题
    private MpServiceFactory mpServiceFactory;

    @Override
    public List<WxDataCubeUserSummary> getUserSummary(Long accountId, LocalDateTime[] date) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        try {
            return mpService.getDataCubeService().getUserSummary(DateUtil.date(date[0]), DateUtil.date(date[1]));
        } catch (WxErrorException e) {
            throw exception(STATISTICS_GET_USER_SUMMARY_FAIL, e.getError().getErrorMsg());
        }
    }

    @Override
    public List<WxDataCubeUserCumulate> getUserCumulate(Long accountId, LocalDateTime[] date) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        try {
            return mpService.getDataCubeService().getUserCumulate(DateUtil.date(date[0]), DateUtil.date(date[1]));
        } catch (WxErrorException e) {
            throw exception(STATISTICS_GET_USER_CUMULATE_FAIL, e.getError().getErrorMsg());
        }
    }

    @Override
    public List<WxDataCubeMsgResult> getUpstreamMessage(Long accountId, LocalDateTime[] date) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        try {
            return mpService.getDataCubeService().getUpstreamMsg(DateUtil.date(date[0]), DateUtil.date(date[1]));
        } catch (WxErrorException e) {
            throw exception(STATISTICS_GET_UPSTREAM_MESSAGE_FAIL, e.getError().getErrorMsg());
        }
    }

    @Override
    public List<WxDataCubeInterfaceResult> getInterfaceSummary(Long accountId, LocalDateTime[] date) {
        WxMpService mpService = mpServiceFactory.getRequiredMpService(accountId);
        try {
            return mpService.getDataCubeService().getInterfaceSummary(DateUtil.date(date[0]), DateUtil.date(date[1]));
        } catch (WxErrorException e) {
            throw exception(STATISTICS_GET_INTERFACE_SUMMARY_FAIL, e.getError().getErrorMsg());
        }
    }
}
