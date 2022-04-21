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

package com.taotao.cloud.common.utils.common;

import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * 文件后缀过滤器
 *
  * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class SuffixFileFilter implements FileFilter, Serializable {

	private static final long serialVersionUID = -3389157631240246157L;

	private final String[] suffixes;

	public SuffixFileFilter(final String suffix) {
		Assert.notNull(suffix, "The suffix must not be null");
		this.suffixes = new String[]{suffix};
	}

	public SuffixFileFilter(final String[] suffixes) {
		Assert.notNull(suffixes, "The suffix must not be null");
		this.suffixes = new String[suffixes.length];
		System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
	}

	@Override
	public boolean accept(File pathname) {
		final String name = pathname.getName();
		for (final String suffix : this.suffixes) {
			if (checkEndsWith(name, suffix)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkEndsWith(final String str, final String end) {
		final int endLen = end.length();
		return str.regionMatches(true, str.length() - endLen, end, 0, endLen);
	}
}
