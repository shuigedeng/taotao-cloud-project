package com.taotao.cloud.web.exception.domain;


/**
 * 异常通知消息
 *
 * @author lingting 2020/6/12 16:07
 */
public class ExceptionMessage {

	/**
	 * 用于筛选重复异常
	 */
	private String key;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 数量
	 */
	private int number;

	/**
	 * 堆栈
	 */
	private String stack;

	/**
	 * 最新的触发时间
	 */
	private String time;

	/**
	 * 机器地址
	 */
	private String mac;

	/**
	 * 线程id
	 */
	private long threadId;

	/**
	 * 服务名
	 */
	private String applicationName;

	/**
	 * hostname
	 */
	private String hostname;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * 请求地址
	 */
	private String requestUri;

	/**
	 * 数量自增
	 */
	public ExceptionMessage increment() {
		number++;
		return this;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	@Override
	public String toString() {
		return "服务名称：" + applicationName + "\nip：" + ip + "\nhostname：" + hostname + "\n机器地址："
			+ mac + "\n触发时间：" + time
			+ "\n请求地址：" + requestUri + "\n线程id：" + threadId + "\n数量：" + number + "\n堆栈："
			+ stack;
	}

}
