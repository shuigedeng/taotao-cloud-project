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

package com.taotao.cloud.wechat.biz.mp.dal.dataobject.tag;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;

/**
 * 公众号标签 DO
 *
 * @author 芋道源码
 */
@TableName("mp_tag")
@KeySequence("mp_tag_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MpTagDO extends BaseDO {

    /** 主键 */
    @TableId(type = IdType.INPUT)
    private Long id;
    /** 公众号标签 id */
    private Long tagId;
    /** 标签名 */
    private String name;
    /**
     * 此标签下粉丝数
     *
     * <p>冗余：{@link WxUserTag#getCount()} 字段，需要管理员点击【同步】后，更新该字段
     */
    private Integer count;

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
