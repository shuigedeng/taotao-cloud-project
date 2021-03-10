package com.taotao.cloud.java.javaee.s2.c7_springboot.springboot.java.config;


import com.taotao.cloud.java.javaee.s2.c7_springboot.springboot.java.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration   // 代表当前类是一个配置类
public class UserConfig {


    @Bean(name = "user1")       // 构建一个实例，放到spring容器中
    public User user(){

        User user = new User();
        user.setId(1);
        user.setName("张三");
        return user;
    }

    /*
    <beans ....>            @Configuration
        <bean id="user1" class="com.qf.firstspringboot.entity.User" />
    </beans>
     */
}
