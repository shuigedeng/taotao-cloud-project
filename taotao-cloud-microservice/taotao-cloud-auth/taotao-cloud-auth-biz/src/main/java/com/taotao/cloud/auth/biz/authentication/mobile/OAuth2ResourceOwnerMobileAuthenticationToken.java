package com.taotao.cloud.auth.biz.authentication.mobile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

public class OAuth2ResourceOwnerMobileAuthenticationToken extends AbstractAuthenticationToken {

	private final AuthorizationGrantType authorizationGrantType;
	private final Authentication clientPrincipal;
	private final Set<String> scopes;
	private final Map<String, Object> additionalParameters;

	private final Object user;

	/**
	 * Constructs an {@code OAuth2ClientCredentialsAuthenticationToken} using the provided
	 * parameters.
	 *
	 * @param clientPrincipal the authenticated client principal
	 */

	public OAuth2ResourceOwnerMobileAuthenticationToken(
		AuthorizationGrantType authorizationGrantType,
		Authentication clientPrincipal,
		@Nullable Set<String> scopes,
		@Nullable Map<String, Object> additionalParameters) {
		super(Collections.emptyList());

		Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
		Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");

		this.authorizationGrantType = authorizationGrantType;
		this.clientPrincipal = clientPrincipal;
		this.scopes = Collections.unmodifiableSet(
			scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
		this.additionalParameters = Collections.unmodifiableMap(
			additionalParameters != null ? new HashMap<>(additionalParameters)
				: Collections.emptyMap());

		this.user = null;
	}

	public OAuth2ResourceOwnerMobileAuthenticationToken(
		AuthorizationGrantType authorizationGrantType,
		Authentication clientPrincipal,
		@Nullable Set<String> scopes,
		@Nullable Map<String, Object> additionalParameters,
		Object user,
		Collection<? extends GrantedAuthority> authorities) {
		super(authorities);

		Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
		Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");

		this.authorizationGrantType = authorizationGrantType;
		this.clientPrincipal = clientPrincipal;
		this.scopes = Collections.unmodifiableSet(
			scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
		this.additionalParameters = Collections.unmodifiableMap(
			additionalParameters != null ? new HashMap<>(additionalParameters)
				: Collections.emptyMap());

		this.user = user;
	}

	/**
	 * Returns the authorization grant type.
	 *
	 * @return the authorization grant type
	 */
	public AuthorizationGrantType getGrantType() {
		return this.authorizationGrantType;
	}

	@Override
	public Object getPrincipal() {
		return this.user;
	}

	@Override
	public Object getCredentials() {
		return "";
	}

	/**
	 * Returns the requested scope(s).
	 *
	 * @return the requested scope(s), or an empty {@code Set} if not available
	 */
	public Set<String> getScopes() {
		return this.scopes;
	}

	/**
	 * Returns the additional parameters.
	 *
	 * @return the additional parameters
	 */
	public Map<String, Object> getAdditionalParameters() {
		return this.additionalParameters;
	}

	public Authentication getClientPrincipal() {
		return clientPrincipal;
	}
}
