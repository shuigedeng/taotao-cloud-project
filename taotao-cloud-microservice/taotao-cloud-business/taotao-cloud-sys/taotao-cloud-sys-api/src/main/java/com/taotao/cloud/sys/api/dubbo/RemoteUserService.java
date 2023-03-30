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

package com.taotao.cloud.sys.api.dubbo;

/** 用户服务 */
public interface RemoteUserService {

    /// **
    // * 通过用户名查询用户信息
    // *
    // * @param username 用户名
    // * @return 结果
    // */
    // LoginUser getUserInfo(String username) throws UserException;
    //
    /// **
    // * 通过手机号查询用户信息
    // *
    // * @param phonenumber 手机号
    // * @return 结果
    // */
    // LoginUser getUserInfoByPhonenumber(String phonenumber) throws UserException;
    //
    /// **
    // * 通过openid查询用户信息
    // *
    // * @param openid openid
    // * @return 结果
    // */
    // XcxLoginUser getUserInfoByOpenid(String openid) throws UserException;
    //
    /// **
    // * 注册用户信息
    // *
    // * @param sysUser 用户信息
    // * @return 结果
    // */
    // Boolean registerUserInfo(SysUser sysUser);
    //
    /// **
    // * 通过userId查询用户账户
    // *
    // * @param userId 用户id
    // * @return 结果
    // */
    // String selectUserNameById(Long userId);
}
