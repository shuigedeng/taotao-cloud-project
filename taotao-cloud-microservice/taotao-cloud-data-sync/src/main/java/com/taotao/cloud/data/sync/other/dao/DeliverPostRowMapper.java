package com.taotao.cloud.data.sync.other.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.open.capacity.batch.entity.DeliverPost;

public class DeliverPostRowMapper implements RowMapper<DeliverPost> {
	public DeliverPost mapRow(ResultSet rs, int rowNum) throws SQLException {
		DeliverPost deliverPost = new DeliverPost();
		deliverPost.setOrderId(rs.getString("order_id"));
		deliverPost.setPostId(rs.getString("post_id"));
		deliverPost.setArrived(rs.getBoolean("isArrived"));
		return deliverPost;
	}
}
