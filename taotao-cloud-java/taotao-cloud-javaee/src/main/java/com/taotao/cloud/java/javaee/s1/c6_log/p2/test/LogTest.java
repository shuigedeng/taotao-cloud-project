package com.taotao.cloud.java.javaee.s1.c6_log.p2.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    private Logger log = LoggerFactory.getLogger(LogTest.class);
    @Test
    public void test1() throws InterruptedException {
        while(true) {
            log.trace("hello trace");
            log.debug("hello debug");
            log.info("hello info");
            log.warn("hello warn");
            log.error("hello error");
            Thread.sleep(1000);
        }
    }
}
