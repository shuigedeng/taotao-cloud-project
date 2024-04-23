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

package com.taotao.cloud.goods.application.command.store.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 小程序直播间基础哦
 *
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudioCO {

    /** 直播间名字，最短3个汉字，最长17个汉字，1个汉字相当于2个字符 */
    private String name;

    /**
     * 背景图，填入mediaID（mediaID获取后，三天内有效）；图片mediaID的获取，请参考以下文档： <a
     * href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html">https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html</a>；直播间背景图，图片规则：建议像素1080*1920，大小不超过2M
     */
    private String coverImg;

    /** 直播计划开始时间（开播时间需要在当前时间的10分钟后 并且 开始时间不能在 6 个月后） */
    private String startTime;

    /** 直播计划结束时间（开播时间和结束时间间隔不得短于30分钟，不得超过24小时） */
    private String endTime;

    /** 主播昵称，最短2个汉字，最长15个汉字，1个汉字相当于2个字符 */
    private String anchorName;

    /**
     * 主播微信号，如果未实名认证，需要先前往“小程序直播”小程序进行实名验证, 小程序二维码链接：<a
     * href="https://res.wx.qq.com/op_res/9rSix1dhHfK4rR049JL0PHJ7TpOvkuZ3mE0z7Ou_Etvjf-w1J_jVX0rZqeStLfwh">https://res.wx.qq.com/op_res/9rSix1dhHfK4rR049JL0PHJ7TpOvkuZ3mE0z7Ou_Etvjf-w1J_jVX0rZqeStLfwh</a>
     */
    private String anchorWechat;

    /**
     * 分享图，填入mediaID（mediaID获取后，三天内有效）；图片mediaID的获取，请参考以下文档： <a
     * href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html">https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html</a>；直播间分享图，图片规则：建议像素800*2550，大小不超过1M；
     */
    private String shareImg;

    /**
     * 购物直播频道封面图，填入mediaID（mediaID获取后，三天内有效）；图片mediaID的获取，请参考以下文档： <a
     * href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html">https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/New_temporary_materials.html</a>;
     * 购物直播频道封面图，图片规则：建议像素800*800，大小不超过100KB；
     */
    private String feedsImg;

    /** 回放视频链接 */
    private String mediaUrl;

    /** 房间ID */
    private Integer roomId;

    /** 小程序直播码 */
    private String qrCodeUrl;

    /** 店铺ID */
    private Long storeId;

    /** 直播间商品数量 */
    private Integer roomGoodsNum;

    /** 直播间商品(最多展示两个商品：name/goodsImage) */
    private String roomGoodsList;

    /** 推荐直播间 */
    private Boolean recommend;

    /** 直播间状态 */
    private String status;
}
