package com.taotao.cloud.member.biz.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.core.utils.AddrUtil;
import com.taotao.cloud.core.utils.AuthUtil;
import com.taotao.cloud.member.api.dto.member.MemberDTO;
import com.taotao.cloud.member.api.query.member.MemberQuery;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.entity.QMember;
import com.taotao.cloud.member.biz.repository.MemberRepository;
import com.taotao.cloud.member.biz.service.IMemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 会员(c端用户)表服务实现类
 *
 * @author dengtao
 * @date 2020-10-16 16:23:49
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class MemberServiceImpl implements IMemberService {
	private final MemberRepository memberUserRepository;

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
	public Member registerUser(MemberDTO memberDTO) {
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
		BCryptPasswordEncoder passwordEncoder = AuthUtil.getPasswordEncoder();
		Member member = Member.builder()
			.nickname(nickname)
			.phone(phone)
			.password(passwordEncoder.encode(memberDTO.getPassword()))
			.createIp(AddrUtil.getRemoteAddr(request))
			.build();
		return memberUserRepository.saveAndFlush(member);
	}

	@Override
	public Member findMember(String nicknameOrUserNameOrPhoneOrEmail) {
		BooleanExpression predicate = MEMBER.delFlag.eq(false);
		predicate.eq(MEMBER.username.eq(nicknameOrUserNameOrPhoneOrEmail))
			.or(MEMBER.nickname.eq(nicknameOrUserNameOrPhoneOrEmail))
			.or(MEMBER.phone.eq(nicknameOrUserNameOrPhoneOrEmail))
			.or(MEMBER.email.eq(nicknameOrUserNameOrPhoneOrEmail));
		return memberUserRepository.fetchOne(predicate);
	}

	@Override
	public Member findMemberById(Long id) {
		Optional<Member> optionalMember = memberUserRepository.findById(id);
		return optionalMember.orElseThrow(() -> new BusinessException(ResultEnum.MEMBER_NOT_EXIST));

	}

}
