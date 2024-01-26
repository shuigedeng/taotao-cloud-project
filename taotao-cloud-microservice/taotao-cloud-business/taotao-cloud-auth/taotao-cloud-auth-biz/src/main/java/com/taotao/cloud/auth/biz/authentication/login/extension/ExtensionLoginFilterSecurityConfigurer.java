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
import com.taotao.cloud.auth.biz.authentication.login.extension.captcha.CaptchaExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.email.EmailExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.face.FaceExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.fingerprint.FingerprintExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.gestures.GesturesExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.OneClickExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.QrcodeExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.sms.SmsExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatminiapp.WechatMiniAppExtensionLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.extension.wechatmp.WechatMpExtensionLoginFilterConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * 基于spring security自定义扩展登录方式(基于json请求)
 *
 * @author shuigedeng
 * @version 2023.07
 * @see SecurityConfigurerAdapter
 * @since 2023-07-10 17:42:42
 */
public class ExtensionLoginFilterSecurityConfigurer<H extends HttpSecurityBuilder<H>>
	extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

	private AccountExtensionLoginFilterConfigurer<H> accountLoginFilterConfigurer;
	private CaptchaExtensionLoginFilterConfigurer<H> captchaLoginFilterConfigurer;
	private EmailExtensionLoginFilterConfigurer<H> emailLoginFilterConfigurer;
	private FaceExtensionLoginFilterConfigurer<H> faceLoginFilterConfigurer;
	private FingerprintExtensionLoginFilterConfigurer<H> fingerprintLoginFilterConfigurer;
	private GesturesExtensionLoginFilterConfigurer<H> gesturesLoginFilterConfigurer;
	private OneClickExtensionLoginFilterConfigurer<H> oneClickLoginFilterConfigurer;
	private QrcodeExtensionLoginFilterConfigurer<H> qrcodeLoginFilterConfigurer;
	private SmsExtensionLoginFilterConfigurer<H> smsLoginFilterConfigurer;
	private WechatMpExtensionLoginFilterConfigurer<H> wechatMpLoginFilterConfigurer;
	private WechatMiniAppExtensionLoginFilterConfigurer<H> wechatMiniAppLoginFilterConfigurer;

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public EmailExtensionLoginFilterConfigurer<H> emailLogin() {
		return lazyInitEmailLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param emailLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> emailLogin(
		Customizer<EmailExtensionLoginFilterConfigurer<H>> emailLoginConfigurerCustomizer) {
		emailLoginConfigurerCustomizer.customize(lazyInitEmailLoginFilterConfigurer());
		return this;
	}

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
	 * @param fingerprintLoginConfigurerCustomizer the captcha login filter configurer customizer
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
	public CaptchaExtensionLoginFilterConfigurer<H> captchaLogin() {
		return lazyInitCaptchaLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param captchaLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> captchaLogin(
		Customizer<CaptchaExtensionLoginFilterConfigurer<H>> captchaLoginConfigurerCustomizer) {
		captchaLoginConfigurerCustomizer.customize(lazyInitCaptchaLoginFilterConfigurer());
		return this;
	}

	/**
	 * Captcha login captcha login filter configurer.
	 *
	 * @return the captcha login filter configurer
	 */
	public SmsExtensionLoginFilterConfigurer<H> smsLogin() {
		return lazyInitSmsLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param smsLoginConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> smsLogin(
		Customizer<SmsExtensionLoginFilterConfigurer<H>> smsLoginConfigurerCustomizer) {
		smsLoginConfigurerCustomizer.customize(lazyInitSmsLoginFilterConfigurer());
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
	 * @param wechatMiniAppLoginConfigurerCustomizer the mini app login filter configurer
	 *                                               customizer
	 * @return the login filter security configurer
	 */
	public ExtensionLoginFilterSecurityConfigurer<H> wechatMiniAppLogin(
		Customizer<WechatMiniAppExtensionLoginFilterConfigurer<H>> wechatMiniAppLoginConfigurerCustomizer) {
		wechatMiniAppLoginConfigurerCustomizer.customize(lazyInitMiniAppLoginFilterConfigurer());
		return this;
	}

	@Override
	public void init(H builder) throws Exception {
		init(emailLoginFilterConfigurer, builder);
		init(accountLoginFilterConfigurer, builder);
		init(captchaLoginFilterConfigurer, builder);
		init(faceLoginFilterConfigurer, builder);
		init(fingerprintLoginFilterConfigurer, builder);
		init(gesturesLoginFilterConfigurer, builder);
		init(wechatMpLoginFilterConfigurer, builder);
		init(oneClickLoginFilterConfigurer, builder);
		init(qrcodeLoginFilterConfigurer, builder);
		init(smsLoginFilterConfigurer, builder);
		init(wechatMiniAppLoginFilterConfigurer, builder);
	}

	private <E extends AbstractExtensionLoginFilterConfigurer<H, ?, ?, ?>> void init(E e,
		H builder) {
		if (e != null) {
			e.init(builder);
		}
	}

	private <E extends AbstractExtensionLoginFilterConfigurer<H, ?, ?, ?>> void configure(E e,
		H builder)
		throws Exception {
		if (e != null) {
			e.configure(builder);
		}
	}

	@Override
	public void configure(H builder) throws Exception {
		configure(emailLoginFilterConfigurer, builder);
		configure(accountLoginFilterConfigurer, builder);
		configure(captchaLoginFilterConfigurer, builder);
		configure(faceLoginFilterConfigurer, builder);
		configure(fingerprintLoginFilterConfigurer, builder);
		configure(gesturesLoginFilterConfigurer, builder);
		configure(wechatMpLoginFilterConfigurer, builder);
		configure(oneClickLoginFilterConfigurer, builder);
		configure(qrcodeLoginFilterConfigurer, builder);
		configure(smsLoginFilterConfigurer, builder);
		configure(wechatMiniAppLoginFilterConfigurer, builder);
	}

	private EmailExtensionLoginFilterConfigurer<H> lazyInitEmailLoginFilterConfigurer() {
		if (emailLoginFilterConfigurer == null) {
			this.emailLoginFilterConfigurer = new EmailExtensionLoginFilterConfigurer<>(this);
		}
		return emailLoginFilterConfigurer;
	}

	private SmsExtensionLoginFilterConfigurer<H> lazyInitSmsLoginFilterConfigurer() {
		if (smsLoginFilterConfigurer == null) {
			this.smsLoginFilterConfigurer = new SmsExtensionLoginFilterConfigurer<>(this);
		}
		return smsLoginFilterConfigurer;
	}

	private WechatMiniAppExtensionLoginFilterConfigurer<H> lazyInitMiniAppLoginFilterConfigurer() {
		if (wechatMiniAppLoginFilterConfigurer == null) {
			this.wechatMiniAppLoginFilterConfigurer = new WechatMiniAppExtensionLoginFilterConfigurer<>(
				this);
		}
		return wechatMiniAppLoginFilterConfigurer;
	}

	private CaptchaExtensionLoginFilterConfigurer<H> lazyInitCaptchaLoginFilterConfigurer() {
		if (captchaLoginFilterConfigurer == null) {
			this.captchaLoginFilterConfigurer = new CaptchaExtensionLoginFilterConfigurer<>(this);
		}
		return captchaLoginFilterConfigurer;
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
			this.fingerprintLoginFilterConfigurer = new FingerprintExtensionLoginFilterConfigurer<>(
				this);
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

}
