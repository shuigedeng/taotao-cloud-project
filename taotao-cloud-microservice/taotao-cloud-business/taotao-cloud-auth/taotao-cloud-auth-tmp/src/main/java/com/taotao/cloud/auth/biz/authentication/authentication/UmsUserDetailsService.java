/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.taotao.cloud.auth.biz.authentication.authentication;

//import me.zhyd.oauth.model.AuthUser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;

/**
 * 用户名密码、手机短信登录、第三方授权登录及自动注册、用户注册服务：<br><br>
 * 1. 用于用户名密码登录与手机短信登录逻辑。<br><br>
 * 2. 用于用户名密码注册与手机短信注册逻辑。<br><br>
 * 3. 用于第三方登录注册逻辑。<br><br>
 *
 * @author YongWu zheng
 * @version V1.0  Created by 2020/5/16 11:48
 */
public interface UmsUserDetailsService extends UserDetailsService, UserDetailsRegisterService {

	/**
	 * 用于第三方登录时查询服务, userId 为本地账户的 userId
	 *
	 * @param userId userId 为本地账户的 userId (必须具有唯一性, username 支持唯一索引, 也可以是 username)
	 * @return the UserDetails requested, 注意 {@link UserDetails#getUsername()} 中的 username 即视为 userId
	 * @throws UsernameNotFoundException 没有此 userId 的用户
	 * @see UserDetailsService#loadUserByUsername(String)
	 */
	UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException;

	/**
	 * 在本地账户中检查是否存在 usernames, usernames 为本地账户的 usernames.<br>
	 * 注意: 如果在你本地账户用户名允许重名, 直接都返回 true(List = [true, true, true])
	 *
	 * @param usernames usernames 为本地账户的 username, 一般通过调用 {@link #generateUsernames(AuthUser)} 获取
	 * @return usernames    是否存在的列表(true 表示存在), 与传入的 usernames 顺序一一对应
	 * @throws IOException 数据库查询异常
	 */
	List<Boolean> existedByUsernames(String... usernames) throws IOException;

	/**
	 * {@link #existedByUsernames(String...)} usernames 生成规则.
	 * 如需自定义重新实现此逻辑
	 * @param authUser     第三方用户信息
	 * @return 返回一个 username 数组
	 */
//    default String[] generateUsernames(AuthUser authUser) {
//        return new String[]{
//                authUser.getUsername(),
//                // providerId = authUser.getSource()
//                authUser.getUsername() + "_" + authUser.getSource(),
//                // providerUserId = authUser.getUuid()
//                authUser.getUsername() + "_" + authUser.getSource() + "_" + authUser.getUuid()
//        };
//    }


}
