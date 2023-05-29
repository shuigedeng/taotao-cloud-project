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

package com.taotao.cloud.goods.biz.model.entity;

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

/**
 * 商品评论表
 *
 * <p>todo 暂时未用 需要添加注释
 *
 * @author shuigedeng
 * @since 2020/4/30 16:06
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GoodsComment.TABLE_NAME)
@TableName(GoodsComment.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = GoodsComment.TABLE_NAME, comment = "商品评论表")
public class GoodsComment extends BaseSuperEntity<GoodsComment, Long> {

    public static final String TABLE_NAME = "tt_goods_comment";

    @Column(name = "goods_spec_ame", columnDefinition = "varchar(255) not null comment '会员ID'")
    private String goodsSpecName;

    @Column(name = "mall_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long mallId;

    @Column(name = "scene_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long sceneId;

    @Column(name = "customer_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long customerId;

    @Column(name = "member_nick", columnDefinition = "varchar(255) not null comment '会员ID'")
    private String memberNick;

    @Column(name = "member_avatar", columnDefinition = "varchar(255) not null comment '会员ID'")
    private String memberAvatar;

    @Column(name = "order_code", columnDefinition = "varchar(255) not null comment '会员ID'")
    private String orderCode;

    @Column(name = "type", columnDefinition = "int not null comment '会员ID'")
    private Integer type;

    @Column(name = "rank", columnDefinition = "int not null comment '会员ID'")
    private Integer rank;

    @Column(name = "has_image", columnDefinition = "int not null comment '会员ID'")
    private Integer hasImage;

    @Column(name = "comment_pic_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long commentPicId;

    @Column(name = "has_sen_word", columnDefinition = "int not null comment '会员ID'")
    private Integer hasSenWord;

    @Column(name = "origin_content", columnDefinition = "varchar(255) not null comment '会员ID'")
    private String originContent;

    @Column(name = "filter_content", columnDefinition = "varchar(255) not null comment '会员ID'")
    private String filterContent;

    @Column(name = "op_type", columnDefinition = "int not null comment '会员ID'")
    private Integer opType;

    @Column(name = "reply_status", columnDefinition = "int not null comment '会员ID'")
    private Integer replyStatus;

    @Column(name = "reply_content", columnDefinition = "varchar(255) not null comment '会员ID'")
    private String replyContent;

    @Column(name = "reply_ori_content", columnDefinition = "varchar(255) not null comment '会员ID'")
    private String replyOriContent;

    @Column(name = "reply_time", columnDefinition = "datetime not null comment '会员ID'")
    private LocalDateTime replyTime;

    @Column(name = "reply_user_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long replyUserId;

    @Column(name = "reply_pic_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long replyPicId;

    @Column(name = "has_add", columnDefinition = "int not null comment '会员ID'")
    private Integer hasAdd;

    @Column(name = "after_days", columnDefinition = "int not null comment '会员ID'")
    private Integer afterDays;

    @Column(name = "append_time", columnDefinition = "datetime not null comment '会员ID'")
    private LocalDateTime appendTime;

    @Column(name = "status", columnDefinition = "int not null comment '会员ID'")
    private Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        GoodsComment that = (GoodsComment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
