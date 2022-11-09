package com.taotao.cloud.auth.biz.authentication.face.service;

import com.taotao.cloud.auth.biz.face.FaceUtils;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DefaultFaceUserDetailsService implements FaceUserDetailsService {
	@Autowired
	private FaceUtils faceUtils;
	@Autowired
	private IFeignMemberApi feignMemberApi;

	@Override
	public UserDetails loadUserByImgBase64(String imgBase64) throws UsernameNotFoundException {

		Double aDouble = faceUtils.verifyUser(imgBase64);

		return SecurityUser.builder()
			.account("admin")
			.userId(1L)
			.username("admin")
			.nickname("admin")
			.password(
				"$2a$10$ofQ95D2nNs1JC.JiPaGo3O11.P7sP3TkcRyXBpyfskwBDJRAh0caG")
			.phone("15730445331")
			.mobile("15730445331")
			.email("981376578@qq.com")
			.sex(1)
			.status(1)
			.type(2)
			.permissions(Set.of("xxx", "sldfl"))
			.build();
	}
}
