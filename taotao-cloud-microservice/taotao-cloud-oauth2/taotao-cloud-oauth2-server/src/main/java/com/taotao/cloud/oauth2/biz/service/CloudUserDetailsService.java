package com.taotao.cloud.oauth2.biz.service;

import com.taotao.cloud.oauth2.biz.models.CloudAuthAccount;
import com.taotao.cloud.oauth2.biz.models.CloudUserDetails;
import com.taotao.cloud.oauth2.biz.repository.CloudAuthAccountRepository;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CloudUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudUserDetailsService.class);

	@Autowired
	private CloudAuthAccountRepository cloudAuthAccountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CloudAuthAccount account = cloudAuthAccountRepository.findFirstByUsername(username);

		if (Objects.isNull(account)) {
			LOGGER.debug("Account [{}] not found.", username);
			throw new UsernameNotFoundException(
				String.format("Account: [%s] not found.", username));
		}


		return CloudUserDetails.withId(account.getId())
			.username(account.getUsername())
			.password(account.getPassword())
			.roles(account.getRoles().toArray(String[]::new))
			.accountNonLocked(!account.accountLocked())
			.enabled(account.getEnabled())
			.accountNonExpired(true)
			.credentialsNonExpired(true)
			.build();


	}
}
