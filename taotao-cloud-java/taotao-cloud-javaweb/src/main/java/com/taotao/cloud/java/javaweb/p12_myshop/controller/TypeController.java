package com.taotao.cloud.java.javaweb.p12_myshop.controller;

import com.google.gson.Gson;
import com.itqf.entity.Type;
import com.itqf.service.TypeService;
import com.itqf.service.impl.TypeServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/type")
public class TypeController extends BaseServlet {

    public String findAll(HttpServletRequest request, HttpServletResponse response) throws SQLException {

        TypeService typeService = new TypeServiceImpl();
        List<Type> types = typeService.findAll();

        Gson gson = new Gson();
        String json = gson.toJson(types);

        return json;
    }
}
