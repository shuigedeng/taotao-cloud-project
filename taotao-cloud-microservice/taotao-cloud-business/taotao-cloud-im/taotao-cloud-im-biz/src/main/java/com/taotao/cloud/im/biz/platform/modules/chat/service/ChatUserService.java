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

package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.auth.vo.AuthVo01;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.vo.MyVo09;
import org.apache.shiro.authc.AuthenticationToken;

/** 用户表 服务层 q3z3 */
public interface ChatUserService extends BaseService<ChatUser> {

    /** 通过手机+密码注册 */
    void register(AuthVo01 authVo);

    /** 通过手机号查询 */
    ChatUser queryByPhone(String phone);

    /**
     * 重置密码
     *
     * @return
     */
    void resetPass(Long userId, String password);

    /** 修改密码 */
    void editPass(String password, String pwd);

    /** 修改微聊号 */
    void editChatNo(String chatNo);

    /** 获取基本信息 */
    MyVo09 getInfo();

    /** 获取二维码 */
    String getQrCode();

    /** 获取二维码 */
    String resetQrCode();

    /** 用户注销 */
    void deleted();

    /** 执行登录/返回token */
    Dict doLogin(AuthenticationToken authenticationToken, String cid);

    /** 退出登录 */
    void logout();

    /** 绑定cid */
    void bindCid(String cid);
}
