package com.taotao.cloud.maven.plugin.aa;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.stream.Stream;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;


package com.xx.plugin.es.enc.character;

import org.apache.lucene.analysis.charfilter.BaseCharFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 参考：
 *
 * @see PatternReplaceCharFilter
 */
public class EncCharFilter extends BaseCharFilter {

	private Reader transformedInput;

	public EncCharFilter(Reader in, EncNormalizer encNormalizer) {
		super(in);
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {

// 逻辑具体实现，源码看起来有点绕，还没有看明白逻辑；待后续实现 这里只暂时简单打印

		System.out.println("");
		System.out.println("--------------------------------------------------");
		// Buffer all input on the first call.
		System.out.printf("transformedInput == null:%s%n", transformedInput == null);
		if (transformedInput == null) {
			fill();
		}

		String str = new String(cbuf);
		System.out.printf("cbuf>>>:%s length:%s off>>>:%s len>>>:%s", str, str.length(), off, len);
		int read = transformedInput.read(cbuf, off, len);
		System.out.println(" read>>>" + read);
		return read;
	}

	private String fill() throws IOException {
		StringBuilder buffered = new StringBuilder();
		char[] temp = new char[1024];
		for (int cnt = input.read(temp); cnt > 0; cnt = input.read(temp)) {
			buffered.append(temp, 0, cnt);
		}
		String newStr = this.processPattern(buffered).toString();//+ (buffered.length() > 0 ? "" : tail);
		if (Objects.equals(newStr, "110")) {
			Stream.of(Thread.currentThread().getStackTrace()).forEach(System.out::println);
		}
		transformedInput = new StringReader(newStr);
		return newStr;
	}

	@Override
	public int read() throws IOException {
		if (transformedInput == null) {
			fill();
		}
		return transformedInput.read();
	}

	@Override
	protected int correct(int currentOff) {
		return Math.max(0, super.correct(currentOff));
	}

	/**
	 * Replace pattern in input and mark correction offsets.
	 */
	CharSequence processPattern(CharSequence input) {
		return input;
	}
}

