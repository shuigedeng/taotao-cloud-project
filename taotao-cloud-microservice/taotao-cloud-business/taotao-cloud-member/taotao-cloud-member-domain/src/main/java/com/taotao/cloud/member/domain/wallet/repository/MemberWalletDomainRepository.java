package com.taotao.cloud.member.domain.wallet.repository;

import com.taotao.cloud.member.domain.wallet.entity.MemberWalletEntity;

public interface MemberWalletDomainRepository {
	/**
	 * 新增部门.
	 *
	 * @param dept 部门对象
	 */
	void create(MemberWalletEntity dept);

	/**
	 * 修改部门.
	 *
	 * @param dept 部门对象
	 */
	void modify(MemberWalletEntity dept);

	/**
	 * 根据ID删除部门.
	 *
	 * @param ids IDS
	 */
	void remove(Long[] ids);
}
