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

package com.taotao.cloud.goods.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 商品关键字表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GoodsWords.TABLE_NAME)
@TableName(GoodsWords.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = GoodsWords.TABLE_NAME, comment = "商品关键字表")
public class GoodsWords extends BaseSuperEntity<GoodsWords, Long> {

    public static final String TABLE_NAME = "tt_goods_words";

    /** 商品关键字 */
    @Column(name = "words", columnDefinition = "varchar(255) not null comment '商品关键字'")
    private String words;

    /** 全拼音 */
    @Column(name = "whole_spell", columnDefinition = "varchar(255) not null comment '全拼音'")
    private String wholeSpell;

    /** 缩写 */
    @Column(name = "abbreviate", columnDefinition = "varchar(255) not null comment '缩写'")
    private String abbreviate;

    /** 类型 */
    @Column(name = "type", columnDefinition = "varchar(255) not null comment '类型'")
    private String type;

    /** 排序 */
    @Column(name = "sort", columnDefinition = "int not null default 0  comment '排序'")
    private Integer sort;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        GoodsWords that = (GoodsWords) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
