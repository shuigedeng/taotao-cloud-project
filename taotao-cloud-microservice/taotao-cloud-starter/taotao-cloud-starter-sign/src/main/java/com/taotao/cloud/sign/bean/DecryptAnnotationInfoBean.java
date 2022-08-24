package com.taotao.cloud.sign.bean;


import com.taotao.cloud.sign.enums.DecryptBodyMethod;

/**
 * <p>解密注解信息</p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:41:48
 */
public class DecryptAnnotationInfoBean {

	/**
	 * 解密方法
	 */
	private DecryptBodyMethod decryptBodyMethod;

	/**
	 * 注解key 优先于配置文件key
	 */
	private String key;

	/**
	 * 数据超时时间
	 */
	private long timeOut;

	public DecryptBodyMethod getDecryptBodyMethod() {
		return decryptBodyMethod;
	}

	public void setDecryptBodyMethod(DecryptBodyMethod decryptBodyMethod) {
		this.decryptBodyMethod = decryptBodyMethod;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public static DecryptAnnotationInfoBeanBuilder builder() {
		return new DecryptAnnotationInfoBeanBuilder();
	}


	public static final class DecryptAnnotationInfoBeanBuilder {

		private DecryptBodyMethod decryptBodyMethod;
		private String key;
		private long timeOut;

		private DecryptAnnotationInfoBeanBuilder() {
		}

		public DecryptAnnotationInfoBeanBuilder decryptBodyMethod(
			DecryptBodyMethod decryptBodyMethod) {
			this.decryptBodyMethod = decryptBodyMethod;
			return this;
		}

		public DecryptAnnotationInfoBeanBuilder key(String key) {
			this.key = key;
			return this;
		}

		public DecryptAnnotationInfoBeanBuilder timeOut(long timeOut) {
			this.timeOut = timeOut;
			return this;
		}

		public DecryptAnnotationInfoBean build() {
			DecryptAnnotationInfoBean decryptAnnotationInfoBean = new DecryptAnnotationInfoBean();
			decryptAnnotationInfoBean.setDecryptBodyMethod(decryptBodyMethod);
			decryptAnnotationInfoBean.setKey(key);
			decryptAnnotationInfoBean.setTimeOut(timeOut);
			return decryptAnnotationInfoBean;
		}
	}
}
