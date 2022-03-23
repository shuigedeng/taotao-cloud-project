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
package com.taotao.cloud.common.model;

import java.lang.reflect.InvocationTargetException;

/**
 * Callable
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:28:55
 */
public class Callable {

	/**
	 * Action0
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:29:03
	 */
	public interface Action0 {

		/**
		 * invoke
		 *
		 * @since 2021-09-02 20:29:49
		 */
		void invoke();
	}

	/**
	 * Action1
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:29:09
	 */
	public interface Action1<T1> {

		/**
		 * invoke
		 *
		 * @param t1 t1
		 * @since 2021-09-02 20:29:54
		 */
		void invoke(T1 t1);
	}

	/**
	 * Action2
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:29:14
	 */
	public interface Action2<T1, T2> {

		/**
		 * invoke
		 *
		 * @param t1 t1
		 * @param t2 t2
		 * @since 2021-09-02 20:30:03
		 */
		void invoke(T1 t1, T2 t2);
	}

	/**
	 * Action3
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:29:18
	 */
	public interface Action3<T1, T2, T3> {

		/**
		 * invoke
		 *
		 * @param t1 t1
		 * @param t2 t2
		 * @param t3 t3
		 * @since 2021-09-02 20:30:06
		 */
		void invoke(T1 t1, T2 t2, T3 t3);
	}

	/**
	 * Func0
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:29:24
	 */
	public interface Func0<T0> {

		/**
		 * invoke
		 *
		 * @return T0
		 * @since 2021-09-02 20:30:09
		 */
		T0 invoke();
	}

	/**
	 * Func1
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:29:28
	 */
	public interface Func1<T0, T1> {

		/**
		 * invoke
		 *
		 * @param t1 t1
		 * @return T0
		 * @since 2021-09-02 20:30:14
		 */
		T0 invoke(T1 t1);
	}

	/**
	 * Func2
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:29:40
	 */
	public interface Func2<T0, T1, T2> {

		/**
		 * invoke
		 *
		 * @param t1 t1
		 * @param t2 t2
		 * @return T0
		 * @since 2021-09-02 20:30:20
		 */
		T0 invoke(T1 t1, T2 t2);
	}
}
