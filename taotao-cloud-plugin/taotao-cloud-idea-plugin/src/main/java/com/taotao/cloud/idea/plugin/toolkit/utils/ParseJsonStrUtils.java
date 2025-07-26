package com.taotao.cloud.idea.plugin.toolkit.utils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Java 解析不带引号的JSON字符串
 *
 * @author Heal Chow
 * @since 2019/08/13 11:36
 */
public class ParseJsonStrUtils {
    /**
     * 解析 Json 格式的字符串, 封装为 List 或 Map 并返回
     * 注意: (1) key 和 value 不能含有 ",", key 中不能含有 ":" —— 要分别用 "," 和 ":" 进行分隔
     *      (2) 要解析的字符串必须符合JSON对象的格式, 只对最外层的多层嵌套做了简单的处理,
     *          复杂的如"{a:b},{x:y}"将不能完全识别 —— 正确的应该是"[{a:b},{x:y}]"
     * @param sourceStr 首尾被"[]"或"{}"包围的字符串
     * @return 生成的JsonObject
     */
    public static Object parseJson(String sourceStr) throws InvalidParameterException {
        if (sourceStr == null || "".equals(sourceStr)) {
            return sourceStr;
        }

        // 判断字符串首尾有没有多余的、相匹配的 "[]" 和 "{}"
        String parsedStr = simplifyStr(sourceStr, "[", "]");
        parsedStr = simplifyStr(parsedStr, "{", "}");

        // 借助栈来实现 "[]" 和 "{}" 的出入
        Stack<String> leftSymbolStack = new Stack<>();
        Stack<String> rightSymbolStack = new Stack<>();

        if ((parsedStr.startsWith("[") && parsedStr.endsWith("]")) || (parsedStr.startsWith("{") && parsedStr.endsWith("}"))) {
            leftSymbolStack.push(parsedStr.substring(0, 1));
            rightSymbolStack.push(parsedStr.substring(parsedStr.length() - 1));
            parsedStr = parsedStr.substring(1, parsedStr.length() - 1);

            // parsedStr 内部还可能是连续的"{{}}"
            parsedStr = simplifyStr(parsedStr, "{", "}");
        } else {
            throw new InvalidParameterException("要解析的字符串中存在不匹配的'[]'或'{}', 请检查!\n原字符串为: " + sourceStr);
        }

        // 保存解析的结果, jsonArray中可能只有String, 也可能含有Map<String, Object>
        List<Object> jsonArray = new ArrayList<>();
        Map<String, Object> jsonMap = new HashMap<>(16);

        // 内部遍历、解析
        innerParseByLoop(parsedStr, leftSymbolStack, rightSymbolStack, jsonArray, jsonMap);

        // 判断jsonArray是否为空
        if (jsonArray.size() > 0) {
            return jsonArray;
        } else {
            return jsonMap;
        }
    }

