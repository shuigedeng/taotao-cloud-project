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
package com.taotao.cloud.common.blockchain;

import com.taotao.cloud.common.utils.DateUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.secure.SHAUtil;

/**
 * Block
 *
 * @version 1.0.0
 * @author shuigedeng
 * @since 2021/8/27 20:55
 */
public class Block {

	public String hash;
	public String previousHash;

	/**
	 * 区块链数据，基本的数据
	 */
	private String data;
	private long timestamp;
	private int nonce;

	/**
	 * 区块链构造方法
	 */
	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timestamp = DateUtil.getTimestamp();
		// 确保hash值的来源
		this.hash = calculateHash();
	}

	/**
	 * 使用 sha256 算法让一个输入转变成256位的hash值
	 */
	public String calculateHash() {
		return SHAUtil.encrypt256(previousHash +
			timestamp +
			nonce +
			data);
	}

	/**
	 * Increases nonce value until hash target is reached
	 */
	public String mineBlock(int difficulty) {
		// Create a string with difficulty * "0"
		String target = getDificultyString(difficulty);
		while (!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
		LogUtil.info("Block Mined: " + hash);
		return hash;
	}

	/**
	 * Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
	 */
	private static String getDificultyString(int difficulty) {
		return new String(new char[difficulty]).replace('\0', '0');
	}

	public String getData() {
		return data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public int getNonce() {
		return nonce;
	}
}
