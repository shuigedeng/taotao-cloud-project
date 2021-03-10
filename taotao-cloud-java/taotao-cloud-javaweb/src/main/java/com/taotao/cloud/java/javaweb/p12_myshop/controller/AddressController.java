package com.taotao.cloud.java.javaweb.p12_myshop.controller;


import com.taotao.cloud.java.javaweb.p12_myshop.entity.Address;
import com.taotao.cloud.java.javaweb.p12_myshop.entity.User;
import com.taotao.cloud.java.javaweb.p12_myshop.service.AddressService;
import com.taotao.cloud.java.javaweb.p12_myshop.service.impl.AddressServiceImpl;
import com.taotao.cloud.java.javaweb.p12_myshop.utils.Constants;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/address")
public class AddressController extends BaseServlet {


    public String show(HttpServletRequest request, HttpServletResponse response) throws SQLException {

        HttpSession session = request.getSession();
        User user  = (User) session.getAttribute("loginUser");
        if (user == null)
        {
            session.setAttribute("msg", "需要先登录！");
            return Constants.REDIRECT + "/login.jsp";
        }

        int uid = user.getUid();

        AddressService addressService = new AddressServiceImpl();
        List<Address> addresses = addressService.findAddressByUid(uid);

        request.setAttribute("list", addresses);

        return Constants.FORWARD + "/self_info.jsp";
    }


    public String add(HttpServletRequest request,HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, SQLException {

        //1.获取请求参数
        Map<String, String[]> map = request.getParameterMap();
        Address address = new Address();
        BeanUtils.populate(address,map);

        //2.调用业务逻辑进行地址添加
        AddressService addressService = new AddressServiceImpl();
        addressService.saveAddress(address);
        //3。转发到展示的方法
        return Constants.FORWARD + "/address?method=show";
    }

    public String delete(HttpServletRequest request,HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, SQLException {

        //1.获取请求参数
        String aid = request.getParameter("aid");

        //2.调用业务逻辑进行地址添加
        AddressService addressService = new AddressServiceImpl();
        addressService.deleteAddressByAid(aid);
        //3。转发到展示的方法
        return Constants.FORWARD + "/address?method=show";
    }

    public String setDefault(HttpServletRequest request,HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, SQLException {

        //1.获取请求参数
        String aid = request.getParameter("aid");
        HttpSession session = request.getSession();
        User user  = (User) session.getAttribute("loginUser");
        if (user == null)
        {
            session.setAttribute("msg", "需要先登录！");
            return Constants.REDIRECT + "/login.jsp";
        }

        //2.调用业务逻辑进行地址添加
        AddressService addressService = new AddressServiceImpl();
        addressService.setAddressToDefault(aid,user.getUid());
        //3。转发到展示的方法
        return Constants.FORWARD + "/address?method=show";
    }

    public String update(HttpServletRequest request,HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, SQLException {

        //1.获取请求参数
        Map<String, String[]> map = request.getParameterMap();

        Address address = new Address();
        BeanUtils.populate(address,map);


        //2.调用业务逻辑进行地址添加
        AddressService addressService = new AddressServiceImpl();
        addressService.updateByAid(address);
        //3。转发到展示的方法
        return Constants.FORWARD + "/address?method=show";
    }
}
