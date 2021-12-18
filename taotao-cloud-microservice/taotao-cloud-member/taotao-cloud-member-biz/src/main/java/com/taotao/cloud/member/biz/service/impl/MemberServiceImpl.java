package com.taotao.cloud.member.biz.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.member.api.dto.member.MemberDTO;
import com.taotao.cloud.member.api.query.member.MemberQuery;
import com.taotao.cloud.member.biz.entity.MemberBack;
import com.taotao.cloud.member.biz.entity.QMember;
import com.taotao.cloud.member.biz.repository.MemberSuperRepository;
import com.taotao.cloud.member.biz.service.IMemberService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 会员(c端用户)表服务实现类
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:49
 * @since 1.0
 */
@Service
public class MemberServiceImpl implements IMemberService {

	private final MemberSuperRepository memberUserRepository;

	public MemberServiceImpl(
		MemberSuperRepository memberUserRepository) {
		this.memberUserRepository = memberUserRepository;
	}

	private static final QMember MEMBER = QMember.member;

	@Override
	public Boolean existMember(MemberQuery memberQuery) {
		BooleanExpression predicate = MEMBER.delFlag.eq(false);

		Optional.ofNullable(memberQuery.getNickname())
			.ifPresent(nickname -> predicate.and(MEMBER.nickname.eq(nickname)));
		Optional.ofNullable(memberQuery.getPhone())
			.ifPresent(phone -> predicate.and(MEMBER.phone.eq(phone)));
		Optional.ofNullable(memberQuery.getUsername())
			.ifPresent(username -> predicate.and(MEMBER.username.eq(username)));
		Optional.ofNullable(memberQuery.getEmail())
			.ifPresent(email -> predicate.and(MEMBER.email.eq(email)));

		return memberUserRepository.exists(predicate);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public MemberBack registerUser(MemberDTO memberDTO) {
		String nickname = memberDTO.getNickname();
		MemberQuery nicknameQuery = MemberQuery.builder().nickname(nickname).build();
		if (existMember(nicknameQuery)) {
			throw new BusinessException(ResultEnum.MEMBER_NICKNAME_EXIST);
		}
		String phone = memberDTO.getPhone();
		MemberQuery phoneQuery = MemberQuery.builder().phone(phone).build();
		if (existMember(phoneQuery)) {
			throw new BusinessException(ResultEnum.MEMBER_PHONE_EXIST);
		}
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		BCryptPasswordEncoder passwordEncoder = SecurityUtil.getPasswordEncoder();
		MemberBack member = MemberBack.builder()
			.nickname(nickname)
			.phone(phone)
			.password(passwordEncoder.encode(memberDTO.getPassword()))
			.createIp(RequestUtil.getRemoteAddr(request))
			.build();
		return memberUserRepository.saveAndFlush(member);
	}

	@Override
	public MemberBack findMember(String nicknameOrUserNameOrPhoneOrEmail) {
		BooleanExpression predicate = MEMBER.delFlag.eq(false);
		predicate.eq(MEMBER.username.eq(nicknameOrUserNameOrPhoneOrEmail))
			.or(MEMBER.nickname.eq(nicknameOrUserNameOrPhoneOrEmail))
			.or(MEMBER.phone.eq(nicknameOrUserNameOrPhoneOrEmail))
			.or(MEMBER.email.eq(nicknameOrUserNameOrPhoneOrEmail));
		return memberUserRepository.fetchOne(predicate);
	}

	@Override
	public MemberBack findMemberById(Long id) {
		Optional<MemberBack> optionalMember = memberUserRepository.findById(id);
		return optionalMember.orElseThrow(() -> new BusinessException(ResultEnum.MEMBER_NOT_EXIST));

	}

}
