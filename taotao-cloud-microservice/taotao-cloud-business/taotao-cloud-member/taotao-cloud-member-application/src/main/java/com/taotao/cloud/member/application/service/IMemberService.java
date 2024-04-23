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

package com.taotao.cloud.member.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.member.infrastructure.persistent.po.Member;
import java.util.List;
import java.util.Map;

/**
 * 会员业务层
 *
 * @since 2020-02-25 14:10:16
 */
public interface IMemberService extends IService<Member> {

	/**
	 * 获取当前登录的用户信息
	 *
	 * @return 会员信息
	 */
	Member getUserInfo();

	/**
	 * 是否可以通过手机获取用户
	 *
	 * @param uuid   UUID
	 * @param mobile 手机号
	 * @return 操作状态
	 */
	boolean findByMobile(String uuid, String mobile);

	/**
	 * 通过用户名获取用户
	 *
	 * @param username 用户名
	 * @return 会员信息
	 */
	Member findByUsername(String username);

	/**
	 * 登录：用户名、密码登录
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return token
	 */
	Token usernameLogin(String username, String password);

	/**
	 * 商家登录：用户名、密码登录
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return token
	 */
	Token usernameStoreLogin(String username, String password);

	/**
	 * 注册：手机号、验证码登录
	 *
	 * @param mobilePhone 手机号
	 * @return token
	 */
	Token mobilePhoneLogin(String mobilePhone);

	/**
	 * 修改会员信息
	 *
	 * @param memberEditDTO 会员修改信息
	 * @return 修改后的会员
	 */
	Boolean editOwn(MemberEditDTO memberEditDTO);

	/**
	 * 修改用户密码
	 *
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return 操作结果
	 */
	Boolean modifyPass(String oldPassword, String newPassword);

	/**
	 * 注册会员
	 *
	 * @param userName    会员
	 * @param password    密码
	 * @param mobilePhone mobilePhone
	 * @return 处理结果
	 */
	Token register(String userName, String password, String mobilePhone);

	/**
	 * 修改当前会员的手机号
	 *
	 * @param mobile 手机号
	 * @return 操作结果
	 */
	Boolean changeMobile(String mobile);

	/**
	 * 通过手机号修改密码
	 *
	 * @param mobile   手机号
	 * @param password 密码
	 */
	Boolean resetByMobile(String mobile, String password);

	/**
	 * 后台-添加会员
	 *
	 * @param memberAddDTO 会员
	 * @return 会员
	 */
	Boolean addMember(MemberAddDTO memberAddDTO);

	/**
	 * 后台-修改会员
	 *
	 * @param managerMemberEditDTO 后台修改会员参数
	 * @return 会员
	 */
	Boolean updateMember(ManagerMemberEditDTO managerMemberEditDTO);

	/**
	 * 获取会员分页
	 *
	 * @param memberSearchPageQuery 会员搜索VO
	 * @return 会员分页
	 */
	IPage<Member> pageQuery(MemberSearchPageQuery memberSearchPageQuery);

	/**
	 * 一键注册会员
	 *
	 * @return
	 */
	Token autoRegister();

	/**
	 * 一键注册会员
	 *
	 * @param authUser 联合登录用户
	 * @return Token
	 */
	Token autoRegister(ConnectAuthUser authUser);

	/**
	 * 刷新token
	 *
	 * @param refreshToken
	 * @return Token
	 */
	Token refreshToken(String refreshToken);

	/**
	 * 刷新token
	 *
	 * @param refreshToken
	 * @return Token
	 */
	Token refreshStoreToken(String refreshToken);

	/**
	 * 会员积分变动
	 *
	 * @param point    变动积分
	 * @param type     是否增加积分 INCREASE 增加 REDUCE 扣减
	 * @param memberId 会员id
	 * @param content  变动日志
	 * @return 操作结果
	 */
	Boolean updateMemberPoint(Long point, String type, Long memberId, String content);

	/**
	 * 修改会员状态
	 *
	 * @param memberIds 会员id集合
	 * @param status    状态
	 * @return 修改结果
	 */
	Boolean updateMemberStatus(List<Long> memberIds, Boolean status);

	/**
	 * 根据条件查询会员总数
	 *
	 * @param memberSearchVO
	 * @return 会员总数
	 */
	Long getMemberNum(MemberSearchVO memberSearchVO);

	/**
	 * 获取指定会员数据
	 *
	 * @param columns   指定获取的列
	 * @param memberIds 会员ids
	 * @return 指定会员数据
	 */
	List<Map<String, Object>> listFieldsByMemberIds(String columns, List<Long> memberIds);

	/**
	 * 登出
	 *
	 * @param userEnum token角色类型
	 */
	void logout(UserEnum userEnum);

	void updateMemberLoginTime(Long id);
}
