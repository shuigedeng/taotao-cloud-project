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

package com.taotao.cloud.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * spring AntPath 规则文件过滤
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class AntPathFilter implements FileFilter, Serializable {

	private static final long serialVersionUID = 812598009067554612L;
	private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

	private final String pattern;

	public AntPathFilter(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean accept(File pathname) {
		String filePath = pathname.getAbsolutePath();
		return PATH_MATCHER.match(pattern, filePath);
	}
}
