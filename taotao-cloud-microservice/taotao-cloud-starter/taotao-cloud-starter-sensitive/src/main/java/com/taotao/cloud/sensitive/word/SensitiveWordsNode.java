package com.taotao.cloud.web.sensitive.word;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * 敏感词节点，每个节点包含了以相同的2个字符开头的所有词
 */
public class SensitiveWordsNode implements Serializable {

	/**
	 * 头两个字符的mix，mix相同，两个字符相同
	 */
	protected final int headTwoCharMix;

	/**
	 * 所有以这两个字符开头的词表
	 */
	protected final TreeSet<StringPointer> words = new TreeSet<>();

	/**
	 * 下一个节点
	 */
	protected SensitiveWordsNode next;

	public SensitiveWordsNode(int headTwoCharMix) {
		this.headTwoCharMix = headTwoCharMix;
	}

	public SensitiveWordsNode(int headTwoCharMix, SensitiveWordsNode parent) {
		this.headTwoCharMix = headTwoCharMix;
		parent.next = this;
	}
}
