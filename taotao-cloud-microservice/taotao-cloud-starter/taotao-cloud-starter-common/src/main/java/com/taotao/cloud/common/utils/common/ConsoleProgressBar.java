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

import java.text.DecimalFormat;

/**
 * 控制台进度条
 */
public class ConsoleProgressBar {

	/**
	 * 进度条起始值
	 */
	private long minimum = 0;

	/**
	 * 进度条最大值
	 */
	private long maximum = 100;

	/**
	 * 进度条长度
	 */
	private long barLen = 100;

	/**
	 * 用于进度条显示的字符
	 */
	private char showChar = '=';

	/**
	 * 格式
	 */
	private DecimalFormat formater = new DecimalFormat("#.##%");

	/**
	 * 使用系统标准输出，显示字符进度条及其百分比。
	 */
	public ConsoleProgressBar() {
	}

	/**
	 * 使用系统标准输出，显示字符进度条及其百分比。
	 *
	 * @param minimum 进度条起始值
	 * @param maximum 进度条最大值
	 * @param barLen  进度条长度
	 */
	public ConsoleProgressBar(long minimum, long maximum,
		long barLen) {
		this(minimum, maximum, barLen, '=');
	}

	/**
	 * 使用系统标准输出，显示字符进度条及其百分比。
	 *
	 * @param minimum  进度条起始值
	 * @param maximum  进度条最大值
	 * @param barLen   进度条长度
	 * @param showChar 用于进度条显示的字符
	 */
	public ConsoleProgressBar(long minimum, long maximum,
		long barLen, char showChar) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.barLen = barLen;
		this.showChar = showChar;
	}

	/**
	 * 显示进度条。
	 *
	 * @param value 当前进度,进度必须大于或等于起始点且小于等于结束点
	 */
	public void show(long value) {
		if (value < minimum || value > maximum) {
			return;
		}

		reset();
		minimum = value;
		float rate = (float) (minimum * 1.0 / maximum);
		long len = (long) (rate * barLen);
		draw(len, rate);
		if (minimum == maximum) {
			afterComplete();
		}
	}

	/**
	 * 画
	 *
	 * @param len  只
	 * @param rate 率
	 */
	private void draw(long len, float rate) {
		for (int i = 0; i < len; i++) {
			System.out.print(showChar);
		}

		System.out.print(' ');
		System.out.print(format(rate));
	}

	/**
	 * 重启
	 */
	private void reset() {
		System.out.print('\r');
	}

	/**
	 * 完成后
	 */
	private void afterComplete() {
		System.out.print('\n');
	}

	/**
	 * 格式
	 *
	 * @param num 数
	 * @return java.lang.String
	 */
	private String format(float num) {
		return formater.format(num);
	}

}
