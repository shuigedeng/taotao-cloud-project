package com.zhengqing.testdockerjar;

import cn.hutool.core.date.DateUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/5/4 9:04 下午
 */
@RestController
public class TestController {

    /**
     * http://127.0.0.1:8080/hi
     */
    @RequestMapping("hi")
    public String hi() {
        String time = DateUtil.now();
        System.out.println("当前时间：" + time);
        return time;
    }

}
