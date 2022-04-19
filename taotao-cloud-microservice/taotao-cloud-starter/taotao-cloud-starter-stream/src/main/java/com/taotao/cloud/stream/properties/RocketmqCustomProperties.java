package com.taotao.cloud.stream.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "taotao.data.rocketmq")
public class RocketmqCustomProperties {

    private String promotionTopic;

    private String promotionGroup;

    private String orderTopic;

    private String orderGroup;

    private String msgExtTopic;

    private String msgExtGroup;

    private String goodsTopic;

    private String goodsGroup;

    private String topicUser;

    private String memberTopic;

    private String memberGroup;

    private String otherTopic;

    private String otherGroup;

    private String noticeTopic;

    private String noticeGroup;

    private String noticeSendTopic;

    private String noticeSendGroup;

    private String storeTopic;

    private String storeGroup;

    private String afterSaleTopic;

    private String afterSaleGroup;

    private String broadcastTopic;

    private String broadcastGroup;

	public String getPromotionTopic() {
		return promotionTopic;
	}

	public void setPromotionTopic(String promotionTopic) {
		this.promotionTopic = promotionTopic;
	}

	public String getPromotionGroup() {
		return promotionGroup;
	}

	public void setPromotionGroup(String promotionGroup) {
		this.promotionGroup = promotionGroup;
	}

	public String getOrderTopic() {
		return orderTopic;
	}

	public void setOrderTopic(String orderTopic) {
		this.orderTopic = orderTopic;
	}

	public String getOrderGroup() {
		return orderGroup;
	}

	public void setOrderGroup(String orderGroup) {
		this.orderGroup = orderGroup;
	}

	public String getMsgExtTopic() {
		return msgExtTopic;
	}

	public void setMsgExtTopic(String msgExtTopic) {
		this.msgExtTopic = msgExtTopic;
	}

	public String getMsgExtGroup() {
		return msgExtGroup;
	}

	public void setMsgExtGroup(String msgExtGroup) {
		this.msgExtGroup = msgExtGroup;
	}

	public String getGoodsTopic() {
		return goodsTopic;
	}

	public void setGoodsTopic(String goodsTopic) {
		this.goodsTopic = goodsTopic;
	}

	public String getGoodsGroup() {
		return goodsGroup;
	}

	public void setGoodsGroup(String goodsGroup) {
		this.goodsGroup = goodsGroup;
	}

	public String getTopicUser() {
		return topicUser;
	}

	public void setTopicUser(String topicUser) {
		this.topicUser = topicUser;
	}

	public String getMemberTopic() {
		return memberTopic;
	}

	public void setMemberTopic(String memberTopic) {
		this.memberTopic = memberTopic;
	}

	public String getMemberGroup() {
		return memberGroup;
	}

	public void setMemberGroup(String memberGroup) {
		this.memberGroup = memberGroup;
	}

	public String getOtherTopic() {
		return otherTopic;
	}

	public void setOtherTopic(String otherTopic) {
		this.otherTopic = otherTopic;
	}

	public String getOtherGroup() {
		return otherGroup;
	}

	public void setOtherGroup(String otherGroup) {
		this.otherGroup = otherGroup;
	}

	public String getNoticeTopic() {
		return noticeTopic;
	}

	public void setNoticeTopic(String noticeTopic) {
		this.noticeTopic = noticeTopic;
	}

	public String getNoticeGroup() {
		return noticeGroup;
	}

	public void setNoticeGroup(String noticeGroup) {
		this.noticeGroup = noticeGroup;
	}

	public String getNoticeSendTopic() {
		return noticeSendTopic;
	}

	public void setNoticeSendTopic(String noticeSendTopic) {
		this.noticeSendTopic = noticeSendTopic;
	}

	public String getNoticeSendGroup() {
		return noticeSendGroup;
	}

	public void setNoticeSendGroup(String noticeSendGroup) {
		this.noticeSendGroup = noticeSendGroup;
	}

	public String getStoreTopic() {
		return storeTopic;
	}

	public void setStoreTopic(String storeTopic) {
		this.storeTopic = storeTopic;
	}

	public String getStoreGroup() {
		return storeGroup;
	}

	public void setStoreGroup(String storeGroup) {
		this.storeGroup = storeGroup;
	}

	public String getAfterSaleTopic() {
		return afterSaleTopic;
	}

	public void setAfterSaleTopic(String afterSaleTopic) {
		this.afterSaleTopic = afterSaleTopic;
	}

	public String getAfterSaleGroup() {
		return afterSaleGroup;
	}

	public void setAfterSaleGroup(String afterSaleGroup) {
		this.afterSaleGroup = afterSaleGroup;
	}

	public String getBroadcastTopic() {
		return broadcastTopic;
	}

	public void setBroadcastTopic(String broadcastTopic) {
		this.broadcastTopic = broadcastTopic;
	}

	public String getBroadcastGroup() {
		return broadcastGroup;
	}

	public void setBroadcastGroup(String broadcastGroup) {
		this.broadcastGroup = broadcastGroup;
	}
}
