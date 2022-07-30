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
package com.taotao.cloud.common.constant;

import cn.hutool.core.lang.Dict;
import com.taotao.cloud.common.utils.common.JsonUtil;
import java.util.List;
import java.util.Map;

/**
 * 系统常量
 * <p>
 * String currentUser = System.getProperty("user.name");
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-22 10:42:53
 */
public final class SystemConst {

	private SystemConst() {
	}

	/**
	 * Java 运行时环境版本
	 */
	public static final String VERSION = "java.version";

	/**
	 * Java 运行时环境供应商
	 */
	public static final String VENDOR = "java.vendor";

	/**
	 * Java 供应商的 URL
	 */
	public static final String VENDOR_URL = "java.vendor.url";

	/**
	 * Java 安装目录
	 */
	public static final String HOME = "java.home";

	/**
	 * Java 虚拟机规范版本
	 */
	public static final String VM_SPECIFICATION_VERSION = "java.vm.specification.version";

	/**
	 * Java 虚拟机规范供应商
	 */
	public static final String VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";

	/**
	 * Java 虚拟机规范名称
	 */
	public static final String VM_SPECIFICATION_NAME = "java.vm.specification.name";

	/**
	 * Java 虚拟机实现版本
	 */
	public static final String VM_VERSION = "java.vm.version";

	/**
	 * Java 虚拟机实现供应商
	 */
	public static final String VM_VENDOR = "java.vm.vendor";

	/**
	 * Java 虚拟机实现名称
	 */
	public static final String VM_NAME = "java.vm.name";

	/**
	 * Java 运行时环境规范版本
	 */
	public static final String SPECIFICATION_VERSION = "java.specification.version";

	/**
	 * Java 运行时环境规范供应商
	 */
	public static final String SPECIFICATION_VENDOR = "java.specification.vendor";

	/**
	 * Java 运行时环境规范名称
	 */
	public static final String SPECIFICATION_NAME = "java.specification.name";

	/**
	 * Java 类格式版本号
	 */
	public static final String CLASS_VERSION = "java.class.version";

	/**
	 * 类路径
	 */
	public static final String CLASS_PATH = "java.class.path";

	/**
	 * 加载库时搜索的路径列表
	 */
	public static final String LIBRARY_PATH = "java.library.path";

	/**
	 * 默认的临时文件路径
	 */
	public static final String IO_TMPDIR = "java.io.tmpdir";

	/**
	 * 要使用的 JIT 编译器的名称
	 */
	public static final String COMPILER = "java.compiler";

	/**
	 * 一个或多个扩展目录的路径
	 */
	public static final String EXT_DIRS = "java.ext.dirs";

	/**
	 * 操作系统的名称
	 */
	public static final String OS_NAME = "os.name";

	/**
	 * 操作系统的架构
	 */
	public static final String OS_ARCH = "os.arch";

	/**
	 * 操作系统的版本
	 */
	public static final String OS_VERSION = "os.version";

	/**
	 * 文件分隔符
	 * UNIX /
	 */
	public static final String FILE_SEPARATOR = "file.separator";

	/**
	 * 路径分隔符
	 * Unix :
	 */
	public static final String PATH_SEPARATOR = "path.separator";

	/**
	 * 行分隔符
	 * Unix /n
	 */
	public static final String LINE_SEPARATOR = "line.separator";

	/**
	 * 用户的账户名称
	 */
	public static final String USER_NAME = "user.name";

	/**
	 * 用户的主目录
	 */
	public static final String USER_HOME = "user.home";

	/**
	 * 户的当前工作目录
	 */
	public static final String USER_DIR = "user.dir";

	public static void main(String[] args) {
		Dict dict = new Dict();

		dict.setVersion(null);
		String s = JsonUtil.toJSONString(dict);
		System.out.println(s);
	}


	public static class Dict{
		private Integer version;
		private Boolean boo;
		private List<Integer> it;
		private Map<String, Integer> st;

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

		public Boolean getBoo() {
			return boo;
		}

		public void setBoo(Boolean boo) {
			this.boo = boo;
		}

		public List<Integer> getIt() {
			return it;
		}

		public void setIt(List<Integer> it) {
			this.it = it;
		}

		public Map<String, Integer> getSt() {
			return st;
		}

		public void setSt(Map<String, Integer> st) {
			this.st = st;
		}
	}

}
