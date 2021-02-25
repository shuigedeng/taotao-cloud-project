package com.taotao.cloud.demo.youzan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

// @RestController
// @SpringBootApplication
public class TaotaoCloudDemoYouzanApplication {

    public static void main(String[] args) {
        // SpringApplication.run(TaotaoCloudDemoYouzanApplication.class, args);
    }

    // @RequestMapping("/sm.gif")
    // public void log(HttpServletRequest request,
    //                 HttpServletResponse response) {
    //     String data = request.getParameter("data");
    //     System.out.println(data);
    //     response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    //     response.setHeader("Content-Type", "application/json;charset=UTF-8");
    //     try (Writer writer = response.getWriter()) {
    //         writer.write("hello");
    //         writer.flush();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

}
