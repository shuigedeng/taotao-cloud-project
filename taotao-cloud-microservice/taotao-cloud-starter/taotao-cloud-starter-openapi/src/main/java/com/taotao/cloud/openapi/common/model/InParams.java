package com.taotao.cloud.openapi.common.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.openapi.common.enums.DataType;
import com.taotao.cloud.openapi.common.util.TruncateUtil;

/**
 * openapi入参
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:07:00
 */
public class InParams {

    /**
     * 流水号
     */
    private String uuid;

    /**
     * 调用者ID
     */
    private String callerId;

    /**
     * 接口名
     */
    private String api;

    /**
     * 方法名
     */
    private String method;

    /**
     * 请求体内容（传入内容明文）
     */
    private String body;

    /**
     * 请求体内容（字节数组形式，由sdk生成的内容密文也保存至此）
     */
    private byte[] bodyBytes;

    /**
     * 用于日志打印
     */
    private String bodyBytesStr;

    /**
     * 签名（由sdk根据内容密文生成）
     */
    private String sign;

    /**
     * 对称加密Key(由sdk生成)
     */
    private String symmetricCryKey;

    /**
     * 是否是多参方法（由sdk判断）
     */
    private boolean multiParam;

    /**
     * 传输的数据类型
     */
    private DataType dataType;

    @Override
    public String toString() {
        InParams inParams = new InParams();
        BeanUtil.copyProperties(this, inParams);
        inParams.setBody(TruncateUtil.truncate(inParams.getBody()));
        inParams.setBodyBytesStr(TruncateUtil.truncate(inParams.getBodyBytes()));
        inParams.setBodyBytes(null);
        return JSONUtil.toJsonStr(inParams);
    }

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public byte[] getBodyBytes() {
		return bodyBytes;
	}

	public void setBodyBytes(byte[] bodyBytes) {
		this.bodyBytes = bodyBytes;
	}

	public String getBodyBytesStr() {
		return bodyBytesStr;
	}

	public void setBodyBytesStr(String bodyBytesStr) {
		this.bodyBytesStr = bodyBytesStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSymmetricCryKey() {
		return symmetricCryKey;
	}

	public void setSymmetricCryKey(String symmetricCryKey) {
		this.symmetricCryKey = symmetricCryKey;
	}

	public boolean isMultiParam() {
		return multiParam;
	}

	public void setMultiParam(boolean multiParam) {
		this.multiParam = multiParam;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
}
