package com.taotao.cloud.agent.cglib;

import com.taotao.cloud.agent.demo.api.Greeting;
import com.taotao.cloud.agent.demo.common.Logger;
import net.sf.cglib.proxy.Enhancer;


/**
 * Created by pphh on 2022/6/24.
 */
public class Demo {

    public static void main(String[] args) {
        Logger.info("start the cglib demo..");

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Greeting.class);
        enhancer.setCallback(new SayHelloInterceptor());

        int count = 0;
        while (++count < 1000) {
            sleep(3);
            System.out.println();
            System.out.println("count = " + count);

            // test
            Greeting greeting = (Greeting) enhancer.create();
            greeting.sayHello();
        }


        Logger.info("end of demo.");
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

}
