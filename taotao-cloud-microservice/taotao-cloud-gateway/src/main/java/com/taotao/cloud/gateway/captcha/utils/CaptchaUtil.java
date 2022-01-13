package com.taotao.cloud.gateway.captcha.utils;

import com.taotao.cloud.gateway.captcha.BaseCaptcha;
import com.taotao.cloud.gateway.captcha.MathCaptcha;
import com.taotao.cloud.gateway.captcha.MathGifCaptcha;
import com.wf.captcha.*;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author chenjh
 * @since 2019/7/16 17:44
 */
public class CaptchaUtil {

    /**
     * 输出验证码
     *
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String out(OutputStream outputStream) {
        return out(5, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param len      长度
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String out(int len, OutputStream outputStream) {
        return out(130, 48, len, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param len      长度
     * @param font     字体
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String out(int len, Font font, OutputStream outputStream) {
        return out(130, 48, len, font, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String out(int width, int height, int len, OutputStream outputStream) {
        return out(width, height, len, null, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param font     字体
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String out(int width, int height, int len, Font font,  OutputStream outputStream) {
        int cType = new Random().nextInt(6);
        return outCaptcha(width, height, len, font, cType, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String outPng( OutputStream outputStream) {
        return outPng(5, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param len      长度
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String outPng(int len, OutputStream outputStream) {
        return outPng(130, 48, len,outputStream);
    }

    /**
     * 输出验证码
     *
     * @param len      长度
     * @param font     字体
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String outPng(int len, Font font, OutputStream outputStream) {
        return outPng(130, 48, len, font, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String outPng(int width, int height, int len, OutputStream outputStream) {
        return outPng(width, height, len, null, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param font     字体
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    public static String outPng(int width, int height, int len, Font font, OutputStream outputStream) {
        int cType = new Random().nextInt(6);
        return outCaptcha(width, height, len, font, cType, outputStream);
    }

    /**
     * 输出验证码
     *
     * @param width    宽度
     * @param height   高度
     * @param len      长度
     * @param font     字体
     * @param cType    类型
     * @param outputStream OutputStream
     * @return 验证码结果
     */
    private static String outCaptcha(int width, int height, int len, Font font, int cType, OutputStream outputStream) {
        BaseCaptcha captcha = null;
        if (cType == 0) {
            captcha = new SpecCaptcha(width, height, len);
        } else if (cType == 1) {
            captcha = new GifCaptcha(width, height, len);
        } else if (cType == 2) {
            captcha = new ChineseCaptcha(width, height, len);
        } else if (cType == 3) {
            captcha = new ChineseGifCaptcha(width, height, len);
        } else if (cType == 4) {
            captcha = new MathCaptcha(width, height, 4);
        } else if (cType == 5) {
            captcha = new MathGifCaptcha(width, height, 4);
        }
        if (font != null) {
            captcha.setFont(font);
        }
        captcha.out(outputStream);
        return captcha.text().toLowerCase();
    }
}
