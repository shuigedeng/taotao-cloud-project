package com.taotao.cloud.java.javaweb.p12_myshop.controller;


import com.taotao.cloud.java.javaweb.p12_myshop.utils.Constants;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * BaseServlet用于集中处理方法的调用！
 * 以及返回值处理！
 * 以及默认页对应方法！
 */
public class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //1.获取请求参数（标识符）
        String methodStr = req.getParameter(Constants.TAG);


        //2.如果method没有获取到值！我们就跳转到首页！（标识符异常处理）
        if (methodStr == null && methodStr.equals("")) {
            methodStr = Constants.INDEX;
        }

        //3.反射调用对应的业务逻辑方法
        Class  clazz = this.getClass();

        try {
            Method method = clazz.getMethod(methodStr, HttpServletRequest.class, HttpServletResponse.class);

            Object result = method.invoke(this,req,resp);

            //4.集中处理返回值响应
            if (result != null) {
                //转发 重定向  返回字符
                String str = (String) result;
                if (str.startsWith(Constants.FORWARD)) {
                    //转发
                    String path = str.substring(str.indexOf(Constants.FLAG) + 1);
                    req.getRequestDispatcher(path).forward(req,resp);
                }else if (str.startsWith(Constants.REDIRECT)){
                    //重定向
                    String path = str.substring(str.indexOf(Constants.FLAG) + 1);
                    resp.sendRedirect(path);
                }else{
                    resp.getWriter().println(str);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            //controller 和 service dao 有异常都会到此处！
            req.getSession().setAttribute("msg", "程序异常!请稍后再试！");
            resp.sendRedirect("/message.jsp");

        }
    }

    /**
     * 当method标识符‘没有值’ 我们默认赋 index 访问每个controller的index方法！
     * 我们将方法提取到baseservlet中即可！
     * 默认处理：跳转到程序的首页！
     * @param req
     * @param resp
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public String index(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException {

        return Constants.FORWARD+"/index.jsp";
    }
}
