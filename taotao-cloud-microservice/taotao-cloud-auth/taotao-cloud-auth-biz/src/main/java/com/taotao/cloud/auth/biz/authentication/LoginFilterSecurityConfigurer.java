package com.taotao.cloud.auth.biz.authentication;


import com.taotao.cloud.auth.biz.authentication.account.AccountLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.accountVerification.AccountVerificationLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.face.FaceLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.fingerprint.FingerprintLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.gestures.GesturesLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.miniapp.MiniAppLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.mp.MpLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.oneClick.OneClickLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.phone.PhoneLoginFilterConfigurer;
import com.taotao.cloud.auth.biz.authentication.qrcocde.QrcodeLoginFilterConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;


/**
 * The type Login filter security configurer.
 *
 * @param <H> the type parameter
 */
public class LoginFilterSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends
	SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

	private AccountLoginFilterConfigurer<H> accountLoginFilterConfigurer;
	private AccountVerificationLoginFilterConfigurer<H> accountVerificationLoginFilterConfigurer;
	private FaceLoginFilterConfigurer<H> faceLoginFilterConfigurer;
	private FingerprintLoginFilterConfigurer<H> fingerprintLoginFilterConfigurer;
	private GesturesLoginFilterConfigurer<H> gesturesLoginFilterConfigurer;
	private MpLoginFilterConfigurer<H> mpLoginFilterConfigurer;
	private OneClickLoginFilterConfigurer<H> oneClickLoginFilterConfigurer;
	private QrcodeLoginFilterConfigurer<H> qrcodeLoginFilterConfigurer;
	private PhoneLoginFilterConfigurer<H> phoneLoginFilterConfigurer;
	private MiniAppLoginFilterConfigurer<H> miniAppLoginFilterConfigurer;

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
	public MpLoginFilterConfigurer<H> mpLogin() {
		return lazyInitMpLoginFilterConfigurer();
	}

	/**
	 * Captcha login login filter security configurer.
	 *
	 * @param mpLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> mpLogin(
		Customizer<MpLoginFilterConfigurer<H>> mpLoginFilterConfigurerCustomizer) {
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
	 * @param fingerprintLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
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
	 * @param accountVerificationLoginFilterConfigurerCustomizer the captcha login filter configurer customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> accountVerificationLogin(
		Customizer<AccountVerificationLoginFilterConfigurer<H>> accountVerificationLoginFilterConfigurerCustomizer) {
		accountVerificationLoginFilterConfigurerCustomizer.customize(lazyInitAccountVerificationLoginFilterConfigurer());
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
	public MiniAppLoginFilterConfigurer<H> miniAppLogin() {
		return lazyInitMiniAppLoginFilterConfigurer();
	}

	/**
	 * Mini app login login filter security configurer.
	 *
	 * @param miniAppLoginFilterConfigurerCustomizer the mini app login filter configurer
	 *                                               customizer
	 * @return the login filter security configurer
	 */
	public LoginFilterSecurityConfigurer<H> miniAppLogin(
		Customizer<MiniAppLoginFilterConfigurer<H>> miniAppLoginFilterConfigurerCustomizer) {
		miniAppLoginFilterConfigurerCustomizer.customize(lazyInitMiniAppLoginFilterConfigurer());
		return this;
	}

	@Override
	public void init(H builder) throws Exception {
		if (phoneLoginFilterConfigurer != null) {
			phoneLoginFilterConfigurer.init(builder);
		}
		if (miniAppLoginFilterConfigurer != null) {
			miniAppLoginFilterConfigurer.init(builder);
		}
	}

	@Override
	public void configure(H builder) throws Exception {
		if (phoneLoginFilterConfigurer != null) {
			phoneLoginFilterConfigurer.configure(builder);
		}
		if (miniAppLoginFilterConfigurer != null) {
			miniAppLoginFilterConfigurer.configure(builder);
		}
	}

	private PhoneLoginFilterConfigurer<H> lazyInitPhoneLoginFilterConfigurer() {
		if (phoneLoginFilterConfigurer == null) {
			this.phoneLoginFilterConfigurer = new PhoneLoginFilterConfigurer<>(this);
		}
		return phoneLoginFilterConfigurer;
	}

	private MiniAppLoginFilterConfigurer<H> lazyInitMiniAppLoginFilterConfigurer() {
		if (miniAppLoginFilterConfigurer == null) {
			this.miniAppLoginFilterConfigurer = new MiniAppLoginFilterConfigurer<>(this);
		}
		return miniAppLoginFilterConfigurer;
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

	private MpLoginFilterConfigurer<H> lazyInitMpLoginFilterConfigurer() {
		if (mpLoginFilterConfigurer == null) {
			this.mpLoginFilterConfigurer = new MpLoginFilterConfigurer<>(this);
		}
		return mpLoginFilterConfigurer;
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
