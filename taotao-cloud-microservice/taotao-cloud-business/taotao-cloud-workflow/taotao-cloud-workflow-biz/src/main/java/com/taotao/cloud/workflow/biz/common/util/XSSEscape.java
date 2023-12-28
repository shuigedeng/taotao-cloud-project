/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.workflow.biz.common.util;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;

/** 防止XSS注入 */
@Slf4j
public class XSSEscape {

    /** 非法路径符号 */
    private static final Pattern PATH_PATTERN =
            Pattern.compile("\\.\\.\\|\\.\\./|~/|~\\|[<]|>|\"|[*]|[|]|[?]", Pattern.CASE_INSENSITIVE);

    private static InputStream inputStream;
    private static Policy policy;
    private static Policy emptyPolicy;

    static {
        try {
            inputStream = XSSEscape.class.getClassLoader().getResourceAsStream("antisamy-ebay.xml");
            policy = Policy.getInstance(inputStream);
            inputStream.close();
            inputStream = XSSEscape.class.getClassLoader().getResourceAsStream("antisamy-empty.xml");
            emptyPolicy = Policy.getInstance(inputStream);
            inputStream.close();
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }

    /**
     * 跨站式脚本攻击字符串过滤
     *
     * @param character 需要转义的字符串
     */
    public static String escape(String character) {
        try {
            AntiSamy antiSamy = new AntiSamy();
            CleanResults scan = antiSamy.scan(character, policy);
            String str = scan.getCleanHTML();
            str = str.replaceAll("&quot;", "\"");
            str = str.replaceAll("&amp;", "&");
            str = str.replaceAll("&lt;", "<");
            str = str.replaceAll("&gt;", ">");
            return str;
        } catch (Exception e) {
            log.error("转换错误：" + e.getMessage());
        }
        return null;
    }

    /**
     * 此方法伪过滤
     *
     * @param character 需要转义的字符串
     */
    public static <T> T escapeObj(T character) {
        try {
            return (T) JsonUtil.getJsonToBean(escapeEmpty(character.toString()), character.getClass());
        } catch (Exception e) {
        }
        return character;
    }

    /**
     * 此方法伪过滤
     *
     * @param character 需要转义的字符串
     */
    public static String escapeEmpty(String character) {
        try {
            AntiSamy antiSamy = new AntiSamy();
            CleanResults scan = antiSamy.scan(character, emptyPolicy);
            return scan.getCleanHTML();
        } catch (Exception e) {
        }
        return character;
    }

    /**
     * 过滤非法路径
     *
     * @param path
     * @return
     */
    public static String escapePath(String path) {
        Matcher matcher = PATH_PATTERN.matcher(path);
        return escapeEmpty(matcher.replaceAll("").replaceAll("\\.\\.", "."));
    }
}
