package com.taotao.cloud.web.sign.bean;


import com.taotao.cloud.web.sign.enums.EncryptBodyMethod;
import com.taotao.cloud.web.sign.enums.SHAEncryptType;

/**
 * <p>加密注解信息</p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:41:56
 */
public class EncryptAnnotationInfoBean {

	/**
	 * 加密类型
	 */
	private EncryptBodyMethod encryptBodyMethod;

	/**
	 * 注解key 优先于配置文件key
	 */
	private String key;

	private SHAEncryptType shaEncryptType;

	/**
	 * 所需要加密的字段
	 */
	private String encryptMsgName;

	public EncryptBodyMethod getEncryptBodyMethod() {
		return encryptBodyMethod;
	}

	public void setEncryptBodyMethod(EncryptBodyMethod encryptBodyMethod) {
		this.encryptBodyMethod = encryptBodyMethod;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public SHAEncryptType getShaEncryptType() {
		return shaEncryptType;
	}

	public void setShaEncryptType(SHAEncryptType shaEncryptType) {
		this.shaEncryptType = shaEncryptType;
	}

	public String getEncryptMsgName() {
		return encryptMsgName;
	}

	public void setEncryptMsgName(String encryptMsgName) {
		this.encryptMsgName = encryptMsgName;
	}

	public static EncryptAnnotationInfoBeanBuilder builder() {
		return new EncryptAnnotationInfoBeanBuilder();
	}

	public static final class EncryptAnnotationInfoBeanBuilder {

		private EncryptBodyMethod encryptBodyMethod;
		private String key;
		private SHAEncryptType shaEncryptType;
		private String encryptMsgName;

		private EncryptAnnotationInfoBeanBuilder() {
		}

		public EncryptAnnotationInfoBeanBuilder encryptBodyMethod(
			EncryptBodyMethod encryptBodyMethod) {
			this.encryptBodyMethod = encryptBodyMethod;
			return this;
		}

		public EncryptAnnotationInfoBeanBuilder key(String key) {
			this.key = key;
			return this;
		}

		public EncryptAnnotationInfoBeanBuilder shaEncryptType(SHAEncryptType shaEncryptType) {
			this.shaEncryptType = shaEncryptType;
			return this;
		}

		public EncryptAnnotationInfoBeanBuilder encryptMsgName(String encryptMsgName) {
			this.encryptMsgName = encryptMsgName;
			return this;
		}

		public EncryptAnnotationInfoBean build() {
			EncryptAnnotationInfoBean encryptAnnotationInfoBean = new EncryptAnnotationInfoBean();
			encryptAnnotationInfoBean.setEncryptBodyMethod(encryptBodyMethod);
			encryptAnnotationInfoBean.setKey(key);
			encryptAnnotationInfoBean.setShaEncryptType(shaEncryptType);
			encryptAnnotationInfoBean.setEncryptMsgName(encryptMsgName);
			return encryptAnnotationInfoBean;
		}
	}
}
