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
package com.taotao.cloud.bigdata.storm;

import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * CustomStorm
 *
 * @author dengtao
 * @version v1.0.0
 * @date 2019-07-01 14:03
 */
public class CustomStorm {

	private final Random random = new Random();

	private BlockingQueue<String> sentenceQueue = new ArrayBlockingQueue(50000);

	private BlockingQueue<String> wordQueue = new ArrayBlockingQueue(50000);

	private final Map<String, Integer> counters = new HashMap<>();

	/**
	 * 用来发送句子
	 *
	 * @return void
	 * @throws
	 * @author dengtao
	 * @link
	 * @version v1.0.0
	 * @since 2019-07-01 14:06
	 */
	public void nextTuple() {
		String[] sentences = new String[]{
			"the cow jumped over the moon",
			"an apple a day keeps the doctor away",
			"four score and seven years ago",
			"snow white and the seven dwarfs",
			"i am at two with nature"};

		String sentence = sentences[random.nextInt(sentences.length)];
		try {
			sentenceQueue.put(sentence);
			System.out.println("send sentence:" + sentence);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用来切割句子
	 *
	 * @param sentence 句子
	 * @author dengtao
	 * @since 2019-07-01 14:06
	 */
	public void split(String sentence) {
		System.out.println("resv sentence" + sentence);

		String[] words = sentence.split(" ");
		for (String word : words) {
			word = word.trim();
			if (!word.isEmpty()) {
				word = word.toLowerCase();
				//collector.emit()
				wordQueue.add(word);
				System.out.println("split word:" + word);
			}
		}
	}

	/**
	 * 用来计算单词
	 *
	 * @param word word
	 * @return void
	 * @throws
	 * @author dengtao
	 * @link
	 * @version v1.0.0
	 * @since 2019-07-01 14:07
	 */
	public void wordcounter(String word) {
		if (!counters.containsKey(word)) {
			counters.put(word, 1);
		} else {
			Integer c = counters.get(word) + 1;
			counters.put(word, c);
		}
		System.out.println("print map:" + counters);
	}


	public static void main(String[] args) {
		//线程池
		ExecutorService executorService = newFixedThreadPool(10);

		CustomStorm myStorm = new CustomStorm();

		//发射句子到sentenceQuequ
		executorService.submit(new MySpout(myStorm));

		//接受一个句子，并将句子切割
		executorService.submit(new MyBoltSplit(myStorm));

		//接受一个单词，并进行据算
		executorService.submit(new MyBoltWordCount(myStorm));
	}

	public BlockingQueue getSentenceQueue() {
		return sentenceQueue;
	}

	public void setSentenceQueue(BlockingQueue sentenceQueue) {
		this.sentenceQueue = sentenceQueue;
	}

	public BlockingQueue getWordQueue() {
		return wordQueue;
	}

	public void setWordQueue(BlockingQueue wordQueue) {
		this.wordQueue = wordQueue;
	}
}

class MySpout extends Thread {

	private CustomStorm myStorm;

	public MySpout(CustomStorm myStorm) {
		this.myStorm = myStorm;
	}

	@Override
	public void run() {
		//storm框架在循环调用spout的netxTuple方法
		while (true) {
			myStorm.nextTuple();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class MyBoltWordCount extends Thread {

	private CustomStorm myStorm;

	public MyBoltWordCount(CustomStorm myStorm) {
		this.myStorm = myStorm;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String word = (String) myStorm.getWordQueue().take();
				myStorm.wordcounter(word);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}


}

class MyBoltSplit extends Thread {

	private CustomStorm myStorm;

	public MyBoltSplit(CustomStorm myStorm) {
		this.myStorm = myStorm;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String sentence = (String) myStorm.getSentenceQueue().take();
				myStorm.split(sentence);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

//简单的自定义属于自己的一个storm框架！
//主要是利用了线程池和blockingQueue队列实现！
}
