package com.taotao.cloud.member.biz.service;

import com.taotao.cloud.member.api.dto.MemberDTO;
import com.taotao.cloud.member.api.query.MemberQuery;
import com.taotao.cloud.member.biz.entity.MemberBack;

/**
 * 会员(c端用户)表服务接口
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:49
 * @since 1.0
 */
public interface IMemberService {

	/**
	 * 查询会员是否已(注册)存在
	 *
	 * @param memberQuery memberQuery
	 * @return com.taotao.cloud.uc.biz.entity.MemberUser
	 * @author shuigedeng
	 * @since 2020/10/16 17:58
	 * @version 1.0.0
	 */
	Boolean existMember(MemberQuery memberQuery);

	/**
	 * 注册新会员用户
	 *
	 * @param memberDTO memberDTO
	 * @return java.lang.Boolean
	 * @author shuigedeng
	 * @since 2020/10/19 09:01
	 * @version 1.0.0
	 */
	MemberBack registerUser(MemberDTO memberDTO);

	/**
	 * 查询会员用户
	 *
	 * @param nicknameOrUserNameOrPhoneOrEmail nicknameOrUserNameOrPhoneOrEmail
	 * @return com.taotao.cloud.uc.biz.entity.MemberUser
	 * @author shuigedeng
	 * @since 2020/10/19 09:15
	 * @version 1.0.0
	 */
	MemberBack findMember(String nicknameOrUserNameOrPhoneOrEmail);

	/**
	 * 根据id查询会员信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.member.biz.entity.Member
	 * @author shuigedeng
	 * @since 2020/11/20 下午4:17
	 * @version 1.0.0
	 */
	MemberBack findMemberById(Long id);
}
