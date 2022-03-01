package com.taotao.cloud.sys.biz.activiti.service;

import java.util.List;
import java.util.Map;
import org.activiti.engine.runtime.ProcessInstance;

import boot.spring.po.PurchaseApply;

public interface PurchaseService {
	public ProcessInstance startWorkflow(PurchaseApply apply,String userid,Map<String,Object> variables);
	PurchaseApply getPurchase(int id);
	void updatePurchase(PurchaseApply a);
	
	List<PurchaseApply> listPurchaseApplyByApplyer(String username);
	
	List<PurchaseApply> listPurchaseApplyByApplyer(String username, int current, int rowCount);
}
