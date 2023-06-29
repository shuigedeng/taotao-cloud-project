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

package com.taotao.cloud.auth.biz.authentication.login.extension;

import com.taotao.cloud.auth.biz.authentication.login.extension.account.AccountExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.accountVerification.AccountVerificationExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.face.FaceExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.fingerprint.FingerprintExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.gestures.GesturesExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.OneClickExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.phone.PhoneExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.QrcodeExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp.WechatMiniAppExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatmp.WechatMpExtensionLoginFilterConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * 基于spring security自定义扩展登录方式(基于json请求)
 *
 * @param <H> the type parameter
 */
public class ExtensionLoginFilterSecurityConfigurer<H extends HttpSecurityBuilder<H>>
	extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

	private AccountExtensionLoginFilterConfigurer<H> accountLoginFilterConfigurer;
	private AccountVerificationExtensionLoginFilterConfigurer<H> accountVerificationLoginFilterConfigurer;
	private FaceExtensionLoginFilterConfigurer<H> faceLoginFilterConfigurer;
	private FingerprintExtensionLoginFilterConfigurer<H> fingerprintLoginFilterConfigurer;
	private GesturesExtensionLoginFilterConfigurer<H> gesturesLoginFilterConfigurer;
	private OneClickExtensionLoginFilterConfigurer<H> oneClickLoginFilterConfigurer;
	private QrcodeExtensionLoginFilterConfigurer<H> qrcodeLoginFilterConfigurer;
	private PhoneExtensionLoginFilterConfigurer<H> phoneLoginFilterConfigurer;
	private WechatMpExtensionLoginFilterConfigurer<H> wechatMpLoginFilterConfigurer;
	private WechatMiniAppExtensionLoginFilterConfigurer<H> wechatMiniAppLoginFilterConfigurer;

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public QrcodeExtensionLoginFilterConfigurer<H> qrcodeLogin() {
		return lazyInitQrcodeLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param qrcodeLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> qrcodeLogin(
		Customizer<QrcodeExtensionLoginFilterConfigurer<H>> qrcodeLoginConfigurerCustomizer) {
		qrcodeLoginConfigurerCustomizer.customize(lazyInitQrcodeLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public OneClickExtensionLoginFilterConfigurer<H> oneClickLogin() {
		return lazyInitOneClickLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param oneClickLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> oneClickLogin(
		Customizer<OneClickExtensionLoginFilterConfigurer<H>> oneClickLoginConfigurerCustomizer) {
		oneClickLoginConfigurerCustomizer.customize(lazyInitOneClickLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public WechatMpExtensionLoginFilterConfigurer<H> wechatMpLogin() {
		return lazyInitMpLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param wechcatMpLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> wechatMpLogin(
		Customizer<WechatMpExtensionLoginFilterConfigurer<H>> wechcatMpLoginConfigurerCustomizer) {
		wechcatMpLoginConfigurerCustomizer.customize(lazyInitMpLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public GesturesExtensionLoginFilterConfigurer<H> gesturesLogin() {
		return lazyInitGesturesLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param gesturesLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> gesturesLogin(
		Customizer<GesturesExtensionLoginFilterConfigurer<H>> gesturesLoginConfigurerCustomizer) {
		gesturesLoginConfigurerCustomizer.customize(lazyInitGesturesLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public FingerprintExtensionLoginFilterConfigurer<H> fingerprintLogin() {
		return lazyInitFingerprintLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param fingerprintLoginConfigurerCustomizer the captcha login filter configurer
	 *                                             customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> fingerprintLogin(
		Customizer<FingerprintExtensionLoginFilterConfigurer<H>> fingerprintLoginConfigurerCustomizer) {
		fingerprintLoginConfigurerCustomizer.customize(lazyInitFingerprintLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public FaceExtensionLoginFilterConfigurer<H> faceLogin() {
		return lazyInitFaceLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param faceLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> faceLogin(
		Customizer<FaceExtensionLoginFilterConfigurer<H>> faceLoginConfigurerCustomizer) {
		faceLoginConfigurerCustomizer.customize(lazyInitFaceLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public AccountExtensionLoginFilterConfigurer<H> accountLogin() {
		return lazyInitAccountLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param accountLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> accountLogin(
		Customizer<AccountExtensionLoginFilterConfigurer<H>> accountLoginConfigurerCustomizer) {
		accountLoginConfigurerCustomizer.customize(lazyInitAccountLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public AccountVerificationExtensionLoginFilterConfigurer<H> accountVerificationLogin() {
		return lazyInitAccountVerificationLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param accountVerificationLoginConfigurerCustomizer the captcha login filter configurer
	 *                                                     customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> accountVerificationLogin(
		Customizer<AccountVerificationExtensionLoginFilterConfigurer<H>>
			accountVerificationLoginConfigurerCustomizer) {
		accountVerificationLoginConfigurerCustomizer.customize(
			lazyInitAccountVerificationLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public PhoneExtensionLoginFilterConfigurer<H> phoneLogin() {
		return lazyInitPhoneLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param phoneLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> phoneLogin(
		Customizer<PhoneExtensionLoginFilterConfigurer<H>> phoneLoginConfigurerCustomizer) {
		phoneLoginConfigurerCustomizer.customize(lazyInitPhoneLoginFilterConfigurer());
		return this;
	}

	/**
	 * Mini app login mini app login filter configurer.
	 *
	 * @return the mini app login filter configurer
	 */
	public WechatMiniAppExtensionLoginFilterConfigurer<H> wechatMiniAppLogin() {
		return lazyInitMiniAppLoginFilterConfigurer();
	}

	/**
	 * Mini app login login filter security configurer.
	 *
	 * @param wechatMiniAppLoginConfigurerCustomizer the mini app login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> wechatMiniAppLogin(
		Customizer<WechatMiniAppExtensionLoginFilterConfigurer<H>> wechatMiniAppLoginConfigurerCustomizer) {
		wechatMiniAppLoginConfigurerCustomizer.customize(lazyInitMiniAppLoginFilterConfigurer());
		return this;
	}

	@Override
	public void init(H builder) throws Exception {
		if (accountLoginFilterConfigurer != null) {
			accountLoginFilterConfigurer.init(builder);
		}
		if (accountVerificationLoginFilterConfigurer != null) {
			accountVerificationLoginFilterConfigurer.init(builder);
		}
		if (faceLoginFilterConfigurer != null) {
			faceLoginFilterConfigurer.init(builder);
		}
		if (fingerprintLoginFilterConfigurer != null) {
			fingerprintLoginFilterConfigurer.init(builder);
		}
		if (gesturesLoginFilterConfigurer != null) {
			gesturesLoginFilterConfigurer.init(builder);
		}
		if (wechatMpLoginFilterConfigurer != null) {
			wechatMpLoginFilterConfigurer.init(builder);
		}
		if (oneClickLoginFilterConfigurer != null) {
			oneClickLoginFilterConfigurer.init(builder);
		}
		if (qrcodeLoginFilterConfigurer != null) {
			qrcodeLoginFilterConfigurer.init(builder);
		}
		if (phoneLoginFilterConfigurer != null) {
			phoneLoginFilterConfigurer.init(builder);
		}
		if (wechatMiniAppLoginFilterConfigurer != null) {
			wechatMiniAppLoginFilterConfigurer.init(builder);
		}
	}

	@Override
	public void configure(H builder) throws Exception {
		if (accountLoginFilterConfigurer != null) {
			accountLoginFilterConfigurer.configure(builder);
		}
		if (accountVerificationLoginFilterConfigurer != null) {
			accountVerificationLoginFilterConfigurer.configure(builder);
		}
		if (faceLoginFilterConfigurer != null) {
			faceLoginFilterConfigurer.configure(builder);
		}
		if (fingerprintLoginFilterConfigurer != null) {
			fingerprintLoginFilterConfigurer.configure(builder);
		}
		if (gesturesLoginFilterConfigurer != null) {
			gesturesLoginFilterConfigurer.configure(builder);
		}
		if (wechatMpLoginFilterConfigurer != null) {
			wechatMpLoginFilterConfigurer.configure(builder);
		}
		if (oneClickLoginFilterConfigurer != null) {
			oneClickLoginFilterConfigurer.configure(builder);
		}
		if (qrcodeLoginFilterConfigurer != null) {
			qrcodeLoginFilterConfigurer.configure(builder);
		}
		if (phoneLoginFilterConfigurer != null) {
			phoneLoginFilterConfigurer.configure(builder);
		}
		if (wechatMiniAppLoginFilterConfigurer != null) {
			wechatMiniAppLoginFilterConfigurer.configure(builder);
		}
	}

	private PhoneExtensionLoginFilterConfigurer<H> lazyInitPhoneLoginFilterConfigurer() {
		if (phoneLoginFilterConfigurer == null) {
			this.phoneLoginFilterConfigurer = new PhoneExtensionLoginFilterConfigurer<>(this);
		}
		return phoneLoginFilterConfigurer;
	}

	private WechatMiniAppExtensionLoginFilterConfigurer<H> lazyInitMiniAppLoginFilterConfigurer() {
		if (wechatMiniAppLoginFilterConfigurer == null) {
			this.wechatMiniAppLoginFilterConfigurer = new WechatMiniAppExtensionLoginFilterConfigurer<>(this);
		}
		return wechatMiniAppLoginFilterConfigurer;
	}

	private AccountVerificationExtensionLoginFilterConfigurer<H> lazyInitAccountVerificationLoginFilterConfigurer() {
		if (accountVerificationLoginFilterConfigurer == null) {
			this.accountVerificationLoginFilterConfigurer = new AccountVerificationExtensionLoginFilterConfigurer<>(this);
		}
		return accountVerificationLoginFilterConfigurer;
	}

	private AccountExtensionLoginFilterConfigurer<H> lazyInitAccountLoginFilterConfigurer() {
		if (accountLoginFilterConfigurer == null) {
			this.accountLoginFilterConfigurer = new AccountExtensionLoginFilterConfigurer<>(this);
		}
		return accountLoginFilterConfigurer;
	}

	private FaceExtensionLoginFilterConfigurer<H> lazyInitFaceLoginFilterConfigurer() {
		if (faceLoginFilterConfigurer == null) {
			this.faceLoginFilterConfigurer = new FaceExtensionLoginFilterConfigurer<>(this);
		}
		return faceLoginFilterConfigurer;
	}

	private FingerprintExtensionLoginFilterConfigurer<H> lazyInitFingerprintLoginFilterConfigurer() {
		if (fingerprintLoginFilterConfigurer == null) {
			this.fingerprintLoginFilterConfigurer = new FingerprintExtensionLoginFilterConfigurer<>(this);
		}
		return fingerprintLoginFilterConfigurer;
	}

	private GesturesExtensionLoginFilterConfigurer<H> lazyInitGesturesLoginFilterConfigurer() {
		if (gesturesLoginFilterConfigurer == null) {
			this.gesturesLoginFilterConfigurer = new GesturesExtensionLoginFilterConfigurer<>(this);
		}
		return gesturesLoginFilterConfigurer;
	}

	private WechatMpExtensionLoginFilterConfigurer<H> lazyInitMpLoginFilterConfigurer() {
		if (wechatMpLoginFilterConfigurer == null) {
			this.wechatMpLoginFilterConfigurer = new WechatMpExtensionLoginFilterConfigurer<>(this);
		}
		return wechatMpLoginFilterConfigurer;
	}

	private OneClickExtensionLoginFilterConfigurer<H> lazyInitOneClickLoginFilterConfigurer() {
		if (oneClickLoginFilterConfigurer == null) {
			this.oneClickLoginFilterConfigurer = new OneClickExtensionLoginFilterConfigurer<>(this);
		}
		return oneClickLoginFilterConfigurer;
	}

	private QrcodeExtensionLoginFilterConfigurer<H> lazyInitQrcodeLoginFilterConfigurer() {
		if (qrcodeLoginFilterConfigurer == null) {
			this.qrcodeLoginFilterConfigurer = new QrcodeExtensionLoginFilterConfigurer<>(this);
		}
		return qrcodeLoginFilterConfigurer;
	}


	public H httpSecurity() {
		return getBuilder();
	}
}
