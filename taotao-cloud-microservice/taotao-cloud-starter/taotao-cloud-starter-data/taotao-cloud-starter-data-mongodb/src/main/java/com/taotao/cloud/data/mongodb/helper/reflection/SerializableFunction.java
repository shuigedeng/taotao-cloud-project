package com.taotao.cloud.data.mongodb.helper.reflection;

import java.io.Serializable;
import java.util.function.Function;

/**
 * SerializableFunction
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-10 22:35:53
 */
@FunctionalInterface
public interface SerializableFunction<E, R> extends Function<E, R>, Serializable {
  
}
