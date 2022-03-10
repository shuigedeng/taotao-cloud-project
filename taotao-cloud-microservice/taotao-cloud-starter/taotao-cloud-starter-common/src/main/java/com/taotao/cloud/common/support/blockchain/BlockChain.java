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
package com.taotao.cloud.common.support.blockchain;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/**
 * BlockChain
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:40:25
 */
public class BlockChain {

	private BlockChain() {
	}

	/**
	 * BLOCK_CHAIN
	 */
	private static final List<Block> BLOCK_CHAIN = new ArrayList<>();

	/**
	 * DIFFICULTY
	 */
	private static final int DIFFICULTY = 5;

	/**
	 * 开采块链
	 *
	 * @param data data
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 19:40:42
	 */
	public static String minedBlockChain(String data) {
		String hash;
		if (BLOCK_CHAIN.isEmpty()) {
			hash = addBlock(new Block(data, "0"));
		} else {
			hash = addBlock(new Block(data, BLOCK_CHAIN.get(BLOCK_CHAIN.size() - 1).hash));
		}
		return hash;
	}

	/**
	 * 解析块链
	 *
	 * @param blockHash blockHash
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 19:40:46
	 */
	public static String decryptBlockchain(String blockHash) {
		if ("ALL".equalsIgnoreCase(blockHash)) {
			return JsonUtil.toJSONString(BLOCK_CHAIN);
		} else {
			List<Block> blockList = BLOCK_CHAIN
				.parallelStream()
				.filter(b -> b.hash.equals(blockHash))
				.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(blockList)) {
				return JsonUtil.toJSONString(blockList);
			} else {
				return null;
			}
		}
	}

	/**
	 * 检查区块链的完整性
	 *
	 * @return {@link java.lang.Boolean }
	 * @author shuigedeng
	 * @since 2021-09-02 19:40:54
	 */
	public static Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[DIFFICULTY]).replace('\0', '0');

		// 循环通过区块链来检查散列
		for (int i = 1; i < BLOCK_CHAIN.size(); i++) {
			currentBlock = BLOCK_CHAIN.get(i);
			previousBlock = BLOCK_CHAIN.get(i - 1);
			// 比较注册Hash散列和计算哈希
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				LogUtil.warn("当前的Hash散列不相等");
				return false;
			}
			// 比较以前的Hash散列和注册的以前的Hash散列
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				LogUtil.warn("以前的Hash散列不相等");
				return false;
			}
			// 检查哈希是否已开采
			if (!currentBlock.hash.substring(0, DIFFICULTY).equals(hashTarget)) {
				LogUtil.warn("当前块链还没有被开采");
				return false;
			}

		}
		return true;
	}

	private static String addBlock(Block block) {
		String hash = block.mineBlock(DIFFICULTY);
		BLOCK_CHAIN.add(block);
		return hash;
	}
}
