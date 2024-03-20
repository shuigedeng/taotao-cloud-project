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

package com.taotao.cloud.member.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员店铺收藏表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:42:32
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberStoreCollection.TABLE_NAME)
@TableName(MemberStoreCollection.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberStoreCollection.TABLE_NAME, comment = "会员店铺收藏表")
public class MemberStoreCollection extends BaseSuperEntity<MemberStoreCollection, Long> {

    public static final String TABLE_NAME = "tt_member_store_collection";

    /** 会员id */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员id'")
    private Long memberId;

    /** 店铺id */
    @Column(name = "store_id", columnDefinition = "bigint not null comment '店铺id'")
    private Long storeId;
}
