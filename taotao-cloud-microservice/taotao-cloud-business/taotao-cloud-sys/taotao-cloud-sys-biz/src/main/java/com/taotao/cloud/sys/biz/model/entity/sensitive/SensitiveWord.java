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

package com.taotao.cloud.sys.biz.model.entity.sensitive;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

/**
 * 敏感词实体
 *
 * @author shuigedeng
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true, fluent = false)
@Entity
@Table(name = SensitiveWord.TABLE_NAME)
@TableName(SensitiveWord.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = SensitiveWord.TABLE_NAME, comment = "敏感词表")
public class SensitiveWord extends BaseSuperEntity<SensitiveWord, Long> {

    public static final String TABLE_NAME = "tt_sensitive_words";

    /** 敏感词名称 */
    @Column(name = "sensitive_word", columnDefinition = "varchar(255) not null default '' comment '敏感词名称'")
    private String sensitiveWord;

//    @Builder
//    public SensitiveWord(
//            Long id,
//            LocalDateTime createTime,
//            Long createBy,
//            LocalDateTime updateTime,
//            Long updateBy,
//            Integer version,
//            Boolean delFlag,
//            String sensitiveWord) {
//        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
//        this.sensitiveWord = sensitiveWord;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        SensitiveWord that = (SensitiveWord) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
