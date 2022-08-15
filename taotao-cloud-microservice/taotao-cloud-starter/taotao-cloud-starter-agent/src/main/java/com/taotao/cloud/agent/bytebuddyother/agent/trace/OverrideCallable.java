package com.taotao.cloud.agent.bytebuddyother.agent.trace;

/**
 * Created by pphh on 2022/3/7.
 */
public interface OverrideCallable {

    /**
     * callable interface
     *
     * @param args
     * @return
     */
    Object call(Object[] args);

}
