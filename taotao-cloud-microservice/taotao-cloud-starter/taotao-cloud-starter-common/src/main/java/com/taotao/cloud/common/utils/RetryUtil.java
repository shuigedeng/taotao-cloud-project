package com.taotao.cloud.common.utils;
import java.util.List;
import java.util.function.Consumer;

/**
 * 错误重试工具类
 *
 * @author huojuncheng
 * @date 2020/8/4 23:27
 */
public abstract class RetryUtil {
    private final static String name="重试工具";
    /**
     * 重试调度方法
     *
     * @param dataSupplier
     *      返回数据方法执行体
     * @param exceptionCaught
     *      出错异常处理(包括第一次执行和重试错误)
     * @param retryCount
     *      重试次数
     * @param sleepTime
     *      重试间隔睡眠时间(注意：阻塞当前线程)
     * @param expectExceptions
     *      期待异常(抛出符合相应异常时候重试),空或者空容器默认进行重试
     * @param <R>
     *      数据类型
     *
     * @return R
     */
    public static <R> R invoke(Supplier<R> dataSupplier, Consumer<Throwable> exceptionCaught, int retryCount, long sleepTime, List<Class<? extends Throwable>> expectExceptions) {
        Throwable ex;
        try {
            // 产生数据
            return dataSupplier == null ? null : dataSupplier.get();
        } catch (Throwable throwable) {
            // 捕获异常
            catchException(exceptionCaught, throwable);
            ex = throwable;
        }

        if (expectExceptions != null && !expectExceptions.isEmpty()) {
            // 校验异常是否匹配期待异常
            Class<? extends Throwable> exClass = ex.getClass();
            boolean match = expectExceptions.stream().anyMatch(clazz -> clazz == exClass);
            if (!match) {
                return null;
            }
        }

        // 匹配期待异常或者允许任何异常重试
        for (int i = 0; i < retryCount; i++) {
            try {
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
                return dataSupplier.get();
            } catch (InterruptedException e) {
                System.err.println("thread interrupted !! break retry,cause:" + e.getMessage());
                // 恢复中断信号
                Thread.currentThread().interrupt();
                // 线程中断直接退出重试
                break;
            } catch (Throwable throwable) {
                catchException(exceptionCaught, throwable);
            }
        }

        return null;
    }

    private static void catchException(Consumer<Throwable> exceptionCaught, Throwable throwable) {
        try {
            if (exceptionCaught != null) {
                exceptionCaught.accept(throwable);
            }
        } catch (Throwable e) {
            LogUtil.error("retry exception caught throw error:"+ExceptionUtil.getFullStackTrace(e));
        }
    }

    /**
     *@描述 函数式接口可以抛出异常
     */
    @FunctionalInterface
    public interface Supplier<T> {
        T get() throws Exception;
    }
}
