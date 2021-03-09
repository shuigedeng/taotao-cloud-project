package com.taotao.cloud.java.javaee.s1.c6_log.p1.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class LogTest {
    // 日志对象
    private Log log = LogFactory.getLog(LogTest.class);
    @Test
    public void test1(){
        log.trace("hello trace5~~");
        log.debug("hello debug5~~");
        log.info("hello info5~~");
        log.warn("hello warn5");
        log.error("hello error5");
        log.error("hello error5");
        log.error("hello error5");
        log.error("hello error5");
        log.error("hello error5");
        log.error("hello error5");
        log.error("hello error5");
        log.error("hello error5");
        log.error("hello error5");
        log.error("hello error5");
        log.fatal("hello fatal5");
    }
}
