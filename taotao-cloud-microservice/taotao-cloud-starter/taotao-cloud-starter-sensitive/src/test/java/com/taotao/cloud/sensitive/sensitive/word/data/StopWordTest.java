package com.taotao.cloud.sensitive.sensitive.word.data;

import com.taotao.cloud.common.support.condition.ICondition;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.common.CharsetUtils;
import com.taotao.cloud.common.utils.io.FileUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

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

		List<String> zhLines = CollectionUtils.conditionList(allLines, new ICondition<String>() {
			@Override
			public boolean condition(String s) {
				return CharsetUtils.isAllChinese(s);
			}
		});

		FileUtils.write(targetFile, zhLines);
	}

}
