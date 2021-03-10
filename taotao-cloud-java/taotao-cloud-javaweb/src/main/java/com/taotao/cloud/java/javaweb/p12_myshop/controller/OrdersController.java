package com.taotao.cloud.java.javaweb.p12_myshop.controller;


import com.google.gson.Gson;

import com.taotao.cloud.java.javaweb.p12_myshop.entity.Address;
import com.taotao.cloud.java.javaweb.p12_myshop.entity.Cart;
import com.taotao.cloud.java.javaweb.p12_myshop.entity.Orders;
import com.taotao.cloud.java.javaweb.p12_myshop.entity.User;
import com.taotao.cloud.java.javaweb.p12_myshop.entity.WeiXin;
import com.taotao.cloud.java.javaweb.p12_myshop.service.AddressService;
import com.taotao.cloud.java.javaweb.p12_myshop.service.CartService;
import com.taotao.cloud.java.javaweb.p12_myshop.service.OrdersService;
import com.taotao.cloud.java.javaweb.p12_myshop.service.impl.AddressServiceImpl;
import com.taotao.cloud.java.javaweb.p12_myshop.service.impl.CartServiceImpl;
import com.taotao.cloud.java.javaweb.p12_myshop.service.impl.OrdersServiceImpl;
import com.taotao.cloud.java.javaweb.p12_myshop.utils.Constants;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/order")
public class OrdersController extends BaseServlet {

    public String preView(HttpServletRequest request, HttpServletResponse response) throws SQLException, InvocationTargetException, IllegalAccessException {

        //1.获取请求参数uid
        String uid = request.getParameter("uid");

        //2.调用业务逻辑 （地址，购物车）
        AddressService addressService = new AddressServiceImpl();
        List<Address> addressList = addressService.findAddressByUid(Integer.parseInt(uid));

        CartService cartService = new CartServiceImpl();
        List<Cart> cartList = cartService.findAll(Integer.parseInt(uid));
        //3.放入共享域即可
        request.setAttribute("addressList", addressList);
        request.setAttribute("cartList", cartList);

        return Constants.FORWARD + "/order.jsp";
    }

    public String create(HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, SQLException, InvocationTargetException {

        //1.获取请求参数
        String aid = request.getParameter("aid");
        String uid = request.getParameter("uid");
        String sum = request.getParameter("sum");

        //2.调用业务逻辑生成订单

        OrdersService ordersService = new OrdersServiceImpl();
        ordersService.createOrder(aid,uid,sum);

        //3.转发到订单展示的方法

        return Constants.FORWARD +"order?method=show";
    }


    public String show(HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, SQLException, InvocationTargetException {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("loginUser");

        if (user == null) {
            session.setAttribute("msg", "登录后可以查看订单！");
            return Constants.REDIRECT + "/login.jsp";
        }

        OrdersService ordersService = new OrdersServiceImpl();
        List<Orders> ordersList = ordersService.findOrdersByUid(user.getUid());
        request.setAttribute("ordersList", ordersList);
        return Constants.FORWARD  + "/orderList.jsp";
    }

    public String detail(HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, SQLException, InvocationTargetException {

        String oid = request.getParameter("oid");

        OrdersService ordersService = new OrdersServiceImpl();
        Orders order = ordersService.findOrderByOid(oid);
        request.setAttribute("order", order);
        return Constants.FORWARD  + "/orderDetail.jsp";
    }


    public String success(HttpServletRequest request,HttpServletResponse response) throws SQLException {
        //1.获取请求参数
        String oid = request.getParameter("oid");
        String result = request.getParameter("result");

        Gson gson = new Gson();
        WeiXin weiXin = gson.fromJson(result, WeiXin.class);

        String result_code = weiXin.getResult().getResult_code();

        if (result_code != null && result_code.equals("SUCCESS"))
        {
            //支付成功
            //修改状态
            //订单列表页面
            OrdersService ordersService = new OrdersServiceImpl();
            ordersService.updateStateByOid(oid);

            return Constants.FORWARD + "/order?method=show";
        }else{
            //支付失败
            HttpSession session = request.getSession();
            session.setAttribute("msg", "订单："+oid+" 支付失败！");
            //message.jsp页面
            return Constants.REDIRECT + "/message.jsp";
        }
    }
}
