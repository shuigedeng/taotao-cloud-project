package com.taotao.cloud.core.heaven.support.wait.impl;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.support.wait.IWait;
import java.util.concurrent.TimeUnit;

/**
 * 不进行任何等待
 */
@ThreadSafe
@Deprecated
public class NoWait implements IWait {

    @Override
    public void waits(long time, TimeUnit timeUnit) {

    }
    
}
