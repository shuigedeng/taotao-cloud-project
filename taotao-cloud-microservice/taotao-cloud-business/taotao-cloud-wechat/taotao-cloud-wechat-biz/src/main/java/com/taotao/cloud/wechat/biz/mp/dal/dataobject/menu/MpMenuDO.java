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

package com.taotao.cloud.wechat.biz.mp.dal.dataobject.menu;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpMessageDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.api.WxConsts.MenuButtonType;

/**
 * 公众号菜单 DO
 *
 * @author 芋道源码
 */
@TableName(value = "mp_menu", autoResultMap = true)
@KeySequence("mp_menu_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MpMenuDO extends BaseDO {

    /** 编号 - 顶级菜单 */
    public static final Long ID_ROOT = 0L;

    /** 编号 */
    @TableId
    private Long id;
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

    /** 菜单名称 */
    private String name;
    /**
     * 菜单标识
     *
     * <p>支持多 DB 类型时，无法直接使用 key + @TableField("menuKey") 来实现转换，原因是 "menuKey" AS key 而存在报错
     */
    private String menuKey;
    /** 父菜单编号 */
    private Long parentId;

    // ========== 按钮操作 ==========

    /**
     * 按钮类型
     *
     * <p>枚举 {@link MenuButtonType}
     */
    private String type;

    /**
     * 网页链接
     *
     * <p>粉丝点击菜单可打开链接，不超过 1024 字节
     *
     * <p>类型为 {@link WxConsts.XmlMsgType} 的 VIEW、MINIPROGRAM
     */
    private String url;

    /**
     * 小程序的 appId
     *
     * <p>类型为 {@link MenuButtonType} 的 MINIPROGRAM
     */
    private String miniProgramAppId;
    /**
     * 小程序的页面路径
     *
     * <p>类型为 {@link MenuButtonType} 的 MINIPROGRAM
     */
    private String miniProgramPagePath;

    /** 跳转图文的媒体编号 */
    private String articleId;

    // ========== 消息内容 ==========

    /**
     * 消息类型
     *
     * <p>当 {@link #type} 为 CLICK、SCANCODE_WAITMSG
     *
     * <p>枚举 {@link WxConsts.XmlMsgType} 中的 TEXT、IMAGE、VOICE、VIDEO、NEWS、MUSIC
     */
    private String replyMessageType;

    /**
     * 回复的消息内容
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 TEXT
     */
    private String replyContent;

    /**
     * 回复的媒体 id
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 IMAGE、VOICE、VIDEO
     */
    private String replyMediaId;
    /**
     * 回复的媒体 URL
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 IMAGE、VOICE、VIDEO
     */
    private String replyMediaUrl;

    /**
     * 回复的标题
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 VIDEO
     */
    private String replyTitle;
    /**
     * 回复的描述
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 VIDEO
     */
    private String replyDescription;

    /**
     * 回复的缩略图的媒体 id，通过素材管理中的接口上传多媒体文件，得到的 id
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 MUSIC、VIDEO
     */
    private String replyThumbMediaId;
    /**
     * 回复的缩略图的媒体 URL
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 MUSIC、VIDEO
     */
    private String replyThumbMediaUrl;

    /**
     * 回复的图文消息数组
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 NEWS
     */
    @TableField(typeHandler = MpMessageDO.ArticleTypeHandler.class)
    private List<MpMessageDO.Article> replyArticles;

    /**
     * 回复的音乐链接
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 MUSIC
     */
    private String replyMusicUrl;
    /**
     * 回复的高质量音乐链接
     *
     * <p>WIFI 环境优先使用该链接播放音乐
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 MUSIC
     */
    private String replyHqMusicUrl;
}
