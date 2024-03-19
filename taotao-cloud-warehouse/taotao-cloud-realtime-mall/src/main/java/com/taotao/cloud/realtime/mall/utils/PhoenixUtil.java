package com.taotao.cloud.realtime.mall.utils;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.realtime.mall.common.GmallConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * Date: 2021/2/5
 * Desc: 从Phoenix中查询数据
 */
public class PhoenixUtil {
    private static Connection conn = null;

    public static void init(){
        try {
            //注册驱动
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            //获取Phoenix的连接
            conn = DriverManager.getConnection(GmallConfig.PHOENIX_SERVER);
            //指定操作的表空间
            conn.setSchema(GmallConfig.HABSE_SCHEMA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 从Phoenix中查询数据
    // select * from 表 where XXX=xxx
    public static <T> List<T> queryList(String sql,Class<T> clazz){
        if(conn == null){
            init();
        }
        List<T> resultList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库操作对象
            ps = conn.prepareStatement(sql);
            //执行SQL语句
            rs = ps.executeQuery();
            //通过结果集对象获取元数据信息
            ResultSetMetaData metaData = rs.getMetaData();
            //处理结果集
            while (rs.next()){
                //声明一个对象，用于封装查询的一条结果集
                T rowData = clazz.newInstance();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    BeanUtils.setProperty(rowData,metaData.getColumnName(i),rs.getObject(i));
                }
                resultList.add(rowData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("从维度表中查询数据失败");
        } finally {
            //释放资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultList;
    }

    public static void main(String[] args) {
        System.out.println(queryList("select * from DIM_BASE_TRADEMARK", JSONObject.class));
    }

}
