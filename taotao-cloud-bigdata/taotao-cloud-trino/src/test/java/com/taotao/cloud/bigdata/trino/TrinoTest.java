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
package com.taotao.cloud.bigdata.trino;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import org.junit.Test;

/**
 * TrinoTest
 *
 * @author dengtao
 * @date 2021/1/22 下午3:05
 * @since v1.0
 */
public class TrinoTest {
	@Test
	public void testSlice(){
		// 设置一块内存区域 大小为12个字节
		Slice slice = Slices.allocate(15);

		// 表示第0个字节位置设置100 (一个字节为8 bit 所以最大值为256)
		slice.setByte(0, 100);

		// 获取第0个位置的字节数据
		System.out.println(slice.getByte(0));

		// 设置int的话 占用4个字节 如果超出内存限定就会溢出内存

		// 所以设置内存区域大小要根据实际的长度设置
		slice.setInt(1, 1024*1024*1024);

		System.out.println(slice.getInt(1));

		//每一个中文占用3个字节(utf8) 也就是占用了6个字节 但是总共才10个字节 所以就会报错 增大总字节数
		slice.setBytes(5, "张三".getBytes());

		//内存复写
		slice.setBytes(8, Slices.utf8Slice("五"));

		// 从5开始 后面6个
		System.out.println(new String(slice.getBytes(5, 6)));

		slice.setBytes(8, Slices.utf8Slice("六六"), 0,6);

		System.out.println(new String(slice.getBytes(5, 9)));

		// 两个不同的内存地址结合使用 就是相当于从5开始取10个字节 并且赋值给slice1
		Slice slice1 = Slices.allocate(10);
		slice.getBytes(5, slice1);
		System.out.println(slice1.toStringUtf8());

	}
}
