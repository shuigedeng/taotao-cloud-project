package com.taotao.cloud.pinyin.roses.api.context;

import cn.hutool.extra.spring.SpringUtil;
import com.taotao.cloud.pinyin.roses.api.PinYinApi;

/**
 * 拼音工具类快速获取
 */
public class PinyinContext {

    /**
     * 获取拼音工具类
     *
     */
    public static PinYinApi me() {
        return SpringUtil.getBean(PinYinApi.class);
    }

}
