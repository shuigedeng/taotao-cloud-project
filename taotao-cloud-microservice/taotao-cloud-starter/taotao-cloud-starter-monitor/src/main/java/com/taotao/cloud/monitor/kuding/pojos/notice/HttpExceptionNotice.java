package com.taotao.cloud.monitor.kuding.pojos.notice;

import com.taotao.cloud.monitor.kuding.pojos.notice.ExceptionNotice;
import com.taotao.cloud.monitor.kuding.properties.enums.ProjectEnviroment;

import static java.util.stream.Collectors.toList;

import java.time.format.DateTimeFormatter;
import java.util.Map;


public class HttpExceptionNotice extends ExceptionNotice {

	protected String url;

	protected Map<String, String> paramInfo;

	protected String requestBody;

	protected Map<String, String> headers;

	public HttpExceptionNotice(RuntimeException exception, String filter, String url, Map<String, String> param,
							   String requestBody, Map<String, String> headers, ProjectEnviroment projectEnviroment, String title) {
		super(exception, filter, null, projectEnviroment, title);
		this.url = url;
		this.paramInfo = param;
		this.requestBody = requestBody;
		this.headers = headers;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the paramInfo
	 */
	public Map<String, String> getParamInfo() {
		return paramInfo;
	}

	/**
	 * @param paramInfo the paramInfo to set
	 */
	public void setParamInfo(Map<String, String> paramInfo) {
		this.paramInfo = paramInfo;
	}

	/**
	 * @return the requestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}

	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kuding.content.ExceptionNotice#createText()
	 */
	@Override
	public String createText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("工程信息：").append(project).append("(").append(projectEnviroment.getName()).append(")")
				.append("\r\n");
		stringBuilder.append("接口地址：").append(url).append("\r\n");
		if (paramInfo != null && paramInfo.size() > 0) {
			stringBuilder.append("接口参数：").append("\r\n")
					.append(String.join("\r\r", paramInfo.entrySet().stream()
							.map(x -> String.format("%s::%s", x.getKey(), x.getValue())).collect(toList())))
					.append("\r\n");
		}
		if (requestBody != null) {
			stringBuilder.append("请求体数据：").append(requestBody).append("\r\n");
		}
		if (headers != null && headers.size() > 0) {
			stringBuilder.append("请求头：").append("\r\n");
			stringBuilder.append(String.join(",\t", headers.entrySet().stream()
					.map(x -> String.format("%s::%s", x.getKey(), x.getValue())).collect(toList())));
			stringBuilder.append("\r\n");
		}
		stringBuilder.append("类路径：").append(classPath).append("\r\n");
		stringBuilder.append("方法名：").append(methodName).append("\r\n");
		if (parames != null && parames.size() > 0) {
			stringBuilder.append("参数信息：")
					.append(String.join("\t,\t", parames.stream().map(x -> x.toString()).collect(toList())))
					.append("\r\n");
		}
		stringBuilder.append("异常信息：").append(String.join("\r\n caused by: ", exceptionMessage)).append("\r\n");
		stringBuilder.append("异常追踪：").append("\r\n").append(String.join("\r\n", traceInfo)).append("\r\n");
		stringBuilder.append("最后一次出现时间：").append(createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.append("\r\n");
		stringBuilder.append("出现次数：").append(showCount).append("\r\n");
		return stringBuilder.toString();
	}

}
