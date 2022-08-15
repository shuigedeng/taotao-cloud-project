package com.taotao.cloud.payment.biz.demo.dao.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.yungouos.springboot.demo.dao.OrderDao;
import com.yungouos.springboot.demo.entity.Order;

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
