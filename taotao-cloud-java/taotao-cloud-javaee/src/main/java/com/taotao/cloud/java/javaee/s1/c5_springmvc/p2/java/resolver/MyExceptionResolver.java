package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.resolver;

import com.qf.ex.MyException1;
import com.qf.ex.MyException2;
import com.qf.ex.MyException3;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 异常解析器
// 任何一个Handler中抛出异常时
public class MyExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        if(e instanceof MyException1){
            // error1.jsp
            modelAndView.setViewName("redirect:/error1.jsp");
        }else if(e instanceof MyException2){
            // error2.jsp
            modelAndView.setViewName("redirect:/error2.jsp");
        }else if(e instanceof MyException3){
            // error3.jsp
            modelAndView.setViewName("redirect:/error3.jsp");
        }else if(e instanceof MaxUploadSizeExceededException){
            modelAndView.setViewName("redirect:/uploadError.jsp");
        }

        return modelAndView;
    }
}
