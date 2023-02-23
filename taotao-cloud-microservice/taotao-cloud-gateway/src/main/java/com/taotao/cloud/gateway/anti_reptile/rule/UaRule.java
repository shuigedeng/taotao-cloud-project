package com.taotao.cloud.gateway.anti_reptile.rule;

import com.taotao.cloud.gateway.anti_reptile.AntiReptileProperties;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;

public class UaRule extends AbstractRule {

	@Autowired
	private AntiReptileProperties properties;

	@Override
	protected boolean doExecute(ServerWebExchange exchange) {
		AntiReptileProperties.UaRule uaRule = properties.getUaRule();
		String requestUrl = exchange.getRequest().getURI().getRawPath();

		UserAgent userAgent = UserAgent.parseUserAgentString(
			exchange.getRequest().getHeaders().get("User-Agent").get(0));
		OperatingSystem os = userAgent.getOperatingSystem();
		OperatingSystem osGroup = userAgent.getOperatingSystem().getGroup();
		DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();

		if (DeviceType.UNKNOWN.equals(deviceType)) {
			System.out.println("Intercepted request, uri: " + requestUrl
				+ " Unknown device, User-Agent: " + userAgent.toString());
			return true;
		} else if (OperatingSystem.UNKNOWN.equals(os)
			|| OperatingSystem.UNKNOWN_MOBILE.equals(os)
			|| OperatingSystem.UNKNOWN_TABLET.equals(os)) {
			System.out.println("Intercepted request, uri: " + requestUrl
				+ " Unknown OperatingSystem, User-Agent: " + userAgent.toString());
			return true;
		}

		if (!uaRule.isAllowedLinux() && (OperatingSystem.LINUX.equals(osGroup)
			|| OperatingSystem.LINUX.equals(os))) {
			System.out.println("Intercepted request, uri: " + requestUrl
				+ " Not Allowed Linux request, User-Agent: " + userAgent.toString());
			return true;
		}

		if (!uaRule.isAllowedMobile() && (DeviceType.MOBILE.equals(deviceType)
			|| DeviceType.TABLET.equals(deviceType))) {
			System.out.println("Intercepted request, uri: " + requestUrl
				+ " Not Allowed Mobile Device request, User-Agent: " + userAgent.toString());
			return true;
		}

		if (!uaRule.isAllowedPc() && DeviceType.COMPUTER.equals(deviceType)) {
			System.out.println("Intercepted request, uri: " + requestUrl
				+ " Not Allowed PC request, User-Agent: " + userAgent.toString());
			return true;
		}

		if (!uaRule.isAllowedIot() && (DeviceType.DMR.equals(deviceType)
			|| DeviceType.GAME_CONSOLE.equals(deviceType) || DeviceType.WEARABLE.equals(
			deviceType))) {
			System.out.println("Intercepted request, uri: " + requestUrl
				+ " Not Allowed Iot Device request, User-Agent: " + userAgent.toString());
			return true;
		}

		if (!uaRule.isAllowedProxy() && OperatingSystem.PROXY.equals(os)) {
			System.out.println("Intercepted request, uri: " + requestUrl
				+ " Not Allowed Proxy request, User-Agent: " + userAgent.toString());
			return true;
		}
		return false;
	}

	@Override
	public void reset(ServerWebExchange exchange, String realRequestUri) {
		return;
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
