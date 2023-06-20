/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.userdetails;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.deserializes.TemporaryUserDeserializer;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.properties.Auth2Properties;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * 用于第三方授权登录时, 未开启自动注册且用户是第一次授权登录的临时用户.
 * {@code https://gitee.com/pcore/just-auth-spring-security-starter/issues/I22KP3}
 *
 * @author Ben Alex
 * @author Luke Taylor
 * @author zyw
 */
@SuppressWarnings("ALL")
@Slf4j
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonDeserialize(using = TemporaryUserDeserializer.class)
public class TemporaryUser implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;


	// ~ Instance fields
	// ================================================================================================
	private String password;
	private final String username;
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
	private final Set<GrantedAuthority> authorities;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;
	private final AuthUser authUser;
	private final String encodeState;

	// ~ Constructors
	// ===================================================================================================


	/**
	 * Calls the more complex constructor with all boolean arguments set to {@code true}.
	 */
	/**
	 * Calls the more complex constructor with all boolean arguments set to {@code true}.
	 *
	 * @param username    用户名默认为: username + "_" + providerId + "_" + providerUserId
	 * @param password    密码
	 * @param authorities 权限默认为 {@link Auth2Properties#defaultAuthorities}
	 * @param authUser    第三方授权登录的用户信息
	 * @param encodeState 第三方授权登录的流程中加密后的 state 参数
	 */
	public TemporaryUser(String username,
						 String password,
						 Collection<? extends GrantedAuthority> authorities,
						 AuthUser authUser,
						 String encodeState) {
		this(username, password, true, true, true, true,
			authorities, authUser, encodeState);
	}


	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
	 *
	 * @param username              the username presented to the
	 *                              <code>DaoAuthenticationProvider</code>
	 * @param password              the password that should be presented to the
	 *                              <code>DaoAuthenticationProvider</code>
	 * @param enabled               set to <code>true</code> if the user is enabled
	 * @param accountNonExpired     set to <code>true</code> if the account has not expired
	 * @param credentialsNonExpired set to <code>true</code> if the credentials have not
	 *                              expired
	 * @param accountNonLocked      set to <code>true</code> if the account is not locked
	 * @param authorities           the authorities that should be granted to the caller if they
	 *                              presented the correct username and password and the user is enabled. Not null.
	 * @param authUser              第三方授权登录的用户信息
	 * @param encodeState           第三方授权登录的流程中加密后的 state 参数
	 * @throws IllegalArgumentException if a <code>null</code> value was passed either as
	 *                                  a parameter or as an element in the <code>GrantedAuthority</code> collection
	 */
	public TemporaryUser(String username, String password, boolean enabled,
						 boolean accountNonExpired, boolean credentialsNonExpired,
						 boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
						 AuthUser authUser,
						 String encodeState) {

		if (((username == null) || "".equals(username)) || (password == null)) {
			throw new IllegalArgumentException(
				"Cannot pass null or empty values to constructor");
		}

		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
		this.authUser = authUser;
		this.encodeState = encodeState;
	}

	// ~ Methods
	// ========================================================================================================

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public AuthUser getAuthUser() {
		return authUser;
	}

	public String getEncodeState() {
		return encodeState;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public void eraseCredentials() {
		password = null;
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(
		Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per
		// UserDetails.getAuthorities() contract and SEC-717)
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
			new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority,
				"GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
	}

	private static class AuthorityComparator implements Comparator<GrantedAuthority>,
		Serializable {
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

		@Override
		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			// Neither should ever be null as each entry is checked before adding it to
			// the set.
			// If the authority is null, it is a custom authority and should precede
			// others.
			if (g2.getAuthority() == null) {
				return -1;
			}

			if (g1.getAuthority() == null) {
				return 1;
			}

			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}

	/**
	 * Returns {@code true} if the supplied object is a {@code User} instance with the
	 * same {@code username} value.
	 * <p>
	 * In other words, the objects are equal if they have the same username, representing
	 * the same principal.
	 */
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof TemporaryUser) {
			return username.equals(((TemporaryUser) rhs).username);
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code username}.
	 */
	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		sb.append("Username: ").append(this.username).append("; ");
		sb.append("Password: [PROTECTED]; ");
		sb.append("Enabled: ").append(this.enabled).append("; ");
		sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
		sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired)
			.append("; ");
		sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");
		sb.append("authUser: ").append(this.authUser.toString()).append("; ");
		sb.append("encodeState: ").append(this.encodeState).append("; ");

		if (!authorities.isEmpty()) {
			sb.append("Granted Authorities: ");

			boolean first = true;
			for (GrantedAuthority auth : authorities) {
				if (!first) {
					sb.append(",");
				}
				first = false;

				sb.append(auth);
			}
		} else {
			sb.append("Not granted any authorities");
		}

		return sb.toString();
	}

	/**
	 * Creates a UserBuilder with a specified user name
	 *
	 * @param username the username to use
	 * @return the UserBuilder
	 */
	public static UserBuilder withUsername(String username) {
		return builder().username(username);
	}

	/**
	 * Creates a UserBuilder
	 *
	 * @return the UserBuilder
	 */
	public static UserBuilder builder() {
		return new UserBuilder();
	}

	/**
	 * <p>
	 * <b>WARNING:</b> This method is considered unsafe for production and is only intended
	 * for sample applications.
	 * </p>
	 * <p>
	 * Creates a user and automatically encodes the provided password using
	 * {@code PasswordEncoderFactories.createDelegatingPasswordEncoder()}. For example:
	 * </p>
	 *
	 * <pre>
	 * <code>
	 * UserDetails user = User.withDefaultPasswordEncoder()
	 *     .username("user")
	 *     .password("password")
	 *     .roles("USER")
	 *     .build();
	 * // outputs {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
	 * System.out.println(user.getPassword());
	 * </code>
	 * </pre>
	 * <p>
	 * This is not safe for production (it is intended for getting started experience)
	 * because the password "password" is compiled into the source code and then is
	 * included in memory at the time of creation. This means there are still ways to
	 * recover the plain text password making it unsafe. It does provide a slight
	 * improvement to using plain text passwords since the UserDetails password is
	 * securely hashed. This means if the UserDetails password is accidentally exposed,
	 * the password is securely stored.
	 * <p>
	 * In a production setting, it is recommended to hash the password ahead of time.
	 * For example:
	 *
	 * <pre>
	 * <code>
	 * PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	 * // outputs {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
	 * // remember the password that is printed out and use in the next step
	 * System.out.println(encoder.encode("password"));
	 * </code>
	 * </pre>
	 *
	 * <pre>
	 * <code>
	 * UserDetails user = User.withUsername("user")
	 *     .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
	 *     .roles("USER")
	 *     .build();
	 * </code>
	 * </pre>
	 *
	 * @return a UserBuilder that automatically encodes the password with the default
	 * PasswordEncoder
	 * @deprecated Using this method is not considered safe for production, but is
	 * acceptable for demos and getting started. For production purposes, ensure the
	 * password is encoded externally. See the method Javadoc for additional details.
	 * There are no plans to remove this support. It is deprecated to indicate
	 * that this is considered insecure for production purposes.
	 */
	@Deprecated
	public static UserBuilder withDefaultPasswordEncoder() {
		log.warn("User.withDefaultPasswordEncoder() is considered unsafe for production and is only intended for sample applications.");
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return builder().passwordEncoder(encoder::encode);
	}

	/**
	 * Builds the user to be added. At minimum the username, password, and authorities
	 * should provided. The remaining attributes have reasonable defaults.
	 */
	public static class UserBuilder {
		private String username;
		private String password;
		private List<GrantedAuthority> authorities;
		private AuthUser authUser;
		private String encodeState;
		private boolean accountExpired;
		private boolean accountLocked;
		private boolean credentialsExpired;
		private boolean disabled;
		private Function<String, String> passwordEncoder = password -> password;

		/**
		 * Creates a new instance
		 */
		private UserBuilder() {
		}

		/**
		 * Populates the username. This attribute is required.
		 *
		 * @param username the username. Cannot be null.
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder username(String username) {
			Assert.notNull(username, "username cannot be null");
			this.username = username;
			return this;
		}

		/**
		 * Populates the password. This attribute is required.
		 *
		 * @param password the password. Cannot be null.
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder password(String password) {
			Assert.notNull(password, "password cannot be null");
			this.password = password;
			return this;
		}

		/**
		 * Encodes the current password (if non-null) and any future passwords supplied
		 * to {@link #password(String)}.
		 *
		 * @param encoder the encoder to use
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder passwordEncoder(Function<String, String> encoder) {
			Assert.notNull(encoder, "encoder cannot be null");
			this.passwordEncoder = encoder;
			return this;
		}

		/**
		 * Populates the roles. This method is a shortcut for calling
		 * {@link #authorities(String...)}, but automatically prefixes each entry with
		 * "ROLE_". This means the following:
		 *
		 * <code>
		 * builder.roles("USER","ADMIN");
		 * </code>
		 * <p>
		 * is equivalent to
		 *
		 * <code>
		 * builder.authorities("ROLE_USER","ROLE_ADMIN");
		 * </code>
		 *
		 * <p>
		 * This attribute is required, but can also be populated with
		 * {@link #authorities(String...)}.
		 * </p>
		 *
		 * @param roles the roles for this user (i.e. USER, ADMIN, etc). Cannot be null,
		 *              contain null values or start with "ROLE_"
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder roles(String... roles) {
			List<GrantedAuthority> authorities = new ArrayList<>(
				roles.length);
			for (String role : roles) {
				Assert.isTrue(!role.startsWith("ROLE_"), () -> role
					+ " cannot start with ROLE_ (it is automatically added)");
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			return authorities(authorities);
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities the authorities for this user. Cannot be null, or contain
		 *                    null values
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 * @see #roles(String...)
		 */
		public UserBuilder authorities(GrantedAuthority... authorities) {
			return authorities(Arrays.asList(authorities));
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities the authorities for this user. Cannot be null, or contain
		 *                    null values
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 * @see #roles(String...)
		 */
		public UserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
			this.authorities = new ArrayList<>(authorities);
			return this;
		}

		/**
		 * Populates the authorities. This attribute is required.
		 *
		 * @param authorities the authorities for this user (i.e. ROLE_USER, ROLE_ADMIN,
		 *                    etc). Cannot be null, or contain null values
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 * @see #roles(String...)
		 */
		public UserBuilder authorities(String... authorities) {
			return authorities(AuthorityUtils.createAuthorityList(authorities));
		}

		/**
		 * Populates the authUser. This authUser is required.
		 *
		 * @param authUser 第三方用户信息
		 * @return {@link UserBuilder} for method chaining (i.e. to populate
		 * additional authUser for this user)
		 */
		public UserBuilder authUser(AuthUser authUser) {
			this.authUser = authUser;
			return this;
		}

		/**
		 * Populates the encodeState. This encodeState is required.
		 *
		 * @param encodeState 第三方授权登录的流程中加密后的 state 参数
		 * @return {@link UserBuilder} for method chaining (i.e. to populate
		 * additional encodeState for this user)
		 */
		public UserBuilder encodeState(String encodeState) {
			this.encodeState = encodeState;
			return this;
		}

		/**
		 * Defines if the account is expired or not. Default is false.
		 *
		 * @param accountExpired true if the account is expired, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder accountExpired(boolean accountExpired) {
			this.accountExpired = accountExpired;
			return this;
		}

		/**
		 * Defines if the account is locked or not. Default is false.
		 *
		 * @param accountLocked true if the account is locked, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder accountLocked(boolean accountLocked) {
			this.accountLocked = accountLocked;
			return this;
		}

		/**
		 * Defines if the credentials are expired or not. Default is false.
		 *
		 * @param credentialsExpired true if the credentials are expired, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder credentialsExpired(boolean credentialsExpired) {
			this.credentialsExpired = credentialsExpired;
			return this;
		}

		/**
		 * Defines if the account is disabled or not. Default is false.
		 *
		 * @param disabled true if the account is disabled, false otherwise
		 * @return the {@link UserBuilder} for method chaining (i.e. to populate
		 * additional attributes for this user)
		 */
		public UserBuilder disabled(boolean disabled) {
			this.disabled = disabled;
			return this;
		}

		public UserDetails build() {
			String encodedPassword = this.passwordEncoder.apply(password);
			return new TemporaryUser(username, encodedPassword, !disabled, !accountExpired,
				!credentialsExpired, !accountLocked, authorities,
				authUser, encodeState);
		}
	}
}
