/**
 * Copyright 2018 bejson.com
 * <p>
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.api.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExpressInfo {

    @JsonProperty("LogisticCode")
    private String LogisticCode;
    @JsonProperty("ShipperCode")
    private String ShipperCode;
    @JsonProperty("Traces")
    private List<Traces> Traces;
    @JsonProperty("State")
    private String State;
    @JsonProperty("EBusinessID")
    private String EBusinessID;
    @JsonProperty("Success")
    private boolean Success;
    @JsonProperty("Reason")
    private String Reason;

    private String ShipperName;

    @JsonProperty("OrderCode")
    private String OrderCode;
	public ExpressInfo(){

	}
	public ExpressInfo(String logisticCode, String shipperCode,
		List<com.taotao.cloud.system.api.bo.Traces> traces, String state, String EBusinessID,
		boolean success, String reason, String shipperName, String orderCode) {
		LogisticCode = logisticCode;
		ShipperCode = shipperCode;
		Traces = traces;
		State = state;
		this.EBusinessID = EBusinessID;
		Success = success;
		Reason = reason;
		ShipperName = shipperName;
		OrderCode = orderCode;
	}

	public String getLogisticCode() {
		return LogisticCode;
	}

	public void setLogisticCode(String logisticCode) {
		LogisticCode = logisticCode;
	}

	public String getShipperCode() {
		return ShipperCode;
	}

	public void setShipperCode(String shipperCode) {
		ShipperCode = shipperCode;
	}

	public List<com.taotao.cloud.system.api.bo.Traces> getTraces() {
		return Traces;
	}

	public void setTraces(List<com.taotao.cloud.system.api.bo.Traces> traces) {
		Traces = traces;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getEBusinessID() {
		return EBusinessID;
	}

	public void setEBusinessID(String EBusinessID) {
		this.EBusinessID = EBusinessID;
	}

	public boolean isSuccess() {
		return Success;
	}

	public void setSuccess(boolean success) {
		Success = success;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public String getShipperName() {
		return ShipperName;
	}

	public void setShipperName(String shipperName) {
		ShipperName = shipperName;
	}

	public String getOrderCode() {
		return OrderCode;
	}

	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}
}
