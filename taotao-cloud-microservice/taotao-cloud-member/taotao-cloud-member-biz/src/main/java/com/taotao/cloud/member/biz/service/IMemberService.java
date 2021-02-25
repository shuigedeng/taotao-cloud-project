package com.taotao.cloud.member.biz.service;

import com.taotao.cloud.member.api.dto.member.MemberDTO;
import com.taotao.cloud.member.api.query.member.MemberQuery;
import com.taotao.cloud.member.biz.entity.Member;

/**
 * 会员(c端用户)表服务接口
 *
 * @author dengtao
 * @date 2020-10-16 16:23:49
 * @since 1.0
 */
public interface IMemberService {

	/**
	 * 查询会员是否已(注册)存在
	 *
	 * @param memberQuery memberQuery
	 * @return com.taotao.cloud.uc.biz.entity.MemberUser
	 * @author dengtao
	 * @date 2020/10/16 17:58
	 * @since v1.0
	 */
	Boolean existMember(MemberQuery memberQuery);

	/**
	 * 注册新会员用户
	 *
	 * @param memberDTO memberDTO
	 * @return java.lang.Boolean
	 * @author dengtao
	 * @date 2020/10/19 09:01
	 * @since v1.0
	 */
	Member registerUser(MemberDTO memberDTO);

	/**
	 * 查询会员用户
	 *
	 * @param nicknameOrUserNameOrPhoneOrEmail nicknameOrUserNameOrPhoneOrEmail
	 * @return com.taotao.cloud.uc.biz.entity.MemberUser
	 * @author dengtao
	 * @date 2020/10/19 09:15
	 * @since v1.0
	 */
	Member findMember(String nicknameOrUserNameOrPhoneOrEmail);

	/**
	 * 根据id查询会员信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.member.biz.entity.Member
	 * @author dengtao
	 * @date 2020/11/20 下午4:17
	 * @since v1.0
	 */
	Member findMemberById(Long id);
}
