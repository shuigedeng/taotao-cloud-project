package com.taotao.cloud.oauth2.biz.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

public class CloudUserDetails implements UserDetails, CredentialsContainer, Cloneable,
	Serializable {

	@Serial
	private static final long serialVersionUID = -3685249101751401211L;

	private Long id;
	private String username;
	private String password;
	private Set<GrantedAuthority> authorities;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;

	public CloudUserDetails() {
	}

	public CloudUserDetails(Long id, String username, String password,
		Collection<? extends GrantedAuthority> authorities) {
		this(id, username, password, true, true, true,
			true, authorities);
	}

	public CloudUserDetails(Long id, String username, String password, boolean accountNonExpired,
		boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled,
		Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
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
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	@Override
	public CloudUserDetails clone() {
		try {
			return (CloudUserDetails) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder withId(Long id) {
		return new Builder().id(id);
	}

	private SortedSet<GrantedAuthority> sortAuthorities(
		Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per
		// UserDetails.getAuthorities() contract and SEC-717)
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>((g1, g2) -> {
			if (g2.getAuthority() == null) {
				return -1;
			}
			if (g1.getAuthority() == null) {
				return 1;
			}
			return g1.getAuthority().compareTo(g2.getAuthority());
		});

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority,
				"GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}
		return sortedAuthorities;
	}

	public static final class Builder {

		private Long id;
		private String username;
		private String password;
		private List<GrantedAuthority> authorities;
		private boolean accountNonExpired;
		private boolean accountNonLocked;
		private boolean credentialsNonExpired;
		private boolean enabled;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder username(String username) {
			Assert.notNull(username, "username cannot be null.");
			this.username = username;
			return this;
		}

		public Builder password(String password) {
			Assert.notNull(password, "password cannot be null.");
			this.password = password;
			return this;
		}

		public Builder roles(String... roles) {
			List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
			for (String role : roles) {
				Assert.isTrue(!role.startsWith("ROLE_"),
					() -> role + " cannot start with ROLE_ (it is automatically added)");
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			return authorities(authorities);
		}

		private Builder authorities(Collection<? extends GrantedAuthority> authorities) {
			this.authorities = new ArrayList<>(authorities);
			return this;
		}

		public Builder accountNonExpired(Boolean accountNonExpired) {
			this.accountNonExpired = accountNonExpired;
			return this;
		}

		public Builder accountNonLocked(Boolean accountNonLocked) {
			this.accountNonLocked = accountNonLocked;
			return this;
		}

		public Builder credentialsNonExpired(Boolean credentialsNonExpired) {
			this.credentialsNonExpired = credentialsNonExpired;
			return this;
		}

		public Builder enabled(Boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public CloudUserDetails build() {
			return new CloudUserDetails(id, username, password, accountNonExpired, accountNonLocked,
				credentialsNonExpired, enabled, authorities);
		}
	}
}
