package com.taotao.cloud.office.easypoi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("easypoi-preview.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/easypoijs/**").addResourceLocations("classpath:/META-INF/resources/easypoijs/");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
