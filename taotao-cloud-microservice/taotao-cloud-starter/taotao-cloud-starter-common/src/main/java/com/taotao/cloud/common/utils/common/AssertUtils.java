package com.taotao.cloud.common.utils.common;


import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.log.LogUtils;

/**
 * RESTful断言
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 10:02:51
 */
public class AssertUtils extends cn.hutool.core.lang.Assert {

    /**
     * 断言-执行给定代码块不会抛出异常
     * <p>若不是期望的结果，执行代码块时出现错误，将抛出ResultException</p>
     *
     * @param throwMsg  执行代码块错误时抛出的ResultException message
     * @param lambdaRun lambda给定代码块，示例：{@code () -&gt; {代码块}}
     */
    public static void notThrow(String throwMsg, Runnable lambdaRun) {
        try {
            lambdaRun.run();
        } catch (Exception e) {
            throw new BaseException(throwMsg);
        }
    }

    /**
     * 断言-执行给定代码块不会抛出异常
     * <p>若不是期望的结果，执行代码块时出现错误，将打印printMsg</p>
     *
     * @param printMsg  执行代码块错误时，打印的信息
     * @param lambdaRun lambda给定代码块，示例：{@code () -&gt; {代码块}}
     */
    public static void notThrowIfErrorPrintMsg(String printMsg, Runnable lambdaRun) {
        try {
            lambdaRun.run();
        } catch (Exception e) {
            LogUtils.warn(printMsg);
        }
    }

    /**
     * 断言-执行给定代码块不会抛出异常
     * <p>若不是期望的结果，执行代码块时出现错误，将打印printMsg与堆栈跟踪信息</p>
     *
     * @param printMsg  执行代码块错误时，打印的信息
     * @param lambdaRun lambda给定代码块，示例：{@code () -&gt; {代码块}}
     */
    public static void notThrowIfErrorPrintStackTrace(String printMsg, Runnable lambdaRun) {
        try {
            lambdaRun.run();
        } catch (Exception e) {
	        LogUtils.warn(printMsg);
            e.printStackTrace();
        }
    }

}
