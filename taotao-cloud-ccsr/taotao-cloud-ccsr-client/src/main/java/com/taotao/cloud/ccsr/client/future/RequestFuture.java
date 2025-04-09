package com.taotao.cloud.ccsr.client.future;

import com.taotao.cloud.ccsr.api.grpc.auto.Response;

/**
 * @author SpringCat
 * @date 2025-03-26 16:30
 */
public interface RequestFuture<S> {

    /**
     * check that it is done or not..
     */
    boolean isDone();

    /**
     * get response without timeouts.
     */
    S get() throws Exception;

    /**
     * get response with a given timeouts.
     */
    S get(long timeout) throws Exception;
}
