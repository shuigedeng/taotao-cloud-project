package com.taotao.cloud.ccsr.client.lifecycle;


import com.taotao.cloud.ccsr.common.exception.CcsrClientException;

public interface Closeable {
    
    /**
     * Shutdown the Resources, such as Thread Pool.
     *
     * @throws CcsrClientException exception.
     */
    void shutdown() throws CcsrClientException;
    
}
