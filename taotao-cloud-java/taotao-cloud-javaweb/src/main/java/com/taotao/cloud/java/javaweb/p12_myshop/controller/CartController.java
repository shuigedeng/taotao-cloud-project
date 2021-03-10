package com.taotao.cloud.java.javaweb.p12_myshop.controller;


import com.taotao.cloud.java.javaweb.p12_myshop.entity.Cart;
import com.taotao.cloud.java.javaweb.p12_myshop.entity.User;
import com.taotao.cloud.java.javaweb.p12_myshop.service.CartService;
import com.taotao.cloud.java.javaweb.p12_myshop.service.impl.CartServiceImpl;
import com.taotao.cloud.java.javaweb.p12_myshop.utils.Constants;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cart")
public class CartController extends BaseServlet {

    public String create(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, SQLException, InvocationTargetException {

        //1.判断是否已经登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            session.setAttribute("msg", "添加购物车必须先登录！");
            return Constants.REDIRECT+"/login.jsp";
        }
        //2.商品的id和用户id
        int uid = user.getUid();
        String pid = request.getParameter("pid");

        CartService cartService = new CartServiceImpl();
        cartService.createCart(uid,pid);

        return Constants.FORWARD+ "/cartSuccess.jsp";
    }


    public String show(HttpServletRequest request,HttpServletResponse response) throws IllegalAccessException, SQLException, InvocationTargetException {
        //1.判断是否已经登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            session.setAttribute("msg", "添加购物车必须先登录！");
            return Constants.REDIRECT+"/login.jsp";
        }

        //2.获取参数
        int uid = user.getUid();

        //3.调用业务逻辑进行数据查询

        CartService cartService = new CartServiceImpl();
        List<Cart> list = cartService.findAll(uid);

        request.setAttribute("list", list);

        return Constants.FORWARD + "/cart.jsp";
    }

    public String delete(HttpServletRequest request,HttpServletResponse response) throws SQLException {
        //1.获取cid
        String cid = request.getParameter("cid");

        //2.调用业务逻辑进行删除
        CartService cartService = new CartServiceImpl();
        cartService.deleteCartByCid(cid);
        //3.转发到展示的处理方法中
        return Constants.FORWARD + "/cart?method=show";
    }

    public String update(HttpServletRequest request,HttpServletResponse response) throws SQLException {
        //1.获取cid
        String cid = request.getParameter("cid");
        String price = request.getParameter("price"); //商品的单价
        String cnum = request.getParameter("cnum"); //修改后的数量

        //2.调用业务逻辑进行删除
        CartService cartService = new CartServiceImpl();
        cartService.updateCartByCid(cid,price,cnum);
        //3.转发到展示的处理方法中
        return Constants.FORWARD + "/cart?method=show";
    }

    public String clear(HttpServletRequest request,HttpServletResponse response) throws SQLException {
        //1.获取uid
        String uid = request.getParameter("uid");
        String price = request.getParameter("price"); //商品的单价
        String cnum = request.getParameter("cnum"); //修改后的数量

        //2.调用业务逻辑进行删除
        CartService cartService = new CartServiceImpl();
        cartService.clearCart(uid);
        //3.转发到展示的处理方法中
        return Constants.FORWARD + "/cart?method=show";
    }
}
