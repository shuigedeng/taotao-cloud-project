package com.taotao.cloud.java.javaee.s1.c4_spring.p2.test;
import com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.fangdong.FangDongService;
import com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.fangdong.FangDongServiceImpl;
import com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.service.UserService;
import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyTest {

    @Test
    public void testJDK(){

        // 目标
        FangDongService fangDongService = new FangDongServiceImpl();

        // 额外功能
        InvocationHandler ih = new InvocationHandler(){
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 辅助功能、额外功能
                System.out.println("发布租房信息1");
                System.out.println("带租客看房1");
                // 核心
                fangDongService.zufang();
                return null;
            }
        };

        // 动态生成 代理类
        FangDongService proxy = (FangDongService)Proxy.newProxyInstance(DynamicProxyTest.class.getClassLoader(),
                fangDongService.getClass().getInterfaces(),
                ih);

        proxy.zufang();
    }
    @Test
    public void testCGLIB(){
        // 目标
        FangDongService fangDongService = new FangDongServiceImpl();

        //
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(FangDongServiceImpl.class);
        enhancer.setCallback(new org.springframework.cglib.proxy.InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                // 辅助功能、额外功能
                System.out.println("发布租房信息2");
                System.out.println("带租客看房2");
                // 核心
                fangDongService.zufang();
                return null;
            }
        });


        // 动态生成代理类
        FangDongServiceImpl proxy = (FangDongServiceImpl)enhancer.create();

        proxy.zufang();
    }

    @Test
    public void testSpringAOP(){
        ApplicationContext context  = new ClassPathXmlApplicationContext("/spring-context.xml");

        // 通过目标的beanid，获得代理对象
        UserService proxy = (UserService)context.getBean("userService");

        System.out.println(proxy.getClass());

        proxy.queryUsers();

        System.out.println("==========");
        proxy.deleteUser(null);

        System.out.println("==============");
        proxy.updateUser(null);
        System.out.println("===========");
        proxy.saveUser(null);
    }
}

