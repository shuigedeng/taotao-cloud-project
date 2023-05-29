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

package com.taotao.cloud.message.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** 短链接/暂时只用于小程序二维码业务 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ShortLink.TABLE_NAME)
@TableName(ShortLink.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
// @org.hibernate.annotations.Table(appliesTo = ShortLink.TABLE_NAME, comment = "短链接/暂时只用于小程序二维码业务表")
public class ShortLink extends BaseSuperEntity<ShortLink, Long> {

    public static final String TABLE_NAME = "tt_short_link";

    /** 原始参数 */
    @Column(name = "original_params", columnDefinition = "varchar(255) not null default '' comment '原始参数'")
    private String originalParams;
}
