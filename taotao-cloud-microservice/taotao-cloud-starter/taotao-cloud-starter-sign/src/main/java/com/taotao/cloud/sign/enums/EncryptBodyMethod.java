package com.taotao.cloud.sign.enums;


import com.taotao.cloud.sign.util.ISecurity;
import com.taotao.cloud.sign.util.security.AesUtil;
import com.taotao.cloud.sign.util.security.DesUtil;
import com.taotao.cloud.sign.util.security.Md5Util;
import com.taotao.cloud.sign.util.security.ShaUtil;
import com.taotao.cloud.sign.util.security.rsa.RsaUtil;
import com.taotao.cloud.sign.util.security.sm.Sm2Util;
import com.taotao.cloud.sign.util.security.sm.Sm3Util;
import com.taotao.cloud.sign.util.security.sm.Sm4Util;

/**
 * <p>加密方式</p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:42:06
 */
public enum EncryptBodyMethod {
	/**
	 * MD5
	 */
	MD5(new Md5Util()),

	/**
	 * DES
	 */
	DES(new DesUtil()),

	/**
	 * AES
	 */
	AES(new AesUtil()),

	/**
	 * SHA
	 */
	SHA(new ShaUtil()),

	/**
	 * RSA
	 */
	RSA(new RsaUtil()),

	/**
	 * 选择SM2解密方式
	 */
	SM2(new Sm2Util()),

	/**
	 * 选择SM3解密方式
	 */
	SM3(new Sm3Util()),

	/**
	 * 选择SM4解密方式
	 */
	SM4(new Sm4Util()),
	;

	ISecurity iSecurity;

	EncryptBodyMethod(ISecurity iSecurity) {
		this.iSecurity = iSecurity;
	}

	public ISecurity getiSecurity() {
		return iSecurity;
	}
}
