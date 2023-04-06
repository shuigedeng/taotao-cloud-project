/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.biz.demo.dao.impl;

import com.yungouos.springboot.demo.dao.OrderDao;
import com.yungouos.springboot.demo.entity.Order;
import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

public class OrderDaoImpl implements OrderDao {

    @Autowired
    private Mapper<Order> mapper;

    @Override
    public Order selectOne(Order arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Order> select(Order arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Order> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int selectCount(Order arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Order selectByPrimaryKey(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean existsWithPrimaryKey(Object arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int insert(Order arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int insertSelective(Order arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Order arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(Order arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Order arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Object arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Order> selectByExample(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order selectOneByExample(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int selectCountByExample(Object arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteByExample(Object arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByExample(Order arg0, Object arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateByExampleSelective(Order arg0, Object arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Order> selectByExampleAndRowBounds(Object arg0, RowBounds arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Order> selectByRowBounds(Order arg0, RowBounds arg1) {
        // TODO Auto-generated method stub
        return null;
    }
}
