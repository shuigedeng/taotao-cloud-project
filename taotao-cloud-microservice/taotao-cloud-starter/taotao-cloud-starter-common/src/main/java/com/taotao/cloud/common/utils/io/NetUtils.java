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

package com.taotao.cloud.common.utils.io;

import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.common.RegexUtils;
import com.taotao.cloud.common.utils.exception.ExceptionUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * INet 相关工具
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class NetUtils {

	public static final String LOCAL_LOOPBACK_HOST = "127.0.0.1";

	/**
	 * 获取 服务器 hostname
	 *
	 * @return hostname
	 */
	public static String getHostName() {
		String hostname;
		try {
			InetAddress address = InetAddress.getLocalHost();
			// force a best effort reverse DNS lookup
			hostname = address.getHostName();
			if (!StringUtils.hasText(hostname)) {
				hostname = address.toString();
			}
		} catch (UnknownHostException ignore) {
			hostname = LOCAL_LOOPBACK_HOST;
		}
		return hostname;
	}

	/**
	 * 获取 服务器 HostIp
	 *
	 * @return HostIp
	 */
	public static String getHostIp() {
		String hostAddress;
		try {
			InetAddress address = NetUtils.getLocalHostLanAddress();
			// force a best effort reverse DNS lookup
			hostAddress = address.getHostAddress();
			if (!StringUtils.hasText(hostAddress)) {
				hostAddress = address.toString();
			}
		} catch (UnknownHostException ignore) {
			hostAddress = LOCAL_LOOPBACK_HOST;
		}
		return hostAddress;
	}

	/**
	 * https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
	 *
	 * <p>
	 * Returns an <code>InetAddress</code> object encapsulating what is most likely the machine's
	 * LAN IP address.
	 * <p/>
	 * This method is intended for use as a replacement of JDK method <code>InetAddress.getLocalHost</code>,
	 * because that method is ambiguous on Linux systems. Linux systems enumerate the loopback
	 * network interface the same way as regular LAN network interfaces, but the JDK
	 * <code>InetAddress.getLocalHost</code> method does not specify the algorithm used to select
	 * the address returned under such circumstances, and will often return the loopback address,
	 * which is not valid for network communication. Details
	 * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
	 * <p/>
	 * This method will scan all IP addresses on all network interfaces on the host machine to
	 * determine the IP address most likely to be the machine's LAN address. If the machine has
	 * multiple IP addresses, this method will prefer a site-local IP address (e.g. 192.168.x.x or
	 * 10.10.x.x, usually IPv4) if the machine has one (and will return the first site-local address
	 * if the machine has more than one), but if the machine does not hold a site-local address,
	 * this method will return simply the first non-loopback address found (IPv4 or IPv6).
	 * <p/>
	 * If this method cannot find a non-loopback address using this selection algorithm, it will
	 * fall back to calling and returning the result of JDK method <code>InetAddress.getLocalHost</code>.
	 * <p/>
	 *
	 * @throws UnknownHostException If the LAN address of the machine cannot be found.
	 */
	private static InetAddress getLocalHostLanAddress() throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			// Iterate all NICs (network interface cards)...
			for (Enumeration<NetworkInterface> iFaces = NetworkInterface.getNetworkInterfaces();
				iFaces.hasMoreElements(); ) {
				NetworkInterface iFace = iFaces.nextElement();
				// Iterate all IP addresses assigned to each card...
				for (Enumeration<InetAddress> inetAdders = iFace.getInetAddresses();
					inetAdders.hasMoreElements(); ) {
					InetAddress inetAddr = inetAdders.nextElement();
					if (!inetAddr.isLoopbackAddress()) {
						if (inetAddr.isSiteLocalAddress()) {
							// Found non-loopback site-local address. Return it immediately...
							return inetAddr;
						} else if (candidateAddress == null) {
							// Found non-loopback address, but not necessarily site-local.
							// Store it as a candidate to be returned if site-local address is not subsequently found...
							candidateAddress = inetAddr;
							// Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
							// only the first. For subsequent iterations, candidate will be non-null.
						}
					}
				}
			}
			if (candidateAddress != null) {
				// We did not find a site-local address, but we found some other non-loopback address.
				// Server might have a non-site-local address assigned to its NIC (or it might be running
				// IPv6 which deprecates the "site-local" concept).
				// Return this non-loopback candidate address...
				return candidateAddress;
			}
			// At this point, we did not find a non-loopback address.
			// Fall back to returning whatever InetAddress.getLocalHost() returns...
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException(
					"The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		} catch (Exception e) {
			UnknownHostException unknownHostException = new UnknownHostException(
				"Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}

	/**
	 * 尝试端口时候被占用
	 *
	 * @param port 端口号
	 * @return 没有被占用：true,被占用：false
	 */
	public static boolean tryPort(int port) {
		try (ServerSocket ignore = new ServerSocket(port)) {
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将 ip 转成 InetAddress
	 *
	 * @param ip ip
	 * @return InetAddress
	 */
	public static InetAddress getInetAddress(String ip) {
		try {
			return InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 判断是否内网 ip
	 *
	 * @param ip ip
	 * @return boolean
	 */
	public static boolean isInternalIp(String ip) {
		return isInternalIp(getInetAddress(ip));
	}

	/**
	 * 判断是否内网 ip
	 *
	 * @param address InetAddress
	 * @return boolean
	 */
	public static boolean isInternalIp(InetAddress address) {
		if (isLocalIp(address)) {
			return true;
		}
		return isInternalIp(address.getAddress());
	}

	/**
	 * 判断是否本地 ip
	 *
	 * @param address InetAddress
	 * @return boolean
	 */
	public static boolean isLocalIp(InetAddress address) {
		return address.isAnyLocalAddress()
			|| address.isLoopbackAddress()
			|| address.isSiteLocalAddress();
	}

	/**
	 * 判断是否内网 ip
	 *
	 * @param addr ip
	 * @return boolean
	 */
	public static boolean isInternalIp(byte[] addr) {
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		//10.x.x.x/8
		final byte section1 = 0x0A;
		//172.16.x.x/12
		final byte section2 = (byte) 0xAC;
		final byte section3 = (byte) 0x10;
		final byte section4 = (byte) 0x1F;
		//192.168.x.x/16
		final byte section5 = (byte) 0xC0;
		final byte section6 = (byte) 0xA8;
		switch (b0) {
			case section1:
				return true;
			case section2:
				if (b1 >= section3 && b1 <= section4) {
					return true;
				}
			case section5:
				if (b1 == section6) {
					return true;
				}
			default:
				return false;
		}
	}
	/**
	 * 默认的本地地址
	 */
	public static final String LOCALHOST = "127.0.0.1";

	/**
	 * 本地服务地址
	 */
	private static final String LOCAL_HOST;

	static {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new CommonRuntimeException(e);
		}

		LOCAL_HOST = address.getHostAddress();
	}

	/**
	 * 获取本地 ip 地址
	 * @return ip 地址
	 */
	public static String getLocalLoopbackHost() {
		return LOCAL_LOOPBACK_HOST;
	}

	/**
	 * 传入需要连接的IP，返回是否连接成功
	 *
	 * @param remoteInetAddress 远程地址
	 * @return {@code true} 是
	 */
	public static boolean isReachable(String remoteInetAddress) {
		return isReachable(remoteInetAddress, 5000);
	}

	/**
	 * 传入需要连接的IP，返回是否连接成功
	 * 注意：如果出现异常，只是简单的进行错误信息忽略
	 * @param remoteInetAddress 远程地址
	 * @param timeoutInMills   超时时间(milliseconds)
	 * @return {@code true} 是
	 */
	public static boolean isReachable(String remoteInetAddress, int timeoutInMills) {
		boolean reachable = false;
		try {
			InetAddress address = InetAddress.getByName(remoteInetAddress);
			reachable = address.isReachable(timeoutInMills);
		} catch (Exception e) {
			//ignore error
		}
		return reachable;
	}

	/**
	 * 断言处于联网状态
	 * 1. 这里可能会有一个问题，如果用户的 hosts 文件中有这个网址
	 */
	public static void assertOnLine() {
		final String address = "translate.google.cn";
		try {
			InetAddress inetAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			throw new RuntimeException("The net work is broken, " +
				"check your network or set isCommentWhenNetworkBroken=true.");
		}
	}

	/**
	 * 获取当前机器的 IP 地址
	 * @return IP 地址
	 */
	public static String getLocalIp() {
		InetAddress inetAddress = findLocalAddress();
		if(inetAddress == null) {
			return null;
		}

		String ip = inetAddress.getHostAddress();
		if(RegexUtils.isIp(ip)) {
			return ip;
		}

		return null;
	}

	/**
	 * 查询本地地址
	 * @return 地址
	 */
	private static InetAddress findLocalAddress() {
		String preferNamePrefix = "bond0";
		String defaultNicList = "bond0,eth0,em0,en0,em1,br0,eth1,em2,en1,eth2,em3,en2,eth3,em4,en3";

		InetAddress resultAddress = null;
		Map<String, NetworkInterface> candidateInterfaces = new HashMap<>();

		// 遍历所有网卡，找出所有可用网卡，尝试找出符合prefer前缀的网卡
		try {
			for (Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces(); allInterfaces
				.hasMoreElements(); ) {
				NetworkInterface nic = allInterfaces.nextElement();
				// 检查网卡可用并支持广播
				if (!nic.isUp() || !nic.supportsMulticast()) {
					continue;
				}

				// 检查是否符合prefer前缀
				String name = nic.getName();
				if (name.startsWith(preferNamePrefix)) {
					resultAddress = findAvailableAddress(nic);
					if (resultAddress != null) {
						return resultAddress;
					}
				} else {
					// 不是Prefer前缀，先放入可选列表
					candidateInterfaces.put(name, nic);
				}
			}

			for (String nifName : defaultNicList.split(PunctuationConst.COMMA)) {
				NetworkInterface nic = candidateInterfaces.get(nifName);
				if (nic != null) {
					resultAddress = findAvailableAddress(nic);
					if (resultAddress != null) {
						return resultAddress;
					}
				}
			}

			return null;
		} catch (SocketException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 检查有否非ipv6，非127.0.0.1
	 */
	private static InetAddress findAvailableAddress(NetworkInterface nic) {
		for (Enumeration<InetAddress> inetAddresses = nic.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
			InetAddress inetAddress = inetAddresses.nextElement();
			if (!(inetAddress instanceof Inet6Address) && !inetAddress.isLoopbackAddress()) {
				return inetAddress;
			}
		}
		return null;
	}
}