    /**
     * 循环解析内部的List、Map对象
     */
    private static void innerParseByLoop(String parsedStr, Stack<String> leftSymbolStack, Stack<String> rightSymbolStack,
                                         List<Object> jsonArray, Map<String, Object> jsonMap) throws InvalidParameterException {
        if (parsedStr == null || parsedStr.equals("")) {
            return;
        }
        // 按照","分隔
        String[] allKeyValues = parsedStr.split(",");
        if (allKeyValues.length > 0) {

            // 遍历, 并按照":"分隔解析
            out:
            for (String keyValue : allKeyValues) {

                // 如果keyValue中含有":", 说明该keyValue是List<Map>中的一个对象, 就需要确定第一个":"的位置 —— 可能存在多个":"
                int index = keyValue.indexOf(":");
                if (index > 0) {

                    // 判断key是否仍然以"{"或"["开始, 如果是, 则压栈
                    String key = keyValue.substring(0, index).trim();
                    while (key.startsWith("[") || key.startsWith("{")) {
                        leftSymbolStack.push(key.substring(0, 1));
                        // 解析过的串要一直跟进
                        parsedStr = parsedStr.substring(1);
                        key = key.substring(1);
                    }

                    // 判读和value是否以"["开头 —— 又是一个 List 对象 —— 递归解析
                    String value = keyValue.substring(index + 1).trim();
                    if (value.startsWith("[")) {
                        int innerIndex = parsedStr.indexOf("]");
                        List<Object> innerList = (List<Object>) parseJson(parsedStr.substring(key.length() + 1, innerIndex + 1));
                        jsonMap.put(key, innerList);
                        // 清除最后的"]", 并判断是否存在","
                        parsedStr = parsedStr.substring(innerIndex + 1);
                        if (parsedStr.indexOf(",") == 0) {
                            parsedStr = parsedStr.substring(1);
                        }

                        // 此内部存在对象, 内部的","已经解析完毕了, 要修正按照","切割的字符串数组, 并继续遍历
                        innerParseByLoop(parsedStr, leftSymbolStack, rightSymbolStack, jsonArray, jsonMap);
                        break;
                    }

                    // 判读和value是否以 "{" 开头 —— 又是一个 Map 对象 —— 递归解析
                    else if (value.startsWith("{")) {
                        int innerIndex = parsedStr.indexOf("}");
                        Map<String, Object> innerMap = (Map<String, Object>) parseJson(parsedStr.substring(key.length() + 1, innerIndex + 1));
                        jsonMap.put(key, innerMap);

                        // 清除最后的"}", 并判断是否存在","
                        parsedStr = parsedStr.substring(innerIndex + 1);
                        if (parsedStr.indexOf(",") == 0) {
                            parsedStr = parsedStr.substring(1);
                        }

                        // 此内部存在对象, 内部的","已经解析完毕了, 要修正按照","切割的字符串数组, 并继续遍历
                        innerParseByLoop(parsedStr, leftSymbolStack, rightSymbolStack, jsonArray, jsonMap);
                        break;
                    }

                    // 最后判断value尾部是否含有 "]" 或 "}"
                    else {
                        while (value.endsWith("]") || value.endsWith("}")) {
                            // 最右侧的字符
                            String right = value.substring(value.length() - 1);
                            // 此时栈顶元素
                            String top = leftSymbolStack.peek();
                            // 如果有相匹配的, 则弹栈, 否则忽略
                            if (("}".equals(right) && "{".equals(top)) || ("]".equals(right) && "[".equals(top))) {
                                leftSymbolStack.pop();
                                value = value.substring(0, value.length() - 1);
                                jsonMap.put(key, value);

                                // 清除最后的"}", 并判断是否存在","
                                parsedStr = parsedStr.substring(key.length() + 1 + value.length() + 1);
                                if (parsedStr.indexOf(",") == 0) {
                                    parsedStr = parsedStr.substring(1);
                                }

                                // 解析完成了一个对象, 则将该元素添加到List中, 并创建新的对象
                                jsonArray.add(jsonMap);
                                jsonMap = new HashMap<>(16);

                                // 继续进行外层的解析
                                continue out;
                            }

                            // 如果都不匹配, 则有可能是源字符串的最后一个符号
                            else {
                                rightSymbolStack.push(right);
                                value = value.substring(0, value.length() - 1);
                            }
                        }
                        jsonMap.put(key, value);
                        int length = key.length() + value.length() + 2;
                        if (parsedStr.length() > length) {
                            parsedStr = parsedStr.substring(length);
                        } else {
                            parsedStr = "";
                        }
                    }
                }
                // 如果keyValue中不含":", 说明该keyValue只是List<String>中的一个串, 而非List<Map>中的一个Map, 则直接将其添加到List中即可
                else {
                    jsonArray.add(keyValue);
                }
            }

            // 遍历结束, 处理最后的符号问题 —— 判断左右栈是否匹配
            while (!leftSymbolStack.empty()) {
                if (leftSymbolStack.peek().equals("{") && parsedStr.equals("}")) {
                    leftSymbolStack.pop();
                }
                if (!rightSymbolStack.empty()) {
                    if (leftSymbolStack.peek().equals("{") && rightSymbolStack.peek().equals("}")) {
                        leftSymbolStack.pop();
                        rightSymbolStack.pop();
                    } else if (leftSymbolStack.peek().equals("[") && rightSymbolStack.peek().equals("]")) {
                        leftSymbolStack.pop();
                        rightSymbolStack.pop();
                    } else {
                        throw new InvalidParameterException("传入的字符串中不能被解析成JSON对象!\n原字符串为: " + parsedStr);
                    }
                }
            }
        }
    }

    /**
     * 判断字符串首尾有没有多余的、相匹配的 "[]" 和 "{}", 对其进行简化
     */
    private static String simplifyStr(String sourceStr, String firstSymbol, String lastSymbol) {

        while (sourceStr.startsWith(firstSymbol) && sourceStr.endsWith(lastSymbol)) {
            String second = sourceStr.substring(1, 2);
            // 如果第二个仍然是"["或"{", 再判断倒数第二个是不是"]"或"}" —— 说明长度至少为3, 不会发生 IndexOutOfBoundsException
            if (second.equals(firstSymbol)) {
                String penultimate = sourceStr.substring(sourceStr.length() - 2, sourceStr.length() - 1);
                if (penultimate.equals(lastSymbol)) {
                    // 缩短要解析的串
                    sourceStr = sourceStr.substring(1, sourceStr.length() - 1);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return sourceStr;
    }

}
