package com.taotao.cloud.health.utils;

import com.taotao.cloud.common.exception.BaseException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.springframework.boot.convert.ApplicationConversionService;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 11:14
 **/
public class ConvertUtils {

	public static <T> T convert(Object value, Class<T> type) {
		if (value == null) {
			return null;
		}
		return (T) ApplicationConversionService.getSharedInstance().convert(value, type);
	}

	public static <T> T tryConvert(Object value, Class<T> type) {
		try {
			return convert(value, type);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T deepClone(T obj) {
		try {
			try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
				try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
					out.writeObject(obj);
					try (ByteArrayInputStream byteIn = new ByteArrayInputStream(
						byteOut.toByteArray())) {
						ObjectInputStream in = new ObjectInputStream(byteIn);
						return (T) in.readObject();
					}
				}
			}
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}
}
