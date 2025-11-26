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

package com.taotao.cloud.operation.biz.util; // package com.taotao.cloud.message.biz.util;
//
// import com.alibaba.fastjson2.JSON;
// import com.taotao.boot.common.enums.ClientTypeEnum;
// import com.taotao.boot.common.enums.ResultEnum;
// import com.taotao.boot.common.exception.BusinessException;
// import com.taotao.boot.common.utils.log.LogUtils;
// import com.taotao.cloud.message.biz.entity.ShortLink;
// import com.taotao.cloud.message.biz.service.ShortLinkService;
// import lombok.SneakyThrows;
// import org.apache.http.HttpEntity;
// import org.apache.http.HttpResponse;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.StringEntity;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.http.protocol.HTTP;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.util.Base64;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// /**
//  * 微信API交互token
//  */
//
// @Component
// public class WechatMpCodeUtil {
//
// 	private static String UN_LIMIT_API =
// "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";
// 	private static String CREATE_QR_CODE =
// "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=";
//
// 	@Autowired
// 	private WechatAccessTokenUtil wechatAccessTokenUtil;
// 	@Autowired
// 	private ShortLinkService shortLinkService;
//
// 	/**
// 	 * 生成分享二维码
// 	 *
// 	 * @param path 路径
// 	 * @return
// 	 */
// 	public String createQrCode(String path) {
// 		try {
// 			String accessToken = wechatAccessTokenUtil.cgiAccessToken(ClientTypeEnum.WECHAT_MP);
// 			Map<String, String> params = new HashMap<>(2);
// 			params.put("path", path);
// 			params.put("width", "280");
//
// 			//======================================================================//
// 			//执行URL Post调用
// 			//======================================================================//
// 			CloseableHttpClient httpClient = HttpClients.createDefault();
// 			HttpPost httpPost = new HttpPost(CREATE_QR_CODE + accessToken);
// 			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
// 			//必须是json模式的 post
// 			String body = JSON.toJSONString(params);
// 			StringEntity entity = new StringEntity(body);
// 			entity.setContentType("image/png");
// 			httpPost.setEntity(entity);
// 			HttpResponse httpResponse = httpClient.execute(httpPost);
// 			HttpEntity httpEntity = httpResponse.getEntity();
// 			//======================================================================//
// 			//处理HTTP返回结果
// 			//======================================================================//
// 			InputStream contentStream = httpEntity.getContent();
// 			byte[] bytes = toByteArray(contentStream);
// 			contentStream.read(bytes);
// 			//返回内容
// 			return Base64.getEncoder().encodeToString(bytes);
// 		} catch (Exception e) {
// 			LogUtils.error("生成二维码错误：", e);
// 			throw new BusinessException(ResultEnum.WECHAT_QRCODE_ERROR);
// 		}
//
// 	}
//
// 	/**
// 	 * 生成分享二维码
// 	 *
// 	 * @param page
// 	 * @param scene
// 	 * @return
// 	 */
// 	public String createCode(String page, String scene) {
// 		try {
//
// 			//短链接存储
// 			ShortLink shortLink = new ShortLink();
// 			shortLink.setOriginalParams(scene);
// 			List<ShortLink> shortLinks = shortLinkService.queryShortLinks(shortLink);
// 			if (shortLinks.size() > 0) {
// 				shortLink = shortLinks.get(0);
// 			} else {
// 				shortLink.setOriginalParams(scene);
// 				shortLinkService.save(shortLink);
// 				shortLink = shortLinkService.queryShortLinks(shortLink).get(0);
// 			}
// 			String accessToken = wechatAccessTokenUtil.cgiAccessToken(ClientTypeEnum.WECHAT_MP);
// 			Map<String, String> params = new HashMap<>(4);
// 			params.put("page", page);
// 			params.put("scene", String.valueOf(shortLink.getId()));
// 			params.put("width", "280");
//
// 			//======================================================================//
// 			//执行URL Post调用
// 			//======================================================================//
// 			CloseableHttpClient httpClient = HttpClients.createDefault();
// 			HttpPost httpPost = new HttpPost(UN_LIMIT_API + accessToken);
// 			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
// 			//必须是json模式的 post
// 			String body = JSON.toJSONString(params);
// 			StringEntity entity = new StringEntity(body);
// 			entity.setContentType("image/png");
// 			httpPost.setEntity(entity);
// 			HttpResponse httpResponse = httpClient.execute(httpPost);
// 			HttpEntity httpEntity = httpResponse.getEntity();
// 			//======================================================================//
// 			//处理HTTP返回结果
// 			//======================================================================//
// 			InputStream contentStream = httpEntity.getContent();
// 			byte[] bytes = toByteArray(contentStream);
// 			contentStream.read(bytes);
// 			//返回内容
// 			return Base64.getEncoder().encodeToString(bytes);
// 		} catch (Exception e) {
// 			LogUtils.error("生成二维码错误：", e);
// 			throw new BusinessException(ResultEnum.WECHAT_QRCODE_ERROR);
// 		}
//
// 	}
//
// 	/**
// 	 * @param input
// 	 * @return
// 	 * @throws IOException
// 	 */
// 	@SneakyThrows
// 	public static byte[] toByteArray(InputStream input) {
// 		ByteArrayOutputStream output = new ByteArrayOutputStream();
// 		copy(input, output);
// 		return output.toByteArray();
// 	}
//
// 	/**
// 	 * @param input
// 	 * @param output
// 	 * @return
// 	 * @throws IOException
// 	 */
// 	public static int copy(InputStream input, OutputStream output) throws IOException {
// 		long count = copyLarge(input, output);
// 		if (count > 2147483647L) {
// 			return -1;
// 		}
// 		return (int) count;
// 	}
//
// 	/**
// 	 * @param input
// 	 * @param output
// 	 * @return
// 	 * @throws IOException
// 	 */
// 	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
// 		byte[] buffer = new byte[4096];
// 		long count = 0L;
// 		int n = 0;
// 		while (-1 != (n = input.read(buffer))) {
// 			output.write(buffer, 0, n);
// 			count += n;
// 		}
// 		return count;
// 	}
//
//
// }
