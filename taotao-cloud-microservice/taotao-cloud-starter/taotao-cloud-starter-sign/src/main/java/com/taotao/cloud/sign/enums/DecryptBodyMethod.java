package com.taotao.cloud.sign.enums;


import com.taotao.cloud.sign.util.ISecurity;
import com.taotao.cloud.sign.util.security.AesUtil;
import com.taotao.cloud.sign.util.security.DesUtil;
import com.taotao.cloud.sign.util.security.rsa.RsaUtil;
import com.taotao.cloud.sign.util.security.sm.Sm2Util;
import com.taotao.cloud.sign.util.security.sm.Sm4Util;

/**
 * <p>解密方式</p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:42:03
 */
public enum DecryptBodyMethod {

	/**
	 * 选择DES解密方式
	 */
	DES(new DesUtil()),

	/**
	 * 选择AES解密方式
	 */
	AES(new AesUtil()),

	/**
	 * 选择RSA解密方式
	 */
	RSA(new RsaUtil()),

	/**
	 * 选择SM2解密方式
	 */
	SM2(new Sm2Util()),

	/**
	 * 选择SM4解密方式
	 */
	SM4(new Sm4Util()),
	;

	ISecurity iSecurity;

	DecryptBodyMethod(ISecurity iSecurity) {
		this.iSecurity = iSecurity;
	}

	/**
	 * 超时 0为默认值 不设置时间 TIME_OUT = 60L * 1000L 时间为 60秒
	 */
	public static final long TIME_OUT = 0;

	public ISecurity getSecurity() {
		return iSecurity;
	}

	public void setSecurity(ISecurity iSecurity) {
		this.iSecurity = iSecurity;
	}
}
