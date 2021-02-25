/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.bigdata.trino.udf.str_upper;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import io.trino.spi.function.Description;
import io.trino.spi.function.ScalarFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.StandardTypes;

/**
 * 开发标量函数 my_upper
 *
 * @author dengtao
 * @date 2021/1/25 下午2:59
 * @since v1.0
 */
public class StrUpperScalarFunctions {

	@ScalarFunction("my_upper") // 标量函数名称
	@Description("我的大小写转换函数") //函数注释
	@SqlType(StandardTypes.VARBINARY) //函数数据类型
	public static Slice myUpper(@SqlType(StandardTypes.VARBINARY) Slice input) {
		String upper = input.toStringUtf8().toUpperCase();
		return Slices.utf8Slice(upper);
	}
}
