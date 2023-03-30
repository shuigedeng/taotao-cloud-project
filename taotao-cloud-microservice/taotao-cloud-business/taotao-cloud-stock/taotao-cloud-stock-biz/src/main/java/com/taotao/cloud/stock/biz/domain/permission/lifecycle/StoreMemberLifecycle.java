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

package com.taotao.cloud.stock.biz.domain.permission.lifecycle;

import static org.mallfoundry.store.StoreLifecycle.POSITION_STEP;

import org.mallfoundry.member.MemberService;
import org.mallfoundry.store.Store;
import org.mallfoundry.store.StoreLifecycle;
import org.springframework.core.annotation.Order;

@Order(POSITION_STEP * 3)
public class StoreMemberLifecycle implements StoreLifecycle {

    private final MemberService memberService;

    public StoreMemberLifecycle(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void doClose(Store store) {
        this.memberService.clearMembers(store.toId());
    }

    @Override
    public int getPosition() {
        return POSITION_STEP * 3;
    }
}
