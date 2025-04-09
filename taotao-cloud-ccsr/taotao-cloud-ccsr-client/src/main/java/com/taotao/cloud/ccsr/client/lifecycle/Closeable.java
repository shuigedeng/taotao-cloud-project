package com.taotao.cloud.ccsr.client.lifecycle;


import com.taotao.cloud.ccsr.common.exception.OHaraMcsClientException;

public interface Closeable {
    
    /**
     * Shutdown the Resources, such as Thread Pool.
     *
     * @throws OHaraMcsClientException exception.
     */
    void shutdown() throws OHaraMcsClientException;
    
}
