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
package com.taotao.cloud.common.annotation.user;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求的方法参数SysUser上添加该注解，则注入当前登录人信息
 * <p>
 * 例1：public void test(@LoginUser SysUser user) // 取BaseContextHandler中的 用户id、账号、姓名、组织id、岗位id等信息
 * <p>
 * 例2：public void test(@LoginUser(isRoles = true) SysUser user) //能获取SysUser对象的实时的用户信息和角色信息
 * <p>
 * 例3：public void test(@LoginUser(isOrg = true) SysUser user) //能获取SysUser对象的实时的用户信息和组织信息
 * <p>
 * 例4：public void test(@LoginUser(isStation = true) SysUser user) //能获取SysUser对象的实时的用户信息和岗位信息
 * <p>
 * 例5：public void test(@LoginUser(isFull = true) SysUser user) //能获取SysUser对象的所有信息
 * <p>
 * 例6：public void test(@LoginUser(isResource = true) SysUser user) //能获取SysUser对象的实时的用户信息和资源信息
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/6/22 17:05
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {

	/**
	 * 是否查询SysUser对象所有信息，true则通过rpc接口查询
	 */
	boolean isFull() default false;

	/**
	 * 是否只查询角色信息，true则通过rpc接口查询
	 */
	boolean isRoles() default false;

	/**
	 * 是否只查询 资源 信息，true则通过rpc接口查询
	 */
	boolean isResource() default false;

	/**
	 * 是否只查询组织信息，true则通过rpc接口查询
	 */
	boolean isOrg() default false;

	/**
	 * 是否只查询岗位信息，true则通过rpc接口查询
	 */
	boolean isStation() default false;
}
