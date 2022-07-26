package com.taotao.cloud.open.openapi.config;


import com.taotao.cloud.open.openapi.common.enums.AsymmetricCryEnum;
import com.taotao.cloud.open.openapi.common.enums.CryModeEnum;
import com.taotao.cloud.open.openapi.common.enums.SymmetricCryEnum;

/**
 * 开放api服务端配置类接口，由引入者去实现
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:57
 */
public interface OpenApiConfig {

	/**
	 * 获取本系统的私钥
	 *
	 * @return 本系统私钥
	 */
	String getSelfPrivateKey();

	/**
	 * 获取调用者的公钥
	 *
	 * @param callerId 调用者ID
	 * @return 调用者公钥
	 */
	String getCallerPublicKey(String callerId);

	/**
	 * 获取采用的非对称加密算法(RSA,SM2)
	 *
	 * @return 加密算法
	 * @see AsymmetricCryEnum
	 */
	AsymmetricCryEnum getAsymmetricCry();

	/**
	 * 获取返回值是否加密
	 *
	 * @return 返回值是否加密
	 */
	boolean retEncrypt();

	/**
	 * 获取加密模式
	 *
	 * @return 加密模式
	 */
	default CryModeEnum getCryMode() {
		return CryModeEnum.SYMMETRIC_CRY;
	}

	/**
	 * 获取对称加密算法(AES或SM4)
	 *
	 * @return 对称加密算法
	 * @see SymmetricCryEnum
	 */
	default SymmetricCryEnum getSymmetricCry() {
		return SymmetricCryEnum.AES;
	}

	/**
	 * 配置是否启用接口文档
	 *
	 * @return 是否启用接口文档
	 */
	default boolean enableDoc() {
		//默认启用
		return true;
	}

	/**
	 * 配置是否对HTTP传输的数据启用压缩
	 *
	 * @return 是否启用压缩
	 */
	default boolean enableCompress() {
		//默认禁用
		return false;
	}
}
