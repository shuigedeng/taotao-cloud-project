package com.taotao.cloud.core.sensitive.word.support.check.impl;

import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.common.utils.lang.CharUtil;
import com.taotao.cloud.common.utils.common.RegexUtil;
import com.taotao.cloud.core.sensitive.word.api.IWordContext;
import com.taotao.cloud.core.sensitive.word.constant.enums.ValidModeEnum;
import com.taotao.cloud.core.sensitive.word.support.check.ISensitiveCheck;
import com.taotao.cloud.core.sensitive.word.support.check.SensitiveCheckResult;
import com.taotao.cloud.core.sensitive.word.support.format.CharFormatChain;

/**
 * URL 正则表达式检测实现。
 *
 * 也可以严格的保留下来。
 *
 * （1）暂时先粗略的处理 web-site
 * （2）如果网址的最后为图片类型，则跳过。
 * （3）长度超过 70，直接结束。
 *
 */
public class SensitiveCheckUrl implements ISensitiveCheck {

    /**
     * 最长的网址长度
     * @since 0.0.12
     */
    private static final int MAX_WEB_SITE_LEN = 70;

    @Override
    public SensitiveCheckResult sensitiveCheck(String txt, int beginIndex, ValidModeEnum validModeEnum, IWordContext context) {
        // 记录敏感词的长度
        int lengthCount = 0;
        int actualLength = 0;

        StringBuilder stringBuilder = new StringBuilder();
        // 这里偷懒直接使用 String 拼接，然后结合正则表达式。
        // DFA 本质就可以做正则表达式，这样实现不免性能会差一些。
        // 后期如果有想法，对 DFA 进一步深入学习后，将进行优化。
        for(int i = beginIndex; i < txt.length(); i++) {
            char currentChar = txt.charAt(i);
            char mappingChar = Instances.singleton(CharFormatChain.class)
                    .format(currentChar, context);

            if(CharUtil.isWebSiteChar(mappingChar)
                && lengthCount <= MAX_WEB_SITE_LEN) {
                lengthCount++;
                stringBuilder.append(currentChar);

                if(isCondition(stringBuilder.toString())) {
                    actualLength = lengthCount;

                    // 是否遍历全部匹配的模式
                    if(ValidModeEnum.FAIL_FAST.equals(validModeEnum)) {
                        break;
                    }
                }
            } else {
                break;
            }
        }

        return SensitiveCheckResult.of(actualLength, SensitiveCheckUrl.class);
    }

    /**
     * 这里指定一个阈值条件
     * @param string 长度
     * @return 是否满足条件
     * @since 0.0.12
     */
    private boolean isCondition(final String string) {
        return RegexUtil.isWebSite(string);
    }


}
