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

package com.taotao.cloud.report.api.feign.fallback;

import com.taotao.cloud.report.api.feign.IFeignMemberStatisticsApi;
import com.taotao.cloud.report.api.model.dto.MemberStatisticsDTO;
import com.taotao.cloud.report.api.model.vo.MemberStatisticsVO;
import java.util.Date;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignMemberStatisticsFallbackImpl implements FallbackFactory<IFeignMemberStatisticsApi> {

    @Override
    public IFeignMemberStatisticsApi create(Throwable throwable) {
        return new IFeignMemberStatisticsApi() {
            @Override
            public MemberStatisticsVO findMemberStatistics() {
                return null;
            }

            @Override
            public Boolean saveMemberStatistics(MemberStatisticsDTO memberStatisticsDTO) {
                return null;
            }

            @Override
            public Long newlyAdded(Date startTime, Date endTime) {
                return null;
            }

            @Override
            public Long activeQuantity(Date startTime) {
                return null;
            }

            @Override
            public Long memberCount(Date endTime) {
                return null;
            }
        };
    }
}
