package com.taotao.cloud.core.sensitive.word.data;

import com.taotao.cloud.common.support.condition.ICondition;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.common.CharsetUtil;
import com.taotao.cloud.common.utils.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * 停止词数据初始化
 */
@Ignore
public class StopWordTest {

	/**
	 * 中文测试
	 *
	 */
	@Test
	@Ignore
	public void zhTest() {
		final String sourceFile = "stopword.txt";
		final String targetFile = "D:\\github\\sensitive-word\\src\\main\\resources\\stopword_zh.txt";

		List<String> allLines = DataUtil.distinctLines(sourceFile);

		List<String> zhLines = CollectionUtil.conditionList(allLines, new ICondition<String>() {
			@Override
			public boolean condition(String s) {
				return CharsetUtil.isAllChinese(s);
			}
		});

		FileUtil.write(targetFile, zhLines);
	}

}
