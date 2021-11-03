/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.common.spel;


import java.lang.reflect.Method;

/**
 * ExpressionRootObject
 *
  * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class ExpressionRootObject {

	private final Method method;

	private final Object[] args;

	private final Object target;

	private final Class<?> targetClass;

	private final Method targetMethod;

	public ExpressionRootObject(Method method, Object[] args, Object target,
		Class<?> targetClass, Method targetMethod) {
		this.method = method;
		this.args = args;
		this.target = target;
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}

	public Object getTarget() {
		return target;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}
}
