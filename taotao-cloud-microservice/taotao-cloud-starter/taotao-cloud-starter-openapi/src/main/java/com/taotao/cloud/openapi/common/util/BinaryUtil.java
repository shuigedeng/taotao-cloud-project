package com.taotao.cloud.openapi.common.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ByteUtil;
import com.taotao.cloud.openapi.common.model.Binary;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 二进制对象工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:07:25
 */
public class BinaryUtil {

	/**
	 * 判断是否是二进制类型参数(包括数组、集合)
	 *
	 * @param obj 对象
	 * @return 是否是二进制类型对象
	 */
	public static boolean isBinaryParam(Object obj) {
		return obj instanceof Binary || TypeUtil.isBinaryArray(obj.getClass()) || TypeUtil.isBinaryCollection(obj);
	}

	/**
	 * 获取二进制对象的字符串表示（数据清空了）
	 *
	 * @param binary 二进制对象
	 * @return 字符串
	 */
	public static String getBinaryString(Binary binary) {
		Binary tmp = CommonUtil.cloneInstance(binary);
		long length = binary.getLength();
		tmp.setData(null);
		tmp.setLength(length);
		return StrObjectConvert.objToStr(tmp, tmp.getClass());
	}

	/**
	 * 获取二进制对象列表的字符串表示（数据清空了）
	 *
	 * @param binaries 二进制对象列表
	 * @return 字符串
	 */
	public static String getBinariesString(List<Binary> binaries) {
		List<Binary> temps = new ArrayList<>();
		for (Binary binary : binaries) {
			Binary tmp = CommonUtil.cloneInstance(binary);
			long length = binary.getLength();
			tmp.setData(null);
			tmp.setLength(length);
			temps.add(tmp);
		}
		return StrObjectConvert.objToStr(temps, temps.getClass());
	}

	/**
	 * 构建二进制对象的字节数据
	 *
	 * @param binary   二进制对象
	 * @param paramStr 参数的字符串表示
	 * @return 字节数据
	 */
	public static byte[] buildSingleBinaryBytes(Binary binary, String paramStr) {
		long binaryLength = binary.getLength();
		byte[] binaryDataBytes = binary.getData();
		byte[] paramBytes = paramStr.getBytes(StandardCharsets.UTF_8);
		byte[] paramLengthBytes = ByteUtil.intToBytes(paramBytes.length);
		byte[] binaryCountBytes = new byte[]{1};
		byte[] binaryLengthBytes = ByteUtil.longToBytes(binaryLength);
		return ArrayUtil.addAll(paramLengthBytes, paramBytes, binaryCountBytes, binaryLengthBytes, binaryDataBytes);
	}

	/**
	 * 构建多个二进制对象的字节数据
	 *
	 * @param binaries     多个二进制对象
	 * @param paramJsonStr 参数的字符串表示
	 * @return 字节数据
	 */
	public static byte[] buildMultiBinaryBytes(List<Binary> binaries, String paramJsonStr) {
		List<byte[]> binaryLengthBytesList = new ArrayList<>();
		List<byte[]> binaryDataBytesList = new ArrayList<>();
		for (Binary binary : binaries) {
			byte[] binaryLengthBytes = ByteUtil.longToBytes(binary.getLength());
			byte[] binaryDataBytes = binary.getData();
			binaryLengthBytesList.add(binaryLengthBytes);
			binaryDataBytesList.add(binaryDataBytes);
		}
		byte[] paramBytes = paramJsonStr.getBytes(StandardCharsets.UTF_8);
		byte[] paramLengthBytes = ByteUtil.intToBytes(paramBytes.length);
		int binaryCount = binaries.size();
		byte[] binaryCountBytes = new byte[]{(byte) binaryCount};
		byte[] bodyBytes = ArrayUtil.addAll(paramLengthBytes, paramBytes, binaryCountBytes);
		for (int i = 0; i < binaryCount; i++) {
			bodyBytes = ArrayUtil.addAll(bodyBytes, binaryLengthBytesList.get(i), binaryDataBytesList.get(i));
		}
		return bodyBytes;
	}

	/**
	 * 获取参数长度
	 *
	 * @param bodyBytes 请求体（响应体）中的字节数据
	 * @return 参数长度
	 */
	public static int getParamLength(byte[] bodyBytes) {
		int paramLength = ByteUtil.bytesToInt(ArrayUtil.sub(bodyBytes, 0, 4));
		return paramLength;
	}

	/**
	 * 获取参数字符串
	 *
	 * @param bodyBytes   请求体（响应体）中的字节数据
	 * @param paramLength 参数长度
	 * @return 参数字符串
	 */
	public static String getParamStr(byte[] bodyBytes, int paramLength) {
		byte[] paramBytes = ArrayUtil.sub(bodyBytes, 4, 4 + paramLength);
		String paramStr = new String(paramBytes, StandardCharsets.UTF_8);
		return paramStr;
	}

	/**
	 * 获取二进制文件长度信息(BinarySize)的起始位置
	 *
	 * @param paramLength 参数长度
	 * @return 文件长度信息(BinarySize)的起始位置
	 */
	public static long getBinaryLengthStartIndex(int paramLength) {
		return 4 + paramLength + 1;
	}

	/**
	 * 获取二进制对象中的文件数据
	 *
	 * @param bodyBytes              请求体（响应体）中的字节数据
	 * @param binaryLengthStartIndex 二进制文件长度信息(BinarySize)的起始位置
	 * @return 文件数据
	 */
	public static byte[] getBinaryDataBytes(byte[] bodyBytes, long binaryLengthStartIndex) {
		//受限于hutool和底层数组拷贝工具类的位置索引为int类型，文件传输不能大于2,147,483,648字节（约2GB）
		long binaryLength = ByteUtil.bytesToLong(ArrayUtil.sub(bodyBytes, (int) binaryLengthStartIndex, (int) (binaryLengthStartIndex + 8)));
		long binaryDataStartIndex = binaryLengthStartIndex + 8;
		byte[] binaryDataBytes = ArrayUtil.sub(bodyBytes, (int) binaryDataStartIndex, (int) (binaryDataStartIndex + binaryLength));
		return binaryDataBytes;
	}

	/**
	 * 获取下一个二进制对象数据的长度信息(BinarySize)起始位置
	 *
	 * @param binaryLengthStartIndex 二进制数据长度信息(BinarySize)的起始位置
	 * @param binaryDataBytes        上一个二进制对象的数据
	 * @return 下一个二进制对象数据的长度信息(BinarySize)起始位置
	 */
	public static long getNextBinaryLengthStartIndex(long binaryLengthStartIndex, byte[] binaryDataBytes) {
		return binaryLengthStartIndex + 8 + binaryDataBytes.length;
	}


}
