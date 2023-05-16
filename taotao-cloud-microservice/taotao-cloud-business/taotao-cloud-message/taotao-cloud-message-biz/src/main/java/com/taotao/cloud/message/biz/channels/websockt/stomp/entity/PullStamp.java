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

package com.taotao.cloud.message.biz.channels.websockt.stomp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

/** 信息拉取标记 */
@Schema(name = "拉取标记")
@Entity
@Table(
        name = "msg_pull_stamp",
        indexes = {
            @Index(name = "msg_pull_stamp_id_idx", columnList = "stamp_id"),
            @Index(name = "msg_pull_stamp_sid_idx", columnList = "user_id")
        })
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE,
        region = MessageConstants.REGION_MESSAGE_PULL_STAMP)
public class PullStamp extends AbstractEntity {

    @Id
    @UuidGenerator
    @Column(name = "stamp_id", length = 64)
    private String stampId;

    @Schema(name = "用户ID")
    @Column(name = "user_id", length = 64)
    private String userId;

    @Schema(name = "来源", title = "预留字段，以备支持不同端的情况")
    @Column(name = "source", length = 50)
    private String source;

    @Schema(title = "上次拉取时间")
    @Column(name = "latest_pull_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date latestPullTime = new Date();

    public String getStampId() {
        return stampId;
    }

    public void setStampId(String stampId) {
        this.stampId = stampId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getLatestPullTime() {
        return latestPullTime;
    }

    public void setLatestPullTime(Date latestPullTime) {
        this.latestPullTime = latestPullTime;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("stampId", stampId)
                .add("userId", userId)
                .add("source", source)
                .add("latestPullTime", latestPullTime)
                .toString();
    }
}
