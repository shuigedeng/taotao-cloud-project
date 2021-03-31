///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.security.component;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
//import org.springframework.util.StringUtils;
//
//import java.util.Collection;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * 根据checktoken 的结果转化用户信息
// *
// * @author dengtao
// * @version 1.0.0
// * @since 2020/5/3 07:47
// */
//public class UserAuthenticationConverterComponent implements UserAuthenticationConverter {
//
//	private static final String N_A = "N/A";
//
//	/**
//	 * Extract information about the user to be used in an access token (i.e. for resource
//	 * servers).
//	 *
//	 * @param authentication an authentication representing a user
//	 * @return a map of key values representing the unique information about the user
//	 */
//	@Override
//	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put(USERNAME, authentication.getName());
//		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
//			response.put(AUTHORITIES,
//				AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
//		} else {
//			response.put(AUTHORITIES, AuthorityUtils.createAuthorityList());
//		}
//		return response;
//	}
//
//	/**
//	 * Inverse of {@link #convertUserAuthentication(Authentication)}. Extracts an Authentication
//	 * from a map.
//	 *
//	 * @param map a map of user information
//	 * @return an Authentication representing the user or null if there is none
//	 */
//	@Override
//	public Authentication extractAuthentication(Map<String, ?> map) {
//		if (map.containsKey(USERNAME)) {
//			Object principal = map.get(USERNAME);
//			Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
////            if (remoteUserService != null) {
////                String username = (String) map.get(USERNAME);
////                Result<SecurityUser> info = remoteUserService.getUserInfoByUsername(username);
////                if (ObjectUtil.isNull(info) || info.getCode() == HttpStatus.NOT_FOUND.value()) {
////                    throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
////                }
////                principal = info.getData();
////            }
//
////            String username = (String) map.get(SecurityConstants.DETAILS_USERNAME);
////            Integer id = (Integer) map.get(SecurityConstants.DETAILS_USER_ID);
////            Integer deptId = (Integer) map.get(SecurityConstants.DETAILS_DEPT_ID);
////            PigUser user = new PigUser(id, deptId, username, N_A, true, true, true, true, authorities);
////            return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
//
//			return new UsernamePasswordAuthenticationToken(principal, N_A, authorities);
//		}
//		return null;
//	}
//
//	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
//		Object authorities = map.get(AUTHORITIES);
//		if (authorities instanceof String) {
//			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
//		}
//		if (authorities instanceof Collection) {
//			return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
//				.collectionToCommaDelimitedString((Collection<?>) authorities));
//		}
//		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
//	}
//}
