package com.taotao.cloud.core.heaven.support.wait.impl;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.response.exception.CommonRuntimeException;
import com.taotao.cloud.core.heaven.support.wait.IWait;
import java.util.concurrent.TimeUnit;

/**
 * 沉睡等待
 */
@ThreadSafe
@Deprecated
public class SleepWait implements IWait {

    @Override
    public void waits(long time, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CommonRuntimeException(e);
        }
    }

}
