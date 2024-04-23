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

package com.taotao.cloud.member.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.CookieUtils;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.member.application.config.aop.point.PointLogPoint;
import com.taotao.cloud.member.application.service.IMemberService;
import com.taotao.cloud.member.infrastructure.persistent.mapper.IMemberMapper;
import com.taotao.cloud.member.infrastructure.persistent.po.Member;
import com.taotao.cloud.mq.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.mq.stream.framework.rocketmq.tags.MemberTagsEnum;
import com.taotao.cloud.mq.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.security.springsecurity.model.SecurityUser;
import com.taotao.cloud.security.springsecurity.utils.SecurityUtils;
import com.taotao.cloud.sensitive.word.SensitiveWordsFilter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员接口业务层实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl extends ServiceImpl<IMemberMapper, Member> implements
	IMemberService {

	/**
	 * 会员token
	 */
	@Autowired
	private MemberTokenGenerate memberTokenGenerate;
	/**
	 * 商家token
	 */
	@Autowired
	private StoreTokenGenerate storeTokenGenerate;
	/**
	 * 联合登录
	 */
	@Autowired
	private ConnectService connectService;
	/**
	 * 店铺
	 */
	@Autowired
	private IFeignStoreApi feignStoreApi;
	/**
	 * RocketMQ 配置
	 */
	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;
	/**
	 * RocketMQ
	 */
	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	/**
	 * 缓存
	 */
	@Autowired
	private RedisRepository redisRepository;

	@Override
	public Member findByUsername(String userName) {
		QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", userName);
		return this.baseMapper.selectOne(queryWrapper);
	}

	@Override
	public Member getUserInfo() {
		SecurityUser currentUser = SecurityUtils.getCurrentUser();
		return this.findByUsername(currentUser.getUsername());
	}

	@Override
	public boolean findByMobile(String uuid, String mobile) {
		QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mobile", mobile);
		Member member = this.baseMapper.selectOne(queryWrapper);
		if (member == null) {
			throw new BusinessException(ResultEnum.USER_NOT_PHONE);
		}
		redisRepository.set(CachePrefix.FIND_MOBILE + uuid, mobile, 300L);

		return true;
	}

	@Override
	public Token usernameLogin(String username, String password) {
		Member member = this.findMember(username);
		// 判断用户是否存在
		if (member == null || !member.getDisabled()) {
			throw new BusinessException(ResultEnum.USER_NOT_EXIST);
		}
		// 判断密码是否输入正确
		if (!new BCryptPasswordEncoder().matches(password, member.getPassword())) {
			throw new BusinessException(ResultEnum.USER_PASSWORD_ERROR);
		}
		loginBindUser(member);
		return memberTokenGenerate.createToken(member, false);
	}

	@Override
	public Token usernameStoreLogin(String username, String password) {
		Member member = this.findMember(username);
		// 判断用户是否存在
		if (member == null || !member.getDisabled()) {
			throw new BusinessException(ResultEnum.USER_NOT_EXIST);
		}
		// 判断密码是否输入正确
		if (!new BCryptPasswordEncoder().matches(password, member.getPassword())) {
			throw new BusinessException(ResultEnum.USER_PASSWORD_ERROR);
		}
		// 对店铺状态的判定处理
		if (Boolean.TRUE.equals(member.getHaveStore())) {
			StoreVO store = feignStoreApi.findSotreById(member.getStoreId());
			if (!store.getStoreDisable().equals(StoreStatusEnum.OPEN.name())) {
				throw new BusinessException(ResultEnum.STORE_CLOSE_ERROR);
			}
		}
		else {
			throw new BusinessException(ResultEnum.USER_NOT_EXIST);
		}

		return storeTokenGenerate.createToken(member, false);
	}

	/**
	 * 传递手机号或者用户名
	 *
	 * @param userName 手机号或者用户名
	 * @return 会员信息
	 */
	private Member findMember(String userName) {
		QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username", userName).or().eq("mobile", userName);
		return this.getOne(queryWrapper);
	}

	@Override
	public Token autoRegister(ConnectAuthUser authUser) {
		if (CharSequenceUtil.isEmpty(authUser.getNickname())) {
			authUser.setNickname("临时昵称");
		}
		if (CharSequenceUtil.isEmpty(authUser.getAvatar())) {
			authUser.setAvatar("https://i.loli.net/2020/11/19/LyN6JF7zZRskdIe.png");
		}
		try {
			String username = UUID.fastUUID().toString();
			Member member = new Member(
				username,
				UUID.fastUUID().toString(),
				authUser.getAvatar(),
				authUser.getNickname(),
				authUser.getGender() != null
					? Convert.toInt(authUser.getGender().getCode())
					: 0);
			// 保存会员
			this.save(member);
			Member loadMember = this.findByUsername(username);
			// 绑定登录方式
			loginBindUser(loadMember, authUser.getUuid(), authUser.getSource());
			return memberTokenGenerate.createToken(loadMember, false);
		}
		catch (Exception e) {
			log.error("自动注册异常：", e);
			throw new BusinessException(ResultEnum.USER_AUTO_REGISTER_ERROR);
		}
	}

	@Override
	public Token autoRegister() {
		ConnectAuthUser connectAuthUser = this.checkConnectUser();
		return this.autoRegister(connectAuthUser);
	}

	@Override
	public Token refreshToken(String refreshToken) {
		return memberTokenGenerate.refreshToken(refreshToken);
	}

	@Override
	public Token refreshStoreToken(String refreshToken) {
		return storeTokenGenerate.refreshToken(refreshToken);
	}

	@Override
	public Token mobilePhoneLogin(String mobilePhone) {
		QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mobile", mobilePhone);
		Member member = this.baseMapper.selectOne(queryWrapper);
		// 如果手机号不存在则自动注册用户
		if (member == null) {
			member = new Member(mobilePhone, UUID.fastUUID().toString(), mobilePhone);
			// 保存会员
			this.save(member);

			String destination =
				rocketmqCustomProperties.getMemberTopic() + ":"
					+ MemberTagsEnum.MEMBER_REGISTER.name();
			rocketMQTemplate.asyncSend(destination, member,
				RocketmqSendCallbackBuilder.commonCallback());
		}
		loginBindUser(member);
		return memberTokenGenerate.createToken(member, false);
	}

	@Override
	public Boolean editOwn(MemberEditDTO memberEditDTO) {
		// 查询会员信息
		Member member = this.findByUsername(SecurityUtils.getUsername());
		// 传递修改会员信息
		BeanUtils.copyProperties(memberEditDTO, member);
		// 修改会员
		this.updateById(member);
		return true;
	}

	@Override
	public Boolean modifyPass(String oldPassword, String newPassword) {
		Member member = this.getById(SecurityUtils.getUserId());
		// 判断旧密码输入是否正确
		if (!new BCryptPasswordEncoder().matches(oldPassword, member.getPassword())) {
			throw new BusinessException(ResultEnum.USER_OLD_PASSWORD_ERROR);
		}
		// 修改会员密码
		LambdaUpdateWrapper<Member> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
		lambdaUpdateWrapper.eq(Member::getId, member.getId());
		lambdaUpdateWrapper.set(Member::getPassword,
			new BCryptPasswordEncoder().encode(newPassword));
		this.update(lambdaUpdateWrapper);
		return true;
	}

	@Override
	public Token register(String userName, String password, String mobilePhone) {
		// 检测会员信息
		checkMember(userName, mobilePhone);
		// 设置会员信息
		Member member = new Member(userName, new BCryptPasswordEncoder().encode(password),
			mobilePhone);
		// 注册成功后用户自动登录
		if (this.save(member)) {
			Token token = memberTokenGenerate.createToken(member, false);
			String destination =
				rocketmqCustomProperties.getMemberTopic() + ":"
					+ MemberTagsEnum.MEMBER_REGISTER.name();
			rocketMQTemplate.asyncSend(destination, member,
				RocketmqSendCallbackBuilder.commonCallback());
			return token;
		}
		return null;
	}

	@Override
	public Boolean changeMobile(String mobile) {
		Member member = this.findByUsername(SecurityUtils.getUsername());
		// 判断是否用户登录并且会员ID为当前登录会员ID
		if (!Objects.equals(SecurityUtils.getUserId(), member.getId())) {
			throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
		}
		// 修改会员手机号
		LambdaUpdateWrapper<Member> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
		lambdaUpdateWrapper.eq(Member::getId, member.getId());
		lambdaUpdateWrapper.set(Member::getMobile, mobile);
		return this.update(lambdaUpdateWrapper);
	}

	@Override
	public Boolean resetByMobile(String uuid, String password) {
		String phone = redisRepository.get(CachePrefix.FIND_MOBILE + uuid).toString();
		// 根据手机号获取会员判定是否存在此会员
		if (phone != null) {
			// 修改密码
			LambdaUpdateWrapper<Member> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
			lambdaUpdateWrapper.eq(Member::getMobile, phone);
			lambdaUpdateWrapper.set(Member::getPassword,
				new BCryptPasswordEncoder().encode(password));
			return this.update(lambdaUpdateWrapper);
		}
		else {
			throw new BusinessException(ResultEnum.USER_PHONE_NOT_EXIST);
		}
	}

	@Override
	public Boolean addMember(MemberAddDTO memberAddDTO) {
		// 检测会员信息
		checkMember(memberAddDTO.getUsername(), memberAddDTO.getMobile());

		// 添加会员
		Member member = new Member(
			memberAddDTO.getUsername(),
			new BCryptPasswordEncoder().encode(memberAddDTO.getPassword()),
			memberAddDTO.getMobile());
		this.save(member);

		String destination =
			rocketmqCustomProperties.getMemberTopic() + ":" + MemberTagsEnum.MEMBER_REGISTER.name();
		rocketMQTemplate.asyncSend(destination, member,
			RocketmqSendCallbackBuilder.commonCallback());
		return true;
	}

	@Override
	public Boolean updateMember(ManagerMemberEditDTO managerMemberEditDTO) {
		// 判断是否用户登录并且会员ID为当前登录会员ID
		SecurityUser tokenUser = SecurityUtils.getCurrentUser();
		if (tokenUser == null) {
			throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
		}
		// 过滤会员昵称敏感词
		if (StringUtils.isNotBlank(managerMemberEditDTO.getNickName())) {
			managerMemberEditDTO.setNickName(
				SensitiveWordsFilter.filter(managerMemberEditDTO.getNickName()));
		}
		// 如果密码不为空则加密密码
		if (StringUtils.isNotBlank(managerMemberEditDTO.getPassword())) {
			managerMemberEditDTO.setPassword(
				new BCryptPasswordEncoder().encode(managerMemberEditDTO.getPassword()));
		}
		// 查询会员信息
		Member member = this.findByUsername(managerMemberEditDTO.getUsername());
		// 传递修改会员信息
		BeanUtils.copyProperties(managerMemberEditDTO, member);
		this.updateById(member);
		return true;
	}

	@Override
	public IPage<Member> pageQuery(MemberSearchPageQuery memberSearchPageQuery) {
		QueryWrapper<Member> queryWrapper = Wrappers.query();
		// 用户名查询
		queryWrapper.like(
			CharSequenceUtil.isNotBlank(memberSearchPageQuery.getUsername()),
			"username",
			memberSearchPageQuery.getUsername());
		// 用户名查询
		queryWrapper.like(
			CharSequenceUtil.isNotBlank(memberSearchPageQuery.getNickName()),
			"nick_name",
			memberSearchPageQuery.getNickName());
		// 按照电话号码查询
		queryWrapper.like(
			CharSequenceUtil.isNotBlank(memberSearchPageQuery.getMobile()),
			"mobile",
			memberSearchPageQuery.getMobile());
		// 按照会员状态查询
		// queryWrapper.eq(CharSequenceUtil.isNotBlank(memberSearchPageDTO.getDisabled()),
		// "disabled",
		//	memberSearchPageDTO.getDisabled().equals(SwitchEnum.OPEN.name()) ? 1 : 0);
		queryWrapper.orderByDesc("create_time");

		return this.page(memberSearchPageQuery.buildMpPage(), queryWrapper);
	}

	@Override
	@PointLogPoint
	public Boolean updateMemberPoint(Long point, String type, Long memberId, String content) {
		// 获取当前会员信息
		Member member = this.getById(memberId);
		if (member != null) {
			// 积分变动后的会员积分
			long currentPoint;
			// 会员总获得积分
			long totalPoint = member.getTotalPoint();
			// 如果增加积分
			if (type.equals(PointTypeEnum.INCREASE.name())) {
				currentPoint = member.getPoint() + point;
				// 如果是增加积分 需要增加总获得积分
				totalPoint = totalPoint + point;
			}
			// 否则扣除积分
			else {
				currentPoint = member.getPoint() - point < 0 ? 0 : member.getPoint() - point;
			}
			member.setPoint(currentPoint);
			member.setTotalPoint(totalPoint);
			boolean result = this.updateById(member);
			if (result) {
				// 发送会员消息
				MemberPointMessageDTO memberPointMessageDTO = new MemberPointMessageDTO();
				memberPointMessageDTO.setPoint(point);
				memberPointMessageDTO.setType(type);
				memberPointMessageDTO.setMemberId(memberId);
				String destination =
					rocketmqCustomProperties.getMemberTopic() + ":"
						+ MemberTagsEnum.MEMBER_POINT_CHANGE.name();
				rocketMQTemplate.asyncSend(
					destination, memberPointMessageDTO,
					RocketmqSendCallbackBuilder.commonCallback());
				return true;
			}
			return false;
		}
		throw new BusinessException(ResultEnum.USER_NOT_EXIST);
	}

	@Override
	public Boolean updateMemberStatus(List<Long> memberIds, Boolean status) {
		UpdateWrapper<Member> updateWrapper = Wrappers.update();
		updateWrapper.set("disabled", status);
		updateWrapper.in("id", memberIds);

		return this.update(updateWrapper);
	}

	/**
	 * 根据手机号获取会员
	 *
	 * @param mobilePhone 手机号
	 * @return 会员
	 */
	private Member findByPhone(String mobilePhone) {
		QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mobile", mobilePhone);
		return this.baseMapper.selectOne(queryWrapper);
	}

	/**
	 * 获取cookie中的联合登录对象
	 *
	 * @param uuid uuid
	 * @param type 状态
	 * @return cookie中的联合登录对象
	 */
	private ConnectAuthUser getConnectAuthUser(String uuid, String type) {
		Object context = redisRepository.get(ConnectService.cacheKey(type, uuid));
		if (context != null) {
			return (ConnectAuthUser) context;
		}
		return null;
	}

	/**
	 * 成功登录，则检测cookie中的信息，进行会员绑定
	 *
	 * @param member  会员
	 * @param unionId unionId
	 * @param type    状态
	 */
	private void loginBindUser(Member member, String unionId, String type) {
		Connect connect = connectService.queryConnect(
			ConnectQuery.builder().unionId(unionId).unionType(type).build());

		if (connect == null) {
			connect = new Connect(member.getId(), unionId, type);
			connectService.save(connect);
		}
	}

	/**
	 * 成功登录，则检测cookie中的信息，进行会员绑定
	 *
	 * @param member 会员
	 */
	private void loginBindUser(Member member) {
		// 获取cookie存储的信息
		String uuid = CookieUtils.getCookie(ConnectService.CONNECT_COOKIE,
			RequestUtils.getRequest());
		String connectType = CookieUtils.getCookie(ConnectService.CONNECT_TYPE,
			RequestUtils.getRequest());

		// 如果联合登陆存储了信息
		if (CharSequenceUtil.isNotEmpty(uuid) && CharSequenceUtil.isNotEmpty(connectType)) {
			try {
				// 获取信息
				ConnectAuthUser connectAuthUser = getConnectAuthUser(uuid, connectType);
				if (connectAuthUser == null) {
					return;
				}
				Connect connect = connectService.queryConnect(ConnectQuery.builder()
					.unionId(connectAuthUser.getUuid())
					.unionType(connectType)
					.build());
				if (connect == null) {
					connect = new Connect(member.getId(), connectAuthUser.getUuid(), connectType);
					connectService.save(connect);
				}
			}
			catch (Exception e) {
				LogUtils.error("绑定第三方联合登陆失败：", e);
			}
			finally {
				// 联合登陆成功与否，都清除掉cookie中的信息
				CookieUtils.delCookie(ConnectService.CONNECT_COOKIE, RequestUtils.getResponse());
				CookieUtils.delCookie(ConnectService.CONNECT_TYPE, RequestUtils.getResponse());
			}
		}
	}

	/**
	 * 检测是否可以绑定第三方联合登陆 返回null原因 包含原因1：redis中已经没有联合登陆信息 2：已绑定其他账号
	 *
	 * @return 返回对象则代表可以进行绑定第三方会员，返回null则表示联合登陆无法继续
	 */
	private ConnectAuthUser checkConnectUser() {
		// 获取cookie存储的信息
		String uuid = CookieUtils.getCookie(ConnectService.CONNECT_COOKIE,
			RequestUtils.getRequest());
		String connectType = CookieUtils.getCookie(ConnectService.CONNECT_TYPE,
			RequestUtils.getRequest());

		// 如果联合登陆存储了信息
		if (CharSequenceUtil.isNotEmpty(uuid) && CharSequenceUtil.isNotEmpty(connectType)) {
			// 枚举 联合登陆类型获取
			ConnectAuthEnum authInterface = ConnectAuthEnum.valueOf(connectType);

			ConnectAuthUser connectAuthUser = getConnectAuthUser(uuid, connectType);
			if (connectAuthUser == null) {
				throw new BusinessException(ResultEnum.USER_OVERDUE_CONNECT_ERROR);
			}
			// 检测是否已经绑定过用户
			Connect connect = connectService.queryConnect(ConnectQuery.builder()
				.unionType(connectType)
				.unionId(connectAuthUser.getUuid())
				.build());
			// 没有关联则返回true，表示可以继续绑定
			if (connect == null) {
				connectAuthUser.setConnectEnum(authInterface);
				return connectAuthUser;
			}
			else {
				throw new BusinessException(ResultEnum.USER_CONNECT_BANDING_ERROR);
			}
		}
		else {
			throw new BusinessException(ResultEnum.USER_CONNECT_NOT_EXIST_ERROR);
		}
	}

	@Override
	public Long getMemberNum(MemberSearchVO memberSearchVO) {
		QueryWrapper<Member> queryWrapper = Wrappers.query();
		// 用户名查询
		queryWrapper.like(
			CharSequenceUtil.isNotBlank(memberSearchVO.getUsername()), "username",
			memberSearchVO.getUsername());
		// 按照电话号码查询
		queryWrapper.like(
			CharSequenceUtil.isNotBlank(memberSearchVO.getMobile()), "mobile",
			memberSearchVO.getMobile());
		// 按照状态查询
		// queryWrapper.eq(CharSequenceUtil.isNotBlank(memberSearchVO.getDisabled()), "disabled",
		//	memberSearchVO.getDisabled().equals(SwitchEnum.OPEN.name()) ? 1 : 0);
		queryWrapper.orderByDesc("create_time");
		return this.count(queryWrapper);
	}

	/**
	 * 获取指定会员数据
	 *
	 * @param columns   指定获取的列
	 * @param memberIds 会员ids
	 * @return 指定会员数据
	 */
	@Override
	public List<Map<String, Object>> listFieldsByMemberIds(String columns, List<Long> memberIds) {
		return this.listMaps(new QueryWrapper<Member>()
			.select(columns)
			.in(memberIds != null && !memberIds.isEmpty(), "id", memberIds));
	}

	/**
	 * 登出
	 */
	@Override
	public void logout(UserEnum userEnum) {
		// 获取当前用户的token
		String currentUserToken = RequestUtils.getRequest().getHeader("token");
		if (CharSequenceUtil.isNotEmpty(currentUserToken)) {
			redisRepository.del(CachePrefix.ACCESS_TOKEN.getPrefix(userEnum) + currentUserToken);
		}
	}

	/**
	 * 检测会员
	 *
	 * @param userName    会员名称
	 * @param mobilePhone 手机号
	 */
	private void checkMember(String userName, String mobilePhone) {
		// 判断用户名是否存在
		if (findByUsername(userName) != null) {
			throw new BusinessException(ResultEnum.USER_NAME_EXIST);
		}
		// 判断手机号是否存在
		if (findByPhone(mobilePhone) != null) {
			throw new BusinessException(ResultEnum.USER_PHONE_EXIST);
		}
	}

	@Override
	public void updateMemberLoginTime(Long id) {
	}
}
