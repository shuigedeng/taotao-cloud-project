package com.taotao.cloud.auth.biz.authentication.login.extension.email;

import com.taotao.cloud.auth.biz.authentication.login.extension.captcha.CaptchaAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class EmailAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
	private String emailCode;

    public EmailAuthenticationToken(Object principal,String emailCode) {
        super(null);
		this.emailCode = emailCode;
        this.principal = principal;
        setAuthenticated(false);
    }

    public EmailAuthenticationToken(Object principal, String emailCode,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
		this.emailCode = emailCode;
        this.principal = principal;
        super.setAuthenticated(true);
    }

	public static EmailAuthenticationToken unauthenticated(Object principal,String emailCode) {
		return new EmailAuthenticationToken(principal, emailCode);
	}

	public static EmailAuthenticationToken authenticated(Object principal, String emailCode,Collection<? extends GrantedAuthority> authorities) {
		return new EmailAuthenticationToken(principal, emailCode, authorities);
	}

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}
}
