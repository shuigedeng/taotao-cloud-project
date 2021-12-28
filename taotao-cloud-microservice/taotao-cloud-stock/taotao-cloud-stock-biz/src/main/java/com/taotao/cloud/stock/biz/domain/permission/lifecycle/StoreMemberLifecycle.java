/*
 * Copyright (C) 2019-2020 the original author or authors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.taotao.cloud.stock.biz.domain.permission.lifecycle;

import org.mallfoundry.member.MemberService;
import org.mallfoundry.store.Store;
import org.mallfoundry.store.StoreLifecycle;
import org.springframework.core.annotation.Order;

import static org.mallfoundry.store.StoreLifecycle.POSITION_STEP;

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
