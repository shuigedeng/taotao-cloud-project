/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.data.mybatis.plus.log;

import cn.hutool.db.sql.SqlFormatter;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.HashSet;
import java.util.Set;
import org.apache.ibatis.logging.Log;

/**
 * CustomStdOutImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/28 10:42
 */
public class CustomStdOutImpl implements Log {

	private static final Set<String> DML = new HashSet<>();

	static {
		DML.add("insert");
		DML.add("update");
		DML.add("delete");
		DML.add("select");
	}

	public CustomStdOutImpl(String clazz) {
		// Do Nothing
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public void error(String s, Throwable e) {
		e.printStackTrace();
		LogUtils.error(e,SqlFormatter.format(s));
	}

	@Override
	public void error(String s) {
		LogUtils.error(SqlFormatter.format(s));
	}

	@Override
	public void debug(String s) {
		boolean b = DML.stream().anyMatch(item -> s.toLowerCase().contains(item));
		LogUtils.debug(b ? SqlFormatter.format(s) : s);
	}

	@Override
	public void trace(String s) {
		LogUtils.info(s);
	}

	@Override
	public void warn(String s) {
		LogUtils.info(s);
	}
}
