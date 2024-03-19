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

package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.member.api.feign.IFeignMemberAddressApi;
import com.taotao.cloud.member.api.feign.response.FeignMemberAddressResponse;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午4:10
 */
public class FeignMemberAddressApiFallback implements FallbackFactory<IFeignMemberAddressApi> {

    @Override
    public IFeignMemberAddressApi create(Throwable throwable) {
        return new IFeignMemberAddressApi() {
            @Override
            public FeignMemberAddressResponse getById(String shippingAddressId) {
                return null;
            }
        };
    }
}
