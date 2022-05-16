package com.taotao.cloud.core.dynamictp;

import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.DtpRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController related
 */
@RestController
@RequestMapping("/dtp")
public class IndexController {

    private static final Logger logger = LogManager.getLogger();

    @GetMapping("/test")
    public void test() {
        DtpExecutor dtpExecutor = DtpRegistry.getDtpExecutor("dynamic-tp-test");
        dtpExecutor.execute(() -> System.out.println("test"));
    }
}
