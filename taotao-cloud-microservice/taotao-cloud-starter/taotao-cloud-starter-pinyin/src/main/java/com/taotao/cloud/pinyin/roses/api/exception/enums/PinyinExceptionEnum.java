package com.taotao.cloud.pinyin.roses.api.exception.enums;


import com.taotao.cloud.common.constant.RuleConstants;
import com.taotao.cloud.pinyin.roses.api.constants.PinyinConstants;

/**
 * 拼音工具相关异常
 *
 */
public enum PinyinExceptionEnum {

    /**
     * 字符不能转成汉语拼音
     */
    PARSE_ERROR(RuleConstants.THIRD_ERROR_TYPE_CODE + PinyinConstants.PINYIN_EXCEPTION_STEP_CODE + "01", "拼音转化异常，具体信息：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    PinyinExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
