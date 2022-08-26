package com.taotao.cloud.monitor.kuding.text.markdown;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimpleMarkdownBuilder {

	private StringBuilder stringBuilder = new StringBuilder();

	public static SimpleMarkdownBuilder create() {
		SimpleMarkdownBuilder markdownBuilder = new SimpleMarkdownBuilder();
		return markdownBuilder;
	}

	public SimpleMarkdownBuilder title(String content, int level) {
		if (level > 0 && level < 6) {
			stringBuilder.append("#".repeat(level));
			stringBuilder.append(" ").append(content).append("\n\n");
		}
		return this;
	}

	public SimpleMarkdownBuilder text(String content, boolean lineFeed) {
		stringBuilder.append(content);
		if (lineFeed) {
			stringBuilder.append("\n\n");
		}
		return this;
	}

	public SimpleMarkdownBuilder point(List<?> contentList) {
		if (contentList != null && contentList.size() > 0) {
			contentList.forEach(x -> stringBuilder.append("- ").append(x).append("\n"));
			stringBuilder.append("\n");
		}
		return this;
	}

	public SimpleMarkdownBuilder point(Object... contentList) {
		if (contentList != null && contentList.length > 0) {
			Arrays.stream(contentList).forEach(x -> stringBuilder.append("- ").append(x).append("\n"));
			stringBuilder.append("\n");
		}
		return this;
	}

	public SimpleMarkdownBuilder orderPoint(List<?> list) {
		for (int i = 0; i < list.size(); i++) {
			stringBuilder.append(i + 1).append(". ").append(list.get(i)).append("\n");
		}
		stringBuilder.append("\n");
		return this;
	}

	public SimpleMarkdownBuilder orderPoint(Object... list) {
		for (int i = 0; i < list.length; i++) {
			stringBuilder.append(i + 1).append(". ").append(list[i]).append("\n");
		}
		stringBuilder.append("\n");
		return this;
	}

	public SimpleMarkdownBuilder code(String content, int level) {
		if (level > 0 && level < 4) {
			String str = "`````````".substring(0, level);
			if (level != 3) {
				stringBuilder.append(String.format("%s%s%s", str, content, str));
			} else {
				stringBuilder.append(String.format("%s\n%s\n%s\n", str, content, str));
			}
		}
		return this;
	}

	public SimpleMarkdownBuilder linked(String explain, String url) {
		stringBuilder.append("![").append(explain).append("](").append(url).append(")");
		return this;
	}

	public SimpleMarkdownBuilder keyValue(Map<?, ?> map, String keyName, String valueName, TableAlignment alignment) {
		if (map != null && map.size() > 0) {
			stringBuilder.append("|").append(keyName).append("|").append(valueName).append("|").append("\n");
			String value = alignment.getValue();
			stringBuilder.append(value).append("|").append(value).append("|").append("\n");
			map.forEach((x, y) -> stringBuilder.append("|").append(x).append("|").append(y).append("|").append("\n"));
			stringBuilder.append("\n\n");
		}
		return this;
	}

	public SimpleMarkdownBuilder nextLine() {
		stringBuilder.append("\n");
		return this;
	}

	public String build() {
		return stringBuilder.toString();
	}

	public static String bold(String text) {
		return String.format("**%s**", text);
	}
}
