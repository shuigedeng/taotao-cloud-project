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
package com.taotao.cloud.common.base;

/**
 * 通用回调定义
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/6/22 17:06
 **/
public class Callable {

	public interface Action0 {

		void invoke();
	}

	public interface Action1<T1> {

		void invoke(T1 t1);
	}

	public interface Action2<T1, T2> {

		void invoke(T1 t1, T2 t2);
	}

	public interface Action3<T1, T2, T3> {

		void invoke(T1 t1, T2 t2, T3 t3);
	}

	public interface Func0<T0> {

		T0 invoke();
	}

	public interface Func1<T0, T1> {

		T0 invoke(T1 t1);
	}

	public interface Func2<T0, T1, T2> {

		T0 invoke(T1 t1, T2 t2);
	}
}
