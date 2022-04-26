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
package com.taotao.cloud.logger.logRecord.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志记录属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:44:52
 */
@ConfigurationProperties(prefix = "log-record")
public class LogRecordProperties {

	/**
	 * 兔子mq属性
	 */
	private RabbitMqProperties rabbitMqProperties;

	/**
	 * 火箭mq属性
	 */
	private RocketMqProperties rocketMqProperties;

	/**
	 * 流
	 */
	private StreamProperties stream;

	/**
	 * 数据管道
	 */
	private String dataPipeline;

	/**
	 * 池大小
	 */
	private int poolSize = 4;

	/**
	 * 兔子mq属性
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-04-26 14:44:54
	 */
	public static class RabbitMqProperties {
		/**
		 * 宿主
		 */
		private String host;
		/**
		 * 港口
		 */
		private int port;
		/**
		 * 用户名
		 */
		private String username;
		/**
		 * 密码
		 */
		private String password;
		/**
		 * 队列名称
		 */
		private String queueName;
		/**
		 * 交换名字
		 */
		private String exchangeName;
		/**
		 * 路由关键
		 */
		private String routingKey;

		/**
		 * 获得主机
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:54
		 */
		public String getHost() {
		    return host;
	    }

		/**
		 * 设置主机
		 *
		 * @param host 宿主
		 * @since 2022-04-26 14:44:54
		 */
		public void setHost(String host) {
		    this.host = host;
	    }

		/**
		 * 得到港口
		 *
		 * @return int
		 * @since 2022-04-26 14:44:54
		 */
		public int getPort() {
		    return port;
	    }

		/**
		 * 设置端口
		 *
		 * @param port 港口
		 * @since 2022-04-26 14:44:54
		 */
		public void setPort(int port) {
		    this.port = port;
	    }

		/**
		 * 获得用户名
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:54
		 */
		public String getUsername() {
		    return username;
	    }

		/**
		 * 设置用户名
		 *
		 * @param username 用户名
		 * @since 2022-04-26 14:44:54
		 */
		public void setUsername(String username) {
		    this.username = username;
	    }

		/**
		 * 得到密码
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:55
		 */
		public String getPassword() {
		    return password;
	    }

		/**
		 * 设置密码
		 *
		 * @param password 密码
		 * @since 2022-04-26 14:44:55
		 */
		public void setPassword(String password) {
		    this.password = password;
	    }

		/**
		 * 获得队列名称
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:55
		 */
		public String getQueueName() {
		    return queueName;
	    }

		/**
		 * 设置队列名称
		 *
		 * @param queueName 队列名称
		 * @since 2022-04-26 14:44:55
		 */
		public void setQueueName(String queueName) {
		    this.queueName = queueName;
	    }

		/**
		 * 交换名字
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:55
		 */
		public String getExchangeName() {
		    return exchangeName;
	    }

		/**
		 * 设置交换名
		 *
		 * @param exchangeName 交换名字
		 * @since 2022-04-26 14:44:55
		 */
		public void setExchangeName(String exchangeName) {
		    this.exchangeName = exchangeName;
	    }

		/**
		 * 得到路由关键字
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:55
		 */
		public String getRoutingKey() {
		    return routingKey;
	    }

		/**
		 * 设置路由关键
		 *
		 * @param routingKey 路由关键
		 * @since 2022-04-26 14:44:55
		 */
		public void setRoutingKey(String routingKey) {
		    this.routingKey = routingKey;
	    }
    }

	/**
	 * 火箭mq属性
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-04-26 14:44:56
	 */
	public static class RocketMqProperties {
		/**
		 * 主题
		 */
		private String topic = "logRecord";
		/**
		 * 标签
		 */
		private String tag = "";
		/**
		 * namesrv addr
		 */
		private String namesrvAddr = "localhost:9876";
		/**
		 * 组名称
		 */
		private String groupName = "logRecord";
		/**
		 * 最大消息大小
		 */
		private int maxMessageSize = 4000000;
		/**
		 * 味精发送超时
		 */
		private int sendMsgTimeout = 3000;
		/**
		 * 重试发送失败时候
		 */
		private int retryTimesWhenSendFailed = 2;

		/**
		 * 得到话题
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:56
		 */
		public String getTopic() {
		    return topic;
	    }

		/**
		 * 设置主题
		 *
		 * @param topic 主题
		 * @since 2022-04-26 14:44:56
		 */
		public void setTopic(String topic) {
		    this.topic = topic;
	    }

		/**
		 * 得到标签
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:56
		 */
		public String getTag() {
		    return tag;
	    }

		/**
		 * 设置标签
		 *
		 * @param tag 标签
		 * @since 2022-04-26 14:44:56
		 */
		public void setTag(String tag) {
		    this.tag = tag;
	    }

		/**
		 * 得到namesrv addr
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:56
		 */
		public String getNamesrvAddr() {
		    return namesrvAddr;
	    }

		/**
		 * 设置namesrv addr
		 *
		 * @param namesrvAddr namesrv addr
		 * @since 2022-04-26 14:44:56
		 */
		public void setNamesrvAddr(String namesrvAddr) {
		    this.namesrvAddr = namesrvAddr;
	    }

		/**
		 * 把组名
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:56
		 */
		public String getGroupName() {
		    return groupName;
	    }

