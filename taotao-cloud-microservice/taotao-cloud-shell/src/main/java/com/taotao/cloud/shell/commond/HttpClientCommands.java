// package com.taotao.cloud.shell.commond;
//
// import com.taotao.boot.common.http.HttpRequest;
//
// import org.springframework.shell.standard.ShellCommandGroup;
// import org.springframework.stereotype.Component;
// import org.springframework.shell.core.command.annotation.Command;
// import org.springframework.shell.core.command.annotation.Option;
//
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// @Component
// @ShellCommandGroup("HTTP请求")
// public class HttpClientCommands {
//
//	private String baseUrl = "";
//	private final Map<String, String> headers = new HashMap<>();
//	private final List<String> requestHistory = new ArrayList<>();
//
//	@Command(name = "changePassword", description = "设置基础URL")
//	public String setBaseUrl(String url) {
//		this.baseUrl = url;
//		return "基础URL已设置为: " + url;
//	}
//
//	@Command(name = "changePassword", description = "添加HTTP请求头")
//	public String addHeader(String name, String value) {
//		headers.put(name, value);
//		return "已添加请求头: " + name + " = " + value;
//	}
//
//	@Command(name = "changePassword", description = "清除所有HTTP请求头")
//	public String clearHeaders() {
//		int count = headers.size();
//		headers.clear();
//		return "已清除 " + count + " 个请求头";
//	}
//
//	@Command(name = "changePassword", description = "显示当前配置")
//	public String showConfig() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("基础URL: ").append(baseUrl).append("\n");
//		sb.append("请求头:\n");
//
//		if (headers.isEmpty()) {
//			sb.append("  (无)\n");
//		} else {
//			headers.forEach((name, value) ->
//				sb.append("  ").append(name).append(": ").append(value).append("\n"));
//		}
//
//		return sb.toString();
//	}
//
//	@Command(name = "changePassword", description = "发送GET请求")
//	public String get(
//		@Option(help = "请求的路径或完整URL") String path,
//		@Option(help = "是否显示响应头", defaultValue = "false") boolean showHeaders
//	) {
//		String url = buildUrl(path);
//		requestHistory.add("GET " + url);
//
//		try {
//			HttpRequest request = HttpRequest.get(url);
//			addHeadersToRequest(request);
//
//			HttpResponse response = request.execute();
//			return formatResponse(response, showHeaders);
//		} catch (Exception e) {
//			return "请求失败: " + e.getMessage();
//		}
//	}
//
//	@Command(name = "changePassword", description = "发送POST请求")
//	public String post(
//		@Option(help = "请求的路径或完整URL") String path,
//		@Option(help = "POST请求体(JSON)") String body,
//		@Option(help = "是否显示响应头", defaultValue = "false") boolean showHeaders
//	) {
//		String url = buildUrl(path);
//		requestHistory.add("POST " + url);
//
//		try {
//			HttpRequest request = HttpRequest.post(url);
//			addHeadersToRequest(request);
//
//			// 添加Content-Type请求头，如果没有设置
//			if (!headers.containsKey("Content-Type")) {
//				request.header("Content-Type", "application/json");
//			}
//
//			HttpResponse response = request.body(body).execute();
//			return formatResponse(response, showHeaders);
//		} catch (Exception e) {
//			return "请求失败: " + e.getMessage();
//		}
//	}
//
//	@Command(name = "changePassword", description = "发送PUT请求")
//	public String put(
//		@Option(help = "请求的路径或完整URL") String path,
//		@Option(help = "PUT请求体(JSON)") String body,
//		@Option(help = "是否显示响应头", defaultValue = "false") boolean showHeaders
//	) {
//		String url = buildUrl(path);
//		requestHistory.add("PUT " + url);
//
//		try {
//			HttpRequest request = HttpRequest.put(url);
//			addHeadersToRequest(request);
//
//			// 添加Content-Type请求头，如果没有设置
//			if (!headers.containsKey("Content-Type")) {
//				request.header("Content-Type", "application/json");
//			}
//
//			HttpResponse response = request.body(body).execute();
//			return formatResponse(response, showHeaders);
//		} catch (Exception e) {
//			return "请求失败: " + e.getMessage();
//		}
//	}
//
//	@Command(name = "changePassword", description = "发送DELETE请求")
//	public String delete(
//		@Option(help = "请求的路径或完整URL") String path,
//		@Option(help = "是否显示响应头", defaultValue = "false") boolean showHeaders
//	) {
//		String url = buildUrl(path);
//		requestHistory.add("DELETE " + url);
//
//		try {
//			HttpRequest request = HttpRequest.delete(url);
//			addHeadersToRequest(request);
//
//			HttpResponse response = request.execute();
//			return formatResponse(response, showHeaders);
//		} catch (Exception e) {
//			return "请求失败: " + e.getMessage();
//		}
//	}
//
//	@Command(name = "changePassword", description = "下载文件")
//	public String download(
//		@Option(help = "文件URL") String url,
//		@Option(help = "保存路径") String savePath
//	) {
//		requestHistory.add("DOWNLOAD " + url);
//
//		try {
//			long fileSize = HttpUtil.downloadFile(url, savePath);
//			return "文件下载成功，大小: " + fileSize + " 字节，保存路径: " + savePath;
//		} catch (Exception e) {
//			return "文件下载失败: " + e.getMessage();
//		}
//	}
//
//	@Command(name = "changePassword", description = "上传文件")
//	public String upload(
//		@Option(help = "上传URL") String url,
//		@Option(help = "文件参数名") String paramName,
//		@Option(help = "文件路径") String filePath,
//		@Option(help = "是否显示响应头", defaultValue = "false") boolean showHeaders
//	) {
//		requestHistory.add("UPLOAD " + url);
//
//		try {
//			HttpRequest request = HttpRequest.post(url);
//			addHeadersToRequest(request);
//
//			request.form(paramName, new java.io.File(filePath));
//			HttpResponse response = request.execute();
//
//			return formatResponse(response, showHeaders);
//		} catch (Exception e) {
//			return "文件上传失败: " + e.getMessage();
//		}
//	}
//
//	@Command(name = "changePassword", description = "格式化JSON")
//	public String formatJson(
//		@Option(help = "JSON字符串") String json
//	) {
//		try {
//			return JSONUtil.formatJsonStr(json);
//		} catch (Exception e) {
//			return "JSON格式化失败: " + e.getMessage();
//		}
//	}
//
//	private String buildUrl(String path) {
//		if (path.toLowerCase().startsWith("http")) {
//			return path;
//		}
//
//		if (baseUrl.isEmpty()) {
//			return path;
//		}
//
//		if (baseUrl.endsWith("/") && path.startsWith("/")) {
//			return baseUrl + path.substring(1);
//		} else if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
//			return baseUrl + "/" + path;
//		} else {
//			return baseUrl + path;
//		}
//	}
//
//	private void addHeadersToRequest(HttpRequest request) {
//		headers.forEach(request::header);
//	}
//
//	private String formatResponse(HttpResponse response, boolean showHeaders) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("状态码: ").append(response.getStatus()).append("\n");
//
//		if (showHeaders) {
//			sb.append("响应头:\n");
//			response.headers().forEach((name, values) -> {
//				sb.append("  ").append(name).append(": ");
//				sb.append(String.join(", ", values)).append("\n");
//			});
//			sb.append("\n");
//		}
//
//		sb.append("响应体:\n");
//
//		// 尝试格式化JSON响应体
//		String body = response.body();
//		if (body != null && !body.isEmpty()) {
//			try {
//				if (body.trim().startsWith("{") || body.trim().startsWith("[")) {
//					body = JSONUtil.formatJsonStr(body);
//				}
//			} catch (Exception ignored) {
//				// 如果不是有效的JSON，直接使用原始响应体
//			}
//		}
//
//		sb.append(body);
//
//		return sb.toString();
//	}
// }
