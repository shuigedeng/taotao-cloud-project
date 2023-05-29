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

package com.taotao.cloud.sys.biz.model.entity.system;

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
import org.hibernate.Hibernate;

/** 服务订阅消息 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ServiceNotice.TABLE_NAME)
@TableName(ServiceNotice.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = ServiceNotice.TABLE_NAME, comment = "服务订阅消息表")
public class ServiceNotice extends BaseSuperEntity<ServiceNotice, Long> {

    public static final String TABLE_NAME = "tt_service_notice";

    @Column(name = "store_id", columnDefinition = "varchar(255) not null default '' comment '商家id，为-1时，代表是平台发布的消息'")
    private String storeId;

    @Column(name = "banner_image", columnDefinition = "varchar(255) not null default '' comment 'banner图'")
    private String bannerImage;

    @Column(name = "title", columnDefinition = "varchar(255) not null default '' comment '标题'")
    private String title;

    @Column(name = "sub_title", columnDefinition = "varchar(255) not null default '' comment '副标题'")
    private String subTitle;

    @Column(name = "to_url", columnDefinition = "varchar(255) not null default '' comment '点击跳转（此内容与站内信内容只能有一个生效）'")
    private String toUrl;

    @Column(name = "content", columnDefinition = "varchar(255) not null default '' comment '站内信内容(富文本框编辑，可以上传图片的html)'")
    private String content;

    @Builder
    public ServiceNotice(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String storeId,
            String bannerImage,
            String title,
            String subTitle,
            String toUrl,
            String content) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.storeId = storeId;
        this.bannerImage = bannerImage;
        this.title = title;
        this.subTitle = subTitle;
        this.toUrl = toUrl;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ServiceNotice that = (ServiceNotice) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
