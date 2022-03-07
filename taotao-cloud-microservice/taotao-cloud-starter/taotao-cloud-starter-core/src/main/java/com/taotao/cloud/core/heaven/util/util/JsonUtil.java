package com.taotao.cloud.core.heaven.util.util;


import com.taotao.cloud.core.heaven.constant.PunctuationConst;
import com.taotao.cloud.core.heaven.util.guava.Guavas;
import com.taotao.cloud.core.heaven.util.io.FileUtil;
import com.taotao.cloud.core.heaven.util.lang.ObjectUtil;
import com.taotao.cloud.core.heaven.util.lang.StringUtil;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * JSON 工具类
 * <p> project: heaven-JsonUtil </p>
 * <p> create on 2020/2/29 13:27 </p>
 *
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    /**
     * 获取索引列表
     *
     * @param compressJsonPath 压缩的 json 路径
     * @param size 大下
     * @return 结果列表
     * @since 0.1.86
     */
    public static List<String> getIndexList(final String compressJsonPath, final int size) {
        final String json = FileUtil.getFileContent(compressJsonPath);
        if (StringUtil.isEmptyTrim(json) || size <= 0) {
            return Collections.emptyList();
        }

        List<Integer> prefixList = CollectionUtil.fill(size);
        return getIndexList(compressJsonPath, prefixList);
    }

    /**
     * 获取索引列表
     *
     * @param compressJsonPath 压缩的 json 路径
     * @param indexPrefixList  索引前缀列表
     * @return 结果列表
     * @since 0.1.85
     */
    public static List<String> getIndexList(final String compressJsonPath, final List<?> indexPrefixList) {
        final String json = FileUtil.getFileContent(compressJsonPath);
        if (StringUtil.isEmptyTrim(json) || CollectionUtil.isEmpty(indexPrefixList)) {
            return Collections.emptyList();
        }

        List<String> results = Guavas.newArrayList(indexPrefixList.size());
        Stack<Integer> stack = new Stack<>();
        List<String> indexList = Guavas.newArrayList(indexPrefixList.size());

        for (int i = 0; i < json.length(); i++) {
            final char ch = json.charAt(i);

            if ('{' == ch) {
                stack.push(i);
            }
            if ('}' == ch) {
                Integer startIndex = stack.pop();
                int endIndex = i + 1;

                final int byteStartIndex = json.substring(0, startIndex).getBytes().length;
                final int byteEndIndex = byteStartIndex + json.substring(startIndex, endIndex)
                        .getBytes().length;

                String result = byteStartIndex + PunctuationConst.COMMA + byteEndIndex;
                indexList.add(result);
            }
        }

        for (int i = 0; i < indexPrefixList.size(); i++) {
            final String prefix = getPrefix(indexPrefixList.get(i));
            String result = prefix + indexList.get(i);
            results.add(result);
        }

        return results;
    }

    /**
     * 获取前缀
     * @param object 对象
     * @return 结果
     * @since 0.0.1
     */
    private static String getPrefix(Object object) {
        if(ObjectUtil.isNull(object)) {
            return StringUtil.EMPTY;
        }
        String string = object.toString();
        if(StringUtil.isEmptyTrim(string)) {
            return StringUtil.EMPTY;
        }

        return string+StringUtil.BLANK;
    }

}
