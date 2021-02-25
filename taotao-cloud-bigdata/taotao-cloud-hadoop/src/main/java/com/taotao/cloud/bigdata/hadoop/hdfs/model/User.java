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
package com.taotao.cloud.bigdata.hadoop.hdfs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * User
 *
 * @author dengtao
 * @date 2020/10/29 15:28
 * @since v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Writable {
	private String username;
	private Integer age;
	private String address;

	@Override
	public void write(DataOutput output) throws IOException {
		// 把对象序列化
		output.writeChars(username);
		output.writeInt(age);
		output.writeChars(address);
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		// 把序列化的对象读取到内存中
		username = input.readUTF();
		age = input.readInt();
		address = input.readUTF();
	}
}
