package com.taotao.cloud.ip2region.utils;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.taotao.cloud.common.utils.log.LogUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * 获取客户端IP地址
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:29:10
 */
public class IpGeoUtils {

	/**
	 * 获取客户端IP地址
	 * <p>
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 *
	 * @param request 请求
	 * @return {@link String }
	 * @since 2022-04-27 17:29:10
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = null;
		try {
			ip = request.getHeader("x-forwarded-for");
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} catch (Exception e) {
			LogUtil.error("IPUtils ERROR ", e);
		}
		return ip;
	}

	/**
	 * 获得国家
	 *
	 * @param reader
	 * @param ip
	 * @return {@link String }
	 * @since 2022-04-27 17:29:10
	 */
	public static String getCountry(DatabaseReader reader, String ip) throws Exception {
		return reader.city(InetAddress.getByName(ip)).getCountry().getNames().get("zh-CN");
	}

	/**
	 * 获得省
	 *
	 * @param reader
	 * @param ip
	 * @return {@link String }
	 * @since 2022-04-27 17:29:10
	 */
	public static String getProvince(DatabaseReader reader, String ip) throws Exception {
		return reader.city(InetAddress.getByName(ip)).getMostSpecificSubdivision().getNames()
			.get("zh-CN");
	}

	/**
	 * 得到城市
	 *
	 * @param reader
	 * @param ip
	 * @return {@link String }
	 * @since 2022-04-27 17:29:10
	 */
	public static String getCity(DatabaseReader reader, String ip) throws Exception {
		return reader.city(InetAddress.getByName(ip)).getCity().getNames().get("zh-CN");
	}

	/**
	 * 获得经度
	 *
	 * @param reader
	 * @param ip
	 * @return {@link Double }
	 * @since 2022-04-27 17:29:11
	 */
	public static Double getLongitude(DatabaseReader reader, String ip) throws Exception {
//        return reader.city(InetAddress.getByName(ip)).getLocation().getLongitude();
		double data = 0;
		try {
			data = reader.city(InetAddress.getByName(ip)).getLocation().getLongitude();
		} catch (Exception e) {
		}
		return data;
	}

	/**
	 * 得到自由
	 *
	 * @param reader
	 * @param ip
	 * @return {@link Double }
	 * @since 2022-04-27 17:29:11
	 */
	public static Double getLatitude(DatabaseReader reader, String ip) {
		double data = 0;
		try {
			data = reader.city(InetAddress.getByName(ip)).getLocation().getLatitude();
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return data;
	}

	//public static void main(String[] args) throws Exception {
	//	//ResourceLoader resourceLoader = new DefaultResourceLoader();
	//	//Resource resource = resourceLoader.getResource(
	//	//	ResourceUtils.getURL("classpath:ip2region/ip2region.db").getPath());
	//
	//	File file = ResourceUtils.getFile("classpath:ip2region/ip2region.db");
	//	DbConfig config = new DbConfig();
	//	try (InputStream inputStream = new FileInputStream(file)) {
	//		DbSearcher searcher = new DbSearcher(config,
	//			new ByteArrayDBReader(StreamUtils.copyToByteArray(inputStream)));
	//		IpInfo ipInfo = IpInfoUtil.toIpInfo(searcher.memorySearch("218.70.86.250"));
	//		System.out.println(ipInfo);
	//	}
	//
	//	// String path = req.getSession().getServletContext().getRealPath("/WEB-INF/classes/GeoLite2-City.mmdb");
	//	String path = ResourceUtils.getURL("classpath:geolite2/GeoLite2-City.mmdb").getPath();
	//	// 创建 GeoLite2 数据库
	//	File database = new File(path);
	//	// 读取数据库内容
	//	DatabaseReader reader = new DatabaseReader.Builder(database).build();
	//	// 访问IP
	//	String ip = "218.70.86.250";
	//	String site =
	//		IpGeoUtils.getCountry(reader, ip) + "-" + IpGeoUtils.getLongitude(reader, ip) + "-"
	//			+ IpGeoUtils.getLatitude(reader, ip);
	//
	//	System.out.println(site);
	//
	//	Double lon = IpGeoUtils.getLongitude(reader, ip);
	//	double lat = IpGeoUtils.getLatitude(reader, ip);
	//	System.out.println("Lon:" + lon + "---Lat:" + lat);
	//}
}
