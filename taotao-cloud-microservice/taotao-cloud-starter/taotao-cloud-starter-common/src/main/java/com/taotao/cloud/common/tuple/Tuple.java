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
 * 元组
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:02:43
 */
public class Tuple<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> implements Serializable {

	final T1 _1;
	final T2 _2;
	final T3 _3;
	final T4 _4;
	final T5 _5;
	final T6 _6;
	final T7 _7;
	final T8 _8;
	final T9 _9;
	final T10 _10;

	public Tuple(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8, T9 _9, T10 _10) {
		this._1 = _1;
		this._2 = _2;
		this._3 = _3;
		this._4 = _4;
		this._5 = _5;
		this._6 = _6;
		this._7 = _7;
		this._8 = _8;
		this._9 = _9;
		this._10 = _10;
	}

	/**
	 * Tuple2 元组
	 */
	public static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
		return new Tuple2<>(_1, _2);
	}

	/**
	 * Tuple3 元组
	 */
	public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
		return new Tuple3<>(_1, _2, _3);
	}

	/**
	 * Tuple4 元组
	 */
	public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
		return new Tuple4<>(_1, _2, _3, _4);
	}

	/**
	 * Tuple5 元组
	 */
	public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 _1, T2 _2, T3 _3, T4 _4,
		T5 _5) {
		return new Tuple5<>(_1, _2, _3, _4, _5);
	}

	/**
	 * Tuple6 元组
	 */
	public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 _1, T2 _2, T3 _3,
		T4 _4, T5 _5, T6 _6) {
		return new Tuple6<>(_1, _2, _3, _4, _5, _6);
	}

	/**
	 * Tuple7 元组
	 */
	public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(T1 _1, T2 _2,
		T3 _3, T4 _4, T5 _5, T6 _6,
		T7 _7) {
		return new Tuple7<>(_1, _2, _3, _4, _5, _6, _7);
	}

	/**
	 * Tuple8 元组
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(T1 _1,
		T2 _2, T3 _3, T4 _4, T5 _5,
		T6 _6, T7 _7, T8 _8) {
		return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
	}

	/**
	 * Tuple9 元组
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> of(
		T1 _1, T2 _2, T3 _3, T4 _4, T5 _5,
		T6 _6, T7 _7, T8 _8, T9 _9) {
		return new Tuple9<>(_1, _2, _3, _4, _5, _6, _7, _8, _9);
	}

	/**
	 * Tuple10 元组
	 */
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> of(
		T1 _1, T2 _2, T3 _3, T4 _4,
		T5 _5, T6 _6, T7 _7, T8 _8, T9 _9, T10 _10) {
		return new Tuple10<>(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10);
	}
}
