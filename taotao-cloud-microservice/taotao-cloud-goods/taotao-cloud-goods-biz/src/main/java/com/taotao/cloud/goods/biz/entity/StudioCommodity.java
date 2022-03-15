package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 直播间商品表
 */
@Entity
@Table(name = StudioCommodity.TABLE_NAME)
@TableName(StudioCommodity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StudioCommodity.TABLE_NAME, comment = "直播间商品表")
public class StudioCommodity extends BaseSuperEntity<StudioCommodity, Long> {

	public static final String TABLE_NAME = "tt_studio_commodity";

	/**
	 * 房间ID
	 */
	@Column(name = "room_id", nullable = false, columnDefinition = "int not null comment '房间ID'")
	private Integer roomId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id", nullable = false, columnDefinition = "varchar(64) not null comment '商品ID'")
	private String goodsId;

	public StudioCommodity(Integer roomId, String goodsId) {
		this.roomId = roomId;
		this.goodsId = goodsId;
	}


	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
}
