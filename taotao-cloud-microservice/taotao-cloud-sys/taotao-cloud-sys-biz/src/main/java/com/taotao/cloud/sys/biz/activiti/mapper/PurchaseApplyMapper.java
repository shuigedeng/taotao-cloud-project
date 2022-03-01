package com.taotao.cloud.sys.biz.activiti.mapper;

import java.util.List;

import boot.spring.po.PurchaseApply;

public interface PurchaseApplyMapper {
	void save(PurchaseApply apply);
	
	PurchaseApply getPurchaseApply(int id);
	
	void updateByPrimaryKeySelective(PurchaseApply apply);
	
	List<PurchaseApply> listPurchaseApplyByApplyer(String username);
}
