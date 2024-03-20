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

import com.taotao.cloud.common.model.BaseSecurityUser;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.member.api.feign.response.FeignMemberResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午4:10
 */
public class FeignMemberApiFallback implements FallbackFactory<IFeignMemberApi> {

    @Override
    public IFeignMemberApi create(Throwable throwable) {
        return new IFeignMemberApi() {

            @Override
            public BaseSecurityUser getMemberSecurityUser(String nicknameOrUserNameOrPhoneOrEmail) {
                return null;
            }

            @Override
            public FeignMemberResponse findMemberById(Long id) {
                return null;
            }

            @Override
            public Boolean updateMemberPoint(Long payPoint, String name, Long memberId, String s) {
                return null;
            }

            @Override
            public FeignMemberResponse findByUsername(String username) {
                return null;
            }

            @Override
            public FeignMemberResponse getById(Long memberId) {
                return null;
            }

            @Override
            public Boolean update(Long memberId, Long sotreId) {
                return false;
            }

            @Override
            public Boolean updateById(FeignMemberResponse member) {
                return false;
            }

            @Override
            public List<Map<String, Object>> listFieldsByMemberIds(String s, List<String> ids) {
                return null;
            }
        };
    }
}
