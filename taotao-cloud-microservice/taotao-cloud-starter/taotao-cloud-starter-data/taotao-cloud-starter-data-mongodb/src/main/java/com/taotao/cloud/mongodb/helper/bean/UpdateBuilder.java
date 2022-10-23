package com.taotao.cloud.mongodb.helper.bean;

import com.taotao.cloud.mongodb.helper.reflection.ReflectionUtil;
import com.taotao.cloud.mongodb.helper.reflection.SerializableFunction;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.query.Update;

/**
 * UpdateBuilder
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-10 22:35:53
 */
public class UpdateBuilder {
	Update update = new Update();

	public UpdateBuilder() {

	}

//	public UpdateBuilder(String key, Object value) {
//		update.set(key, value);
//	}

	public <E, R> UpdateBuilder(SerializableFunction<E, R> key, Object value) {
		update.set(ReflectionUtil.getFieldName(key), value);
	}

//	public UpdateBuilder set(String key, Object value) {
//		update.set(key, value);
//		return this;
//	}

	public <E, R> UpdateBuilder set(SerializableFunction<E, R> key, Object value) {
		update.set(ReflectionUtil.getFieldName(key), value);
		return this;
	}
	

	public <E, R> UpdateBuilder inc(SerializableFunction<E, R> key, Number count) {
		update.inc(ReflectionUtil.getFieldName(key), count);
		return this;
	}

	public Update toUpdate() {
		return update;
	}

}
