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

package com.taotao.cloud.auth.biz.models; // package com.taotao.cloud.oauth2.biz.models;
//
// import java.time.Instant;
// import java.util.Collections;
// import java.util.List;
// import java.util.Objects;
// import java.util.stream.Collectors;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import org.springframework.security.oauth2.core.AuthorizationGrantType;
// import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//
// @Entity
// public class CloudRegisteredClient extends AbstractAuditable {
//
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.AUTO)
// 	private Long id;
// 	private String clientId;
// 	private Instant clientIdIssuedAt;
// 	private String clientSecret;
// 	private Instant clientSecretExpiresAt;
// 	private String clientName;
//
// 	//private List<String> clientAuthenticationMethods;
// 	//private List<AuthorizationGrantType> authorizationGrantTypes;
// 	//private List<String> redirectUris;
// 	//private List<String> scopes;
//
//
// 	public CloudRegisteredClient() {
// 	}
//
// 	public Long getId() {
// 		return id;
// 	}
//
// 	public void setId(Long id) {
// 		this.id = id;
// 	}
//
// 	public String getClientId() {
// 		return clientId;
// 	}
//
// 	public void setClientId(String clientId) {
// 		this.clientId = clientId;
// 	}
//
// 	public Instant getClientIdIssuedAt() {
// 		return clientIdIssuedAt;
// 	}
//
// 	public void setClientIdIssuedAt(Instant clientIdIssuedAt) {
// 		this.clientIdIssuedAt = clientIdIssuedAt;
// 	}
//
// 	public String getClientSecret() {
// 		return clientSecret;
// 	}
//
// 	public void setClientSecret(String clientSecret) {
// 		this.clientSecret = clientSecret;
// 	}
//
// 	public Instant getClientSecretExpiresAt() {
// 		return clientSecretExpiresAt;
// 	}
//
// 	public void setClientSecretExpiresAt(Instant clientSecretExpiresAt) {
// 		this.clientSecretExpiresAt = clientSecretExpiresAt;
// 	}
//
// 	public String getClientName() {
// 		return clientName;
// 	}
//
// 	public void setClientName(String clientName) {
// 		this.clientName = clientName;
// 	}
//
// 	//public List<ClientAuthenticationMethod> getClientAuthenticationMethods() {
// 	//	if (Objects.nonNull(clientAuthenticationMethods)
// 	//		&& clientAuthenticationMethods.size() > 0) {
// 	//		return clientAuthenticationMethods.stream().map(ClientAuthenticationMethod::new)
// 	//			.collect(Collectors.toList());
// 	//	}
// 	//	return Collections.emptyList();
// 	//}
// 	//
// 	//public void setClientAuthenticationMethods(List<String> clientAuthenticationMethods) {
// 	//	this.clientAuthenticationMethods = clientAuthenticationMethods;
// 	//}
// 	//
// 	//public List<AuthorizationGrantType> getAuthorizationGrantTypes() {
// 	//	return authorizationGrantTypes;
// 	//}
// 	//
// 	//public void setAuthorizationGrantTypes(List<AuthorizationGrantType> authorizationGrantTypes) {
// 	//	this.authorizationGrantTypes = authorizationGrantTypes;
// 	//}
// 	//
// 	//public List<String> getRedirectUris() {
// 	//	return redirectUris;
// 	//}
// 	//
// 	//public void setRedirectUris(List<String> redirectUris) {
// 	//	this.redirectUris = redirectUris;
// 	//}
// 	//
// 	//public List<String> getScopes() {
// 	//	return scopes;
// 	//}
// 	//
// 	//public void setScopes(List<String> scopes) {
// 	//	this.scopes = scopes;
// 	//}
// }