		/**
		 * 设置组名称
		 *
		 * @param groupName 组名称
		 * @since 2022-04-26 14:44:57
		 */
		public void setGroupName(String groupName) {
		    this.groupName = groupName;
	    }

		/**
		 * 得到最大消息大小
		 *
		 * @return int
		 * @since 2022-04-26 14:44:57
		 */
		public int getMaxMessageSize() {
		    return maxMessageSize;
	    }

		/**
		 * 设置最大消息大小
		 *
		 * @param maxMessageSize 最大消息大小
		 * @since 2022-04-26 14:44:57
		 */
		public void setMaxMessageSize(int maxMessageSize) {
		    this.maxMessageSize = maxMessageSize;
	    }

		/**
		 * 得到味精发送超时
		 *
		 * @return int
		 * @since 2022-04-26 14:44:57
		 */
		public int getSendMsgTimeout() {
		    return sendMsgTimeout;
	    }

		/**
		 * 设置发送味精超时
		 *
		 * @param sendMsgTimeout 味精发送超时
		 * @since 2022-04-26 14:44:57
		 */
		public void setSendMsgTimeout(int sendMsgTimeout) {
		    this.sendMsgTimeout = sendMsgTimeout;
	    }

		/**
		 * 得到重试发送失败时候
		 *
		 * @return int
		 * @since 2022-04-26 14:44:57
		 */
		public int getRetryTimesWhenSendFailed() {
		    return retryTimesWhenSendFailed;
	    }

		/**
		 * 设置重试发送失败时候
		 *
		 * @param retryTimesWhenSendFailed 重试发送失败时候
		 * @since 2022-04-26 14:44:57
		 */
		public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
		    this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
	    }
    }

	/**
	 * 流特性
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-04-26 14:44:58
	 */
	public static class StreamProperties {
		/**
		 * 目地
		 * 默认对应消息中间件topic rocketmq的topic, RabbitMq的 exchangeName
		 */
		private String destination;

		/**
		 * 集团
		 * 默认对应的分组
		 */
		private String group;

		/**
		 * 粘结剂
		 * 默认的binder（对应的消息中间件）
		 */
		private String binder;

		/**
		 * 得到目地
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:58
		 */
		public String getDestination() {
		    return destination;
	    }

		/**
		 * 设定目标
		 *
		 * @param destination 目地
		 * @since 2022-04-26 14:44:58
		 */
		public void setDestination(String destination) {
		    this.destination = destination;
	    }

		/**
		 * 获得集团
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:58
		 */
		public String getGroup() {
		    return group;
	    }

		/**
		 * 组群
		 *
		 * @param group 集团
		 * @since 2022-04-26 14:44:58
		 */
		public void setGroup(String group) {
		    this.group = group;
	    }

		/**
		 * 得到粘结剂
		 *
		 * @return {@link String }
		 * @since 2022-04-26 14:44:58
		 */
		public String getBinder() {
		    return binder;
	    }

		/**
		 * 集粘结剂
		 *
		 * @param binder 粘结剂
		 * @since 2022-04-26 14:44:58
		 */
		public void setBinder(String binder) {
		    this.binder = binder;
	    }
    }

	/**
	 * 让兔子mq属性
	 *
	 * @return {@link RabbitMqProperties }
	 * @since 2022-04-26 14:44:52
	 */
	public RabbitMqProperties getRabbitMqProperties() {
		return rabbitMqProperties;
	}

	/**
	 * 兔子mq属性设置
	 *
	 * @param rabbitMqProperties 兔子mq属性
	 * @since 2022-04-26 14:44:53
	 */
	public void setRabbitMqProperties(
		RabbitMqProperties rabbitMqProperties) {
		this.rabbitMqProperties = rabbitMqProperties;
	}

	/**
	 * 获取火箭mq属性
	 *
	 * @return {@link RocketMqProperties }
	 * @since 2022-04-26 14:44:53
	 */
	public RocketMqProperties getRocketMqProperties() {
		return rocketMqProperties;
	}

	/**
	 * 设置火箭mq属性
	 *
	 * @param rocketMqProperties 火箭mq属性
	 * @since 2022-04-26 14:44:53
	 */
	public void setRocketMqProperties(
		RocketMqProperties rocketMqProperties) {
		this.rocketMqProperties = rocketMqProperties;
	}

	/**
	 * 得到流
	 *
	 * @return {@link StreamProperties }
	 * @since 2022-04-26 14:44:53
	 */
	public StreamProperties getStream() {
		return stream;
	}

	/**
	 * 组流
	 *
	 * @param stream 流
	 * @since 2022-04-26 14:44:53
	 */
	public void setStream(
		StreamProperties stream) {
		this.stream = stream;
	}

	/**
	 * 获取数据管道
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:44:53
	 */
	public String getDataPipeline() {
		return dataPipeline;
	}

	/**
	 * 设置数据管道
	 *
	 * @param dataPipeline 数据管道
	 * @since 2022-04-26 14:44:53
	 */
	public void setDataPipeline(String dataPipeline) {
		this.dataPipeline = dataPipeline;
	}

	/**
	 * 得到池大小
	 *
	 * @return int
	 * @since 2022-04-26 14:44:53
	 */
	public int getPoolSize() {
		return poolSize;
	}

	/**
	 * 池大小设置
	 *
	 * @param poolSize 池大小
	 * @since 2022-04-26 14:44:54
	 */
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
}
