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
 * 会员等级表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:16:55
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberGrade.TABLE_NAME)
@TableName(MemberGrade.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberGrade.TABLE_NAME, comment = "会员等级表")
public class MemberGrade extends BaseSuperEntity<MemberGrade, Long> {

    public static final String TABLE_NAME = "tt_member_grade";

    /** 等级名称 */
    @Column(name = "grade_name", columnDefinition = "varchar(255) not null comment '等级名称'")
    private String gradeName;

    /** 等级图片 */
    @Column(name = "grade_image", columnDefinition = "varchar(1024) not null comment '等级图片'")
    private String gradeImage;

    /** 所需经验值 */
    @Column(name = "experience_value", columnDefinition = "int not null default 0 comment '所需经验值'")
    private Integer experienceValue;

    /** 是否为默认等级 */
    @Column(name = "defaulted", columnDefinition = "boolean not null default false comment '是否为默认等级'")
    private Boolean defaulted;
}
