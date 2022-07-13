package com.taotao.cloud.im.biz.platform.common.web.domain;

import com.platform.common.enums.ResultCodeEnum;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * 操作消息提醒
 */
public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param resultCode 状态码
     * @param msg        返回内容
     * @param data       数据对象
     */
    public AjaxResult(ResultCodeEnum resultCode, String msg, Object data) {
        super.put(CODE_TAG, resultCode.getCode());
        super.put(MSG_TAG, StringUtils.isEmpty(msg) ? resultCode.getInfo() : msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return new AjaxResult(ResultCodeEnum.SUCCESS, null, null);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return new AjaxResult(ResultCodeEnum.SUCCESS, null, data);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data, String msg) {
        return new AjaxResult(ResultCodeEnum.SUCCESS, msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult successMsg(String msg) {
        return new AjaxResult(ResultCodeEnum.SUCCESS, msg, null);
    }

    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static AjaxResult fail() {
        return new AjaxResult(ResultCodeEnum.FAIL, null, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult fail(String msg) {
        return new AjaxResult(ResultCodeEnum.FAIL, msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param resultCode 状态码
     * @return 警告消息
     */
    public static AjaxResult result(ResultCodeEnum resultCode) {
        return result(resultCode, resultCode.getInfo());
    }

    /**
     * 返回错误消息
     *
     * @param resultCode 状态码
     * @param msg        返回内容
     * @return 警告消息
     */
    public static AjaxResult result(ResultCodeEnum resultCode, String msg) {
        if (StringUtils.isEmpty(msg)) {
            msg = resultCode.getInfo();
        }
        return new AjaxResult(resultCode, msg, null);
    }

    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
