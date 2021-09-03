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
package com.taotao.cloud.common.tuple;

import java.io.Serializable;

/**
 * Tuple3 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:27:52
 */
public class Tuple3<T1, T2, T3> implements Serializable {

	final T1 _1;
	final T2 _2;
	final T3 _3;


	public Tuple3(T1 _1, T2 _2, T3 _3) {
		this._1 = _1;
		this._2 = _2;
		this._3 = _3;
	}

	public T1 _1() {
		return _1;
	}

	public T2 _2() {
		return _2;
	}

	public T3 _3() {
		return _3;
	}
}
