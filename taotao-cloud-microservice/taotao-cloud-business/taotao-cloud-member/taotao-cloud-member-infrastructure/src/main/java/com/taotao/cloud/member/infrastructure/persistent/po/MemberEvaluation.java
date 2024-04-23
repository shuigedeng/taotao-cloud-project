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

package com.taotao.cloud.member.infrastructure.persistent.po;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.SwitchEnum;
import com.taotao.cloud.common.utils.bean.BeanUtils;
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
 * 会员商品评价表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:07:11
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberEvaluation.TABLE_NAME)
@TableName(MemberEvaluation.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberEvaluation.TABLE_NAME, comment = "会员商品评价表")
public class MemberEvaluation extends BaseSuperEntity<MemberEvaluation, Long> {

    public static final String TABLE_NAME = "tt_member_evaluation";

    /** 会员ID */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long memberId;

    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(256) not null comment '会员名称'")
    private String memberName;

    /** 会员头像 */
    @Column(name = "member_profile", columnDefinition = "varchar(1024) comment '会员头像'")
    private String memberProfile;

    /** 店铺ID */
    @Column(name = "store_id", columnDefinition = "bigint comment '店铺ID'")
    private Long storeId;

    /** 店铺名称 */
    @Column(name = "store_name", columnDefinition = "varchar(256) comment '店铺名称'")
    private String storeName;

    /** 商品ID */
    @Column(name = "goods_id", columnDefinition = "bigint not null comment '商品ID'")
    private Long goodsId;

    /** SKU_ID */
    @Column(name = "sku_id", columnDefinition = "bigint not null comment 'SKU_ID'")
    private Long skuId;

    /** 会员ID */
    @Column(name = "goods_name", columnDefinition = "varchar(255) not null comment '商品名称'")
    private String goodsName;

    /** 商品图片 */
    @Column(name = "goods_image", columnDefinition = "varchar(1024) not null comment '商品图片'")
    private String goodsImage;

    /** 订单号 */
    @Column(name = "order_no", columnDefinition = "varchar(255) not null comment '订单号'")
    private String orderNo;

    /** 好中差评 , GOOD：好评，MODERATE：中评，WORSE：差评 */
    @Column(
            name = "grade",
            columnDefinition = "varchar(32) not null default 'GOOD' comment '好中差评 ," + " GOOD：好评，MODERATE：中评，WORSE：差评'")
    private String grade;

    /** 评价内容 */
    @Column(name = "content", columnDefinition = "text comment '评价内容'")
    private String content;

    /** 评价图片 逗号分割 */
    @Column(name = "images", columnDefinition = "text comment '评价图片 逗号分割'")
    private String images;

    /** 状态 OPEN 正常 ,CLOSE 关闭 */
    @Column(name = "status", columnDefinition = "varchar(32) default 'OPEN' comment '状态  OPEN 正常 ,CLOSE 关闭'")
    private String status;

    /** 评价回复 */
    @Column(name = "reply", columnDefinition = "text comment '评价回复'")
    private String reply;

    /** 评价回复图片 */
    @Column(name = "reply_image", columnDefinition = "text comment '评价回复图片'")
    private String replyImage;

    /** 评论是否有图片 true 有 ,false 没有 */
    @Column(name = "have_image", columnDefinition = "boolean default false comment '评论是否有图片 true 有 ,false 没有'")
    private Boolean haveImage;

    /** 回复是否有图片 true 有 ,false 没有 */
    @Column(name = "have_reply_image", columnDefinition = "boolean default false comment '回复是否有图片 true 有 ,false 没有'")
    private Boolean haveReplyImage;

    /** 回复状态 */
    @Column(name = "reply_status", columnDefinition = "boolean default false comment '回复状态'")
    private Boolean replyStatus;

    /** 物流评分 */
    @Column(name = "delivery_score", columnDefinition = "int default 0 comment '物流评分'")
    private Integer deliveryScore;

    /** 服务评分 */
    @Column(name = "service_score", columnDefinition = "int default 0 comment '服务评分'")
    private Integer serviceScore;

    /** 描述评分 */
    @Column(name = "description_score", columnDefinition = "int default 0 comment '描述评分'")
    private Integer descriptionScore;

    public MemberEvaluation(
            MemberEvaluationDTO memberEvaluationDTO, GoodsSkuSpecGalleryVO goodsSku, Member member, OrderVO order) {
        // 复制评价信息
        BeanUtils.copyProperties(memberEvaluationDTO, this);

        // 设置会员
        this.memberId = member.getId();
        // 会员名称
        this.memberName = member.getNickname();
        // 设置会员头像
        this.memberProfile = member.getFace();
        // 商品名称
        this.goodsName = goodsSku.getGoodsName();
        // 商品图片
        this.goodsImage = goodsSku.getThumbnail();
        // 设置店铺ID
        this.storeId = order.orderBase().storeId();
        // 设置店铺名称
        this.storeName = order.orderBase().storeName();
        // 设置订单编号
        this.orderNo = order.orderBase().sn();
        // 是否包含图片
        this.haveImage = StringUtils.isNotEmpty(memberEvaluationDTO.getImages());
        // 默认开启评价
        this.status = SwitchEnum.OPEN.name();
    }
}
