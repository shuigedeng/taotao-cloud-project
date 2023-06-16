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

package com.taotao.cloud.auth.biz.authentication.authentication;

import com.taotao.cloud.auth.biz.authentication.authentication.account.AccountLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.accountVerification.AccountVerificationLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.face.FaceLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.fingerprint.FingerprintLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.gestures.GesturesLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.oneClick.OneClickLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.phone.PhoneLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.qrcocde.QrcodeLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.wechatminiapp.WechatMiniAppLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.authentication.wechatmp.WechatMpLoginFilterConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * The type Login filter security configurer.
 *
 * @param <H> the type parameter
 */
public class LoginFilterSecurityConfigurer<H extends HttpSecurityBuilder<H>>
	extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

	private AccountLoginFilterConfigurer<H> accountLoginFilterConfigurer;
	private AccountVerificationLoginFilterConfigurer<H> accountVerificationLoginFilterConfigurer;
	private FaceLoginFilterConfigurer<H> faceLoginFilterConfigurer;
	private FingerprintLoginFilterConfigurer<H> fingerprintLoginFilterConfigurer;
	private GesturesLoginFilterConfigurer<H> gesturesLoginFilterConfigurer;
	private OneClickLoginFilterConfigurer<H> oneClickLoginFilterConfigurer;
	private QrcodeLoginFilterConfigurer<H> qrcodeLoginFilterConfigurer;
	private PhoneLoginFilterConfigurer<H> phoneLoginFilterConfigurer;
	private WechatMpLoginFilterConfigurer<H> wechatMpLoginFilterConfigurer;
	private WechatMiniAppLoginFilterConfigurer<H> wechatMiniAppLoginFilterConfigurer;

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public QrcodeLoginFilterConfigurer<H> qrcodeLogin() {
		return lazyInitQrcodeLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param qrcodeLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> qrcodeLogin(
		Customizer<QrcodeLoginFilterConfigurer<H>> qrcodeLoginFilterConfigurerCustomizer) {
		qrcodeLoginFilterConfigurerCustomizer.customize(lazyInitQrcodeLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public OneClickLoginFilterConfigurer<H> oneClickLogin() {
		return lazyInitOneClickLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param oneClickLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> oneClickLogin(
		Customizer<OneClickLoginFilterConfigurer<H>> oneClickLoginFilterConfigurerCustomizer) {
		oneClickLoginFilterConfigurerCustomizer.customize(lazyInitOneClickLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public WechatMpLoginFilterConfigurer<H> wechatMpLogin() {
		return lazyInitMpLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param mpLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> wechatMpLogin(
		Customizer<WechatMpLoginFilterConfigurer<H>> mpLoginFilterConfigurerCustomizer) {
		mpLoginFilterConfigurerCustomizer.customize(lazyInitMpLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public GesturesLoginFilterConfigurer<H> gesturesLogin() {
		return lazyInitGesturesLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param gesturesLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> gesturesLogin(
		Customizer<GesturesLoginFilterConfigurer<H>> gesturesLoginFilterConfigurerCustomizer) {
		gesturesLoginFilterConfigurerCustomizer.customize(lazyInitGesturesLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public FingerprintLoginFilterConfigurer<H> fingerprintLogin() {
		return lazyInitFingerprintLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param fingerprintLoginFilterConfigurerCustomizer the captcha login filter configurer
	 *                                                   customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> fingerprintLogin(
		Customizer<FingerprintLoginFilterConfigurer<H>> fingerprintLoginFilterConfigurerCustomizer) {
		fingerprintLoginFilterConfigurerCustomizer.customize(lazyInitFingerprintLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public FaceLoginFilterConfigurer<H> faceLogin() {
		return lazyInitFaceLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param faceLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> faceLogin(
		Customizer<FaceLoginFilterConfigurer<H>> faceLoginFilterConfigurerCustomizer) {
		faceLoginFilterConfigurerCustomizer.customize(lazyInitFaceLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public AccountLoginFilterConfigurer<H> accountLogin() {
		return lazyInitAccountLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param accountLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> accountLogin(
		Customizer<AccountLoginFilterConfigurer<H>> accountLoginFilterConfigurerCustomizer) {
		accountLoginFilterConfigurerCustomizer.customize(lazyInitAccountLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public AccountVerificationLoginFilterConfigurer<H> accountVerificationLogin() {
		return lazyInitAccountVerificationLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param accountVerificationLoginFilterConfigurerCustomizer the captcha login filter configurer
	 *                                                           customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> accountVerificationLogin(
		Customizer<AccountVerificationLoginFilterConfigurer<H>>
			accountVerificationLoginFilterConfigurerCustomizer) {
		accountVerificationLoginFilterConfigurerCustomizer.customize(
			lazyInitAccountVerificationLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public PhoneLoginFilterConfigurer<H> phoneLogin() {
		return lazyInitPhoneLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param phoneLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> phoneLogin(
		Customizer<PhoneLoginFilterConfigurer<H>> phoneLoginFilterConfigurerCustomizer) {
		phoneLoginFilterConfigurerCustomizer.customize(lazyInitPhoneLoginFilterConfigurer());
		return this;
	}

	/**
	 * Mini app login mini app login filter configurer.
	 *
	 * @return the mini app login filter configurer
	 */
	public WechatMiniAppLoginFilterConfigurer<H> wechatMiniAppLogin() {
		return lazyInitMiniAppLoginFilterConfigurer();
	}

	/**
	 * Mini app login login filter security configurer.
	 *
	 * @param miniAppLoginFilterConfigurerCustomizer the mini app login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> wechatMiniAppLogin(
		Customizer<WechatMiniAppLoginFilterConfigurer<H>> miniAppLoginFilterConfigurerCustomizer) {
		miniAppLoginFilterConfigurerCustomizer.customize(lazyInitMiniAppLoginFilterConfigurer());
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

	private PhoneLoginFilterConfigurer<H> lazyInitPhoneLoginFilterConfigurer() {
		if (phoneLoginFilterConfigurer == null) {
			this.phoneLoginFilterConfigurer = new PhoneLoginFilterConfigurer<>(this);
		}
		return phoneLoginFilterConfigurer;
	}

	private WechatMiniAppLoginFilterConfigurer<H> lazyInitMiniAppLoginFilterConfigurer() {
		if (wechatMiniAppLoginFilterConfigurer == null) {
			this.wechatMiniAppLoginFilterConfigurer = new WechatMiniAppLoginFilterConfigurer<>(this);
		}
		return wechatMiniAppLoginFilterConfigurer;
	}

	private AccountVerificationLoginFilterConfigurer<H> lazyInitAccountVerificationLoginFilterConfigurer() {
		if (accountVerificationLoginFilterConfigurer == null) {
			this.accountVerificationLoginFilterConfigurer = new AccountVerificationLoginFilterConfigurer<>(this);
		}
		return accountVerificationLoginFilterConfigurer;
	}

	private AccountLoginFilterConfigurer<H> lazyInitAccountLoginFilterConfigurer() {
		if (accountLoginFilterConfigurer == null) {
			this.accountLoginFilterConfigurer = new AccountLoginFilterConfigurer<>(this);
		}
		return accountLoginFilterConfigurer;
	}

	private FaceLoginFilterConfigurer<H> lazyInitFaceLoginFilterConfigurer() {
		if (faceLoginFilterConfigurer == null) {
			this.faceLoginFilterConfigurer = new FaceLoginFilterConfigurer<>(this);
		}
		return faceLoginFilterConfigurer;
	}

	private FingerprintLoginFilterConfigurer<H> lazyInitFingerprintLoginFilterConfigurer() {
		if (fingerprintLoginFilterConfigurer == null) {
			this.fingerprintLoginFilterConfigurer = new FingerprintLoginFilterConfigurer<>(this);
		}
		return fingerprintLoginFilterConfigurer;
	}

	private GesturesLoginFilterConfigurer<H> lazyInitGesturesLoginFilterConfigurer() {
		if (gesturesLoginFilterConfigurer == null) {
			this.gesturesLoginFilterConfigurer = new GesturesLoginFilterConfigurer<>(this);
		}
		return gesturesLoginFilterConfigurer;
	}

	private WechatMpLoginFilterConfigurer<H> lazyInitMpLoginFilterConfigurer() {
		if (wechatMpLoginFilterConfigurer == null) {
			this.wechatMpLoginFilterConfigurer = new WechatMpLoginFilterConfigurer<>(this);
		}
		return wechatMpLoginFilterConfigurer;
	}

	private OneClickLoginFilterConfigurer<H> lazyInitOneClickLoginFilterConfigurer() {
		if (oneClickLoginFilterConfigurer == null) {
			this.oneClickLoginFilterConfigurer = new OneClickLoginFilterConfigurer<>(this);
		}
		return oneClickLoginFilterConfigurer;
	}

	private QrcodeLoginFilterConfigurer<H> lazyInitQrcodeLoginFilterConfigurer() {
		if (qrcodeLoginFilterConfigurer == null) {
			this.qrcodeLoginFilterConfigurer = new QrcodeLoginFilterConfigurer<>(this);
		}
		return qrcodeLoginFilterConfigurer;
	}
}
