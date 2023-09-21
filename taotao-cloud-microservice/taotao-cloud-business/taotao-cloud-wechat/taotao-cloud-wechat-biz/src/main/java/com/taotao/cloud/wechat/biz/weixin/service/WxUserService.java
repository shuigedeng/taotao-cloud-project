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

package com.taotao.cloud.wechat.biz.weixin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.weixin.entity.WxOpenDataDTO;
import com.joolun.weixin.entity.WxUser;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * 微信用户
 *
 * @author www.joolun.com
 * @since 2019-03-25 15:39:39
 */
public interface WxUserService extends IService<WxUser> {

    /** 同步微信用户 */
    void synchroWxUser() throws WxErrorException;

    /**
     * 修改用户备注
     *
     * @param entity
     * @return
     */
    boolean updateRemark(WxUser entity) throws WxErrorException;

    /**
     * 认识标签
     *
     * @param taggingType
     * @param tagId
     * @param openIds
     * @throws WxErrorException
     */
    void tagging(String taggingType, Long tagId, String[] openIds) throws WxErrorException;

    /**
     * 根据openId获取用户
     *
     * @param openId
     * @return
     */
    WxUser getByOpenId(String openId);

    /**
     * 小程序登录
     *
     * @param appId
     * @param jsCode
     * @return
     */
    WxUser loginMa(String appId, String jsCode) throws WxErrorException;

    /**
     * 新增、更新微信用户
     *
     * @param wxOpenDataDTO
     * @return
     */
    WxUser saveOrUptateWxUser(WxOpenDataDTO wxOpenDataDTO);
}
