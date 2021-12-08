package com.taotao.cloud.oauth2.biz.models;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class CloudAuthAccount extends AbstractAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Integer loginAttempts;
    private Boolean loginLocked;
    private LocalDateTime unlockTime;

    //private Set<String> roles;

	private Boolean enabled;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean accountNonLocked;

	public CloudAuthAccount() {
	}

	public CloudAuthAccount(Long id, String username, String password,
		Set<String> authorities) {
		this(id, username, password, true, true, true, true, authorities);
	}

	public CloudAuthAccount(Long id, String username, String password, boolean enabled,
		boolean accountNonExpired,
		boolean credentialsNonExpired, boolean accountNonLocked, Set<String> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		//this.roles = Collections.unmodifiableSet(authorities);
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Boolean getLoginLocked() {
        return loginLocked;
    }

    public void setLoginLocked(Boolean loginLocked) {
        this.loginLocked = loginLocked;
    }

    public LocalDateTime getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(LocalDateTime unlockTime) {
        this.unlockTime = unlockTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public boolean accountLocked() {
        return !enabled && (loginLocked || unlockTime.isAfter(LocalDateTime.now()));
    }

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Set<String> getRoles() {
		Set<String> roles = new HashSet<>();
		roles.add("ADMIN");
		roles.add("USER");
		return roles;
	}


	public List<GrantedAuthority> getAuthorities() {
		//todo
		return new ArrayList<>();
	}
}
