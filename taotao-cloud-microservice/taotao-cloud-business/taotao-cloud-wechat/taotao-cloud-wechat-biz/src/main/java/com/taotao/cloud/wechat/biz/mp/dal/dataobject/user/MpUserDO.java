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

package com.taotao.cloud.wechat.biz.mp.dal.dataobject.user;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.type.LongListTypeHandler;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.tag.MpTagDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

/**
 * 微信公众号粉丝 DO
 *
 * @author 芋道源码
 */
@TableName(value = "mp_user", autoResultMap = true)
@KeySequence("mp_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MpUserDO extends BaseDO {

    /** 编号 */
    @TableId
    private Long id;
    /** 粉丝标识 */
    private String openid;
    /**
     * 关注状态
     *
     * <p>枚举 {@link CommonStatusEnum} 1. 开启 - 已关注 2. 禁用 - 取消关注
     */
    private Integer subscribeStatus;
    /** 关注时间 */
    private LocalDateTime subscribeTime;
    /** 取消关注时间 */
    private LocalDateTime unsubscribeTime;
    /**
     * 昵称
     *
     * <p>注意，2021-12-27 公众号接口不再返回头像和昵称，只能通过微信公众号的网页登录获取
     */
    private String nickname;
    /**
     * 头像地址
     *
     * <p>注意，2021-12-27 公众号接口不再返回头像和昵称，只能通过微信公众号的网页登录获取
     */
    private String headImageUrl;
    /** 语言 */
    private String language;
    /** 国家 */
    private String country;
    /** 省份 */
    private String province;
    /** 城市 */
    private String city;
    /** 备注 */
    private String remark;
    /**
     * 标签编号数组
     *
     * <p>注意，对应的是 {@link MpTagDO#getTagId()} 字段
     */
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> tagIds;

    /**
     * 公众号账号的编号
     *
     * <p>关联 {@link MpAccountDO#getId()}
     */
    private Long accountId;
    /**
     * 公众号 appId
     *
     * <p>冗余 {@link MpAccountDO#getAppId()}
     */
    private String appId;
}
