package com.taotao.cloud.openapi.common.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.openapi.common.constant.ErrorCode;
import com.taotao.cloud.openapi.common.enums.DataType;
import com.taotao.cloud.openapi.common.util.TruncateUtil;

/**
 * openapi出参
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:07:05
 */
public class OutParams {

	/**
	 * 流水号
	 */
	private String uuid;

	/**
	 * 网关返回码
	 */
	private Integer code;

	/**
	 * 网关返回消息
	 */
	private String message;

	/**
	 * 目标接口返回值
	 */
	private String data;

	/**
	 * 返回的二进制数据
	 * 注：数据为{@link Binary}类型中的二进制数据data
	 */
	private byte[] binaryData;

	/**
	 * 返回值（字节数组形式，由sdk生成的内容密文也保存至此）
	 */
	private byte[] dataBytes;

	/**
	 * 用于日志打印
	 */
	private String dataBytesStr;

	/**
	 * 对称加密Key(由sdk生成)
	 */
	private String symmetricCryKey;

	/**
	 * 传输的数据类型
	 */
	private DataType dataType;


	/**
	 * 调用成功的结果
	 *
	 * @return 输出参数
	 */
	public static OutParams success() {
		OutParams outParams = new OutParams();
		outParams.code = ErrorCode.SUCCESS;
		return outParams;
	}

	/**
	 * 调用成功的结果
	 *
	 * @param data 数据
	 * @return 输出参数
	 */
	public static OutParams success(String data) {
		OutParams outParams = new OutParams();
		outParams.code = ErrorCode.SUCCESS;
		outParams.data = data;
		return outParams;
	}

	/**
	 * 调用失败的结果
	 *
	 * @param message 错误消息
	 * @return 输出参数
	 */
	public static OutParams error(String message) {
		OutParams outParams = new OutParams();
		outParams.code = ErrorCode.FAILED;
		outParams.message = message;
		return outParams;
	}

	/**
	 * 调用失败的结果
	 *
	 * @param code    错误代码
	 * @param message 错误消息
	 * @return 输出参数
	 */
	public static OutParams error(int code, String message) {
		OutParams outParams = new OutParams();
		outParams.code = code;
		outParams.message = message;
		return outParams;
	}

	/**
	 * 判断openapi调用是否成功
	 *
	 * @param outParams 调用结果
	 * @return 是否调用成功
	 */
	public static boolean isSuccess(OutParams outParams) {
		return outParams != null && outParams.code != null && outParams.code == 200;
	}


	@Override
	public String toString() {
		OutParams outParams = new OutParams();
		BeanUtil.copyProperties(this, outParams);
		outParams.setData(TruncateUtil.truncate(outParams.getData()));
		outParams.setDataBytesStr(TruncateUtil.truncate(outParams.getDataBytes()));
		outParams.setDataBytes(null);
		return JSONUtil.toJsonStr(outParams);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public byte[] getBinaryData() {
		return binaryData;
	}

	public void setBinaryData(byte[] binaryData) {
		this.binaryData = binaryData;
	}

	public byte[] getDataBytes() {
		return dataBytes;
	}

	public void setDataBytes(byte[] dataBytes) {
		this.dataBytes = dataBytes;
	}

	public String getDataBytesStr() {
		return dataBytesStr;
	}

	public void setDataBytesStr(String dataBytesStr) {
		this.dataBytesStr = dataBytesStr;
	}

	public String getSymmetricCryKey() {
		return symmetricCryKey;
	}

	public void setSymmetricCryKey(String symmetricCryKey) {
		this.symmetricCryKey = symmetricCryKey;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
}
