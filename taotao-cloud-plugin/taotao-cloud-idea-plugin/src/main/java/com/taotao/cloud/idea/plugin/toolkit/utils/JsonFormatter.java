package com.taotao.cloud.idea.plugin.toolkit.utils;

/**
 * JsonFormatter
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class JsonFormatter {

    private static final int INDENT_SPACE = 2;

    public static String format( String text ) {
        StringBuilder formattedText = new StringBuilder();
        int indentCount = 0;

        for (char c : text.toCharArray()) {
            if (c == '[' || c == '{') {
                formattedText.append(c);
                formattedText.append('\n');
                indentCount += 1;
                appendIndent(formattedText, indentCount);
            } else if (c == ',') {
                formattedText.append(c);
                formattedText.append('\n');
                appendIndent(formattedText, indentCount);
            } else if (c == ']' || c == '}') {
                formattedText.append('\n');
                indentCount -= 1;
                appendIndent(formattedText, indentCount);
                formattedText.append(c);
            } else {
                formattedText.append(c);
            }
        }

        return formattedText.toString();
    }

    private static void appendIndent( StringBuilder text, int indentCount ) {
        for (int i = 0; i < indentCount * INDENT_SPACE; ++i) {
            text.append(' ');
        }
    }

}
