package com.taotao.cloud.order.biz.squirrel;
// 店铺审核状态
public enum ShopInfoAuditStatusEnum {
	audit(0,"待审核"),
	agree(1,"审核通过"),
	reject(2,"审核驳回");

	private Integer code;
	private String desc;

	ShopInfoAuditStatusEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
