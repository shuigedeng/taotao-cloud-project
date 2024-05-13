package com.taotao.cloud.rpc.registry.apiregistry.base;

import com.taotao.cloud.rpc.registry.apiregistry.RequestInfo;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @ApiClient解析器
 */
public class BaseApiClientParser {
    public static class ApiClientParserInfo{
        String appName;
        String url;
        Method method;
        ProceedingJoinPoint joinPoint;

		public ApiClientParserInfo(String appName, String url, Method method, ProceedingJoinPoint joinPoint) {
			this.appName = appName;
			this.url = url;
			this.method = method;
			this.joinPoint = joinPoint;
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public ProceedingJoinPoint getJoinPoint() {
			return joinPoint;
		}

		public void setJoinPoint(ProceedingJoinPoint joinPoint) {
			this.joinPoint = joinPoint;
		}
	}
    public RequestInfo parse(ApiClientParserInfo info){
        return null;
    }
}
