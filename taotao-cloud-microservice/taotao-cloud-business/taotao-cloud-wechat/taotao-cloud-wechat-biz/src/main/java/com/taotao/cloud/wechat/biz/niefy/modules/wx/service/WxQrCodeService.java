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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.modules.wx.entity.WxQrCode;
import com.github.niefy.modules.wx.form.WxQrCodeForm;
import java.util.Map;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * 公众号带参二维码
 *
 * @author niefy
 * @email niefy@qq.com
 * @since 2020-01-02 11:11:55
 */
public interface WxQrCodeService extends IService<WxQrCode> {
    /**
     * 分页查询用户数据
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 创建公众号带参二维码
     *
     * @param appid
     * @param form
     * @return
     */
    WxMpQrCodeTicket createQrCode(String appid, WxQrCodeForm form) throws WxErrorException;
}
