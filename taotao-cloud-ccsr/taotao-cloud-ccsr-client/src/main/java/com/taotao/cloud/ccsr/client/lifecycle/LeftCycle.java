package com.taotao.cloud.ccsr.client.lifecycle;

import com.taotao.cloud.ccsr.common.exception.OHaraMcsClientException;

import java.util.concurrent.TimeUnit;

public interface LeftCycle {

    void init() throws Exception;


    void destroy(Integer timeout, TimeUnit unit);

}
