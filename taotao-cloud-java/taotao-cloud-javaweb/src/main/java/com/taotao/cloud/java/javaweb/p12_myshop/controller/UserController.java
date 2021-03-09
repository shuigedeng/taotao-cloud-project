package com.taotao.cloud.java.javaweb.p12_myshop.controller;

import com.itqf.entity.User;
import com.itqf.service.UserService;
import com.itqf.service.impl.UserServiceImpl;
import com.itqf.utils.Base64Utils;
import com.itqf.utils.Constants;
import com.itqf.utils.MD5Utils;
import com.itqf.utils.RandomUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

/**
 * 用户用户模块的controller
 */
@WebServlet("/user")
public class UserController extends BaseServlet {

    public String check(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        //1.获取用户名
        String username = request.getParameter("username");

        if (username == null) {
            return Constants.HAS_USER; //不能注册
        }
        //2.调用业务逻辑判断用户名是否存在
        UserService userService = new UserServiceImpl();
        boolean b = userService.checkedUser(username);
        //3.响应字符串  1 存在  0 不存在
        if (b) {
            //用户存在
            return Constants.HAS_USER;
        }
        return Constants.NOT_HAS_USER;
    }


    public String register(HttpServletRequest request, HttpServletResponse response){
        //1.获取用户信息
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //2.完善用户信息
        //已经赋值： 用户名 密码 邮箱 性别
        //没有赋值： 激活状态 账号类型 激活码
        user.setUstatus(Constants.USER_NOT_ACTIVE); //未激活状态 0 激活 1
        user.setUrole(Constants.ROLE_CUSTOMER); //普通客户0  管理员1
        user.setCode(RandomUtils.createActive());

        //需要处理的属性：密码 md5进行加密处理
        user.setUpassword(MD5Utils.md5(user.getUpassword()));

        //3.调用用户的业务逻辑进行注册
        UserService userService = new UserServiceImpl();
        try {
            userService.registerUser(user);
        } catch (SQLException e) {
            e.printStackTrace();

            request.setAttribute("registerMsg","注册失败！");

            return Constants.FORWARD+"/register.jsp";
        }

        //4.响应
        return Constants.FORWARD + "/registerSuccess.jsp";
    }

    /**
     * 激活方法！
     * @param request
     * @param response
     * @return
     */
    public String active(HttpServletRequest request,HttpServletResponse response) throws SQLException {
        //1.获取激活码
        //已经转成base64
        String c = request.getParameter("c");
        //base64翻转
        String code = Base64Utils.decode(c);
        //2.调用激活的业务逻辑
        UserService userService = new UserServiceImpl();
        int i = userService.activeUser(code);

        //3.响应(激活失败（code没有找到） 已经激活 激活成功)
        if (i == Constants.ACTIVE_FAIL){
            request.setAttribute("msg","未激活成功！");
        }else if(i==Constants.ACTIVE_SUCCESS){
            request.setAttribute("msg","激活成功，请登录！");
        }else{
            request.setAttribute("msg","已经激活");
        }

        return Constants.FORWARD+"/message.jsp";
    }

    /**
     * 1.前端提交账号密码和验证码
     * 2.对比验证码 成功 ---》 对比账号密码
     * 3.对比账号密码
     *   失败： --》 回到登录页面 进行提示
     *   成功： --》 未激活  登录页面 进行提示
     *         --》 已激活  程序的首页  将用户放入session共享域
     */

    public String login(HttpServletRequest request,HttpServletResponse response) throws SQLException {


        //1.获取请求参数（用户名，密码，验证码）
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String code  = request.getParameter("code");//用户输入的验证码
        String auto = request.getParameter("auto"); //自动登录标识

        //正确的验证码
        HttpSession session = request.getSession();
        String codestr = (String) session.getAttribute("code");

        //2.判断验证码是否正确

        if (code == null || !code.equalsIgnoreCase(codestr)){
            request.setAttribute("msg","验证码错误");
            return Constants.FORWARD + "/login.jsp";
        }
        //3.调用业务逻辑判断账号密码

        UserService userService = new UserServiceImpl();
        User user = userService.login(username, password);

        //4.响应
        //user 等于null证明账号或者密码错误
        //user 不为null 但是user的状态是未激活状态

        if (user == null) {
            request.setAttribute("msg","账号或者密码错误");
            return Constants.FORWARD +"/login.jsp";
        }

        if (user.getUstatus().equals(Constants.USER_NOT_ACTIVE))
        {
            request.setAttribute("msg","账号未激活！");
            return Constants.FORWARD +"/login.jsp";
        }

        session.setAttribute("loginUser",user);

        //autoUser
        //判断是否勾选自动登录
        if (auto == null){
            //没有勾选！
            //将本地浏览器的存储的cookie'清空'
            Cookie cookie = new Cookie(Constants.AUTO_NAME,"");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }else{
            //自定登录数据库存储 2周
            String content = username+Constants.FLAG+password;
            content = Base64Utils.encode(content);
            Cookie cookie = new Cookie(Constants.AUTO_NAME,content);
            cookie.setPath("/");
            cookie.setMaxAge(14*24*60*60);
            response.addCookie(cookie);
        }


        return Constants.REDIRECT + "/index.jsp";
    }

    /**
     * 注销登录！清空数据！跳转到登录页面
     * @param request
     * @param response
     * @return
     */
    public String logOut(HttpServletRequest request,HttpServletResponse response){

        //1.清空session中的用户数据
        HttpSession session = request.getSession();
        session.removeAttribute("loginUser");

        //2.清空和覆盖cookie存储的自动登录信息

        Cookie cookie = new Cookie(Constants.AUTO_NAME,"");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        //3.转发到登录页面
        request.setAttribute("msg","注销登录成功！");
        return  Constants.FORWARD + "/login.jsp";
    }

}
