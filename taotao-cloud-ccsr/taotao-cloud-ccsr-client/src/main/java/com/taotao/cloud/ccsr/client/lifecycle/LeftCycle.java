package com.taotao.cloud.ccsr.client.lifecycle;

import java.util.concurrent.TimeUnit;

public interface LeftCycle {

    void init() throws Exception;


    void destroy(Integer timeout, TimeUnit unit);

}
