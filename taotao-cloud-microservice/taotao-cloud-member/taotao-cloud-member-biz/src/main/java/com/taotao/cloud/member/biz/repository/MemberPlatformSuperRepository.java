package com.taotao.cloud.member.biz.repository;

import com.taotao.cloud.data.jpa.repository.JpaSuperRepository;
import com.taotao.cloud.member.biz.entity.MemberConnect;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/**
 * 第三方登录信息Repository
 *
 * @author shuigedeng
 * @since 2020/9/29 18:02
 * @version 1.0.0
 */
@Repository
public class MemberPlatformSuperRepository extends JpaSuperRepository<MemberConnect, Long> {
	public MemberPlatformSuperRepository(EntityManager em) {
		super(MemberConnect.class, em);
	}
}
