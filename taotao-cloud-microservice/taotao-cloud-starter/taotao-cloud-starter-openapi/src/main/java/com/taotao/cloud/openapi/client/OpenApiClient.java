package com.taotao.cloud.openapi.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.openapi.common.constant.Constant;
import com.taotao.cloud.openapi.common.constant.Header;
import com.taotao.cloud.openapi.common.enums.AsymmetricCryEnum;
import com.taotao.cloud.openapi.common.enums.CryModeEnum;
import com.taotao.cloud.openapi.common.enums.DataType;
import com.taotao.cloud.openapi.common.enums.SymmetricCryEnum;
import com.taotao.cloud.openapi.common.exception.OpenApiClientException;
import com.taotao.cloud.openapi.common.handler.AsymmetricCryHandler;
import com.taotao.cloud.openapi.common.handler.SymmetricCryHandler;
import com.taotao.cloud.openapi.common.model.Binary;
import com.taotao.cloud.openapi.common.model.BinaryParam;
import com.taotao.cloud.openapi.common.model.InParams;
import com.taotao.cloud.openapi.common.model.OutParams;
import com.taotao.cloud.openapi.common.util.Base64Util;
import com.taotao.cloud.openapi.common.util.BinaryUtil;
import com.taotao.cloud.openapi.common.util.CommonUtil;
import com.taotao.cloud.openapi.common.util.CompressUtil;
import com.taotao.cloud.openapi.common.util.StrObjectConvert;
import com.taotao.cloud.openapi.common.util.SymmetricCryUtil;
import com.taotao.cloud.openapi.common.util.TypeUtil;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对外开放api客户端 注：推荐使用{@link OpenApiClientBuilder}构建对象
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:05:30
 */
public class OpenApiClient {

	/**
	 * openapi基础路径,例如(http://localhost)
	 */
	private final String baseUrl;

	/**
	 * 本系统私钥
	 */
	private final String selfPrivateKey;

	/**
	 * 远程系统的公钥
	 */
	private final String remotePublicKey;

	/**
	 * 非对称加密算法
	 */
	private final AsymmetricCryEnum asymmetricCryEnum;

	/**
	 * 返回值是否需要解密
	 */
	private final boolean retDecrypt;

	/**
	 * 加密模式
	 */
	private final CryModeEnum cryModeEnum;

	/**
	 * 对称加密算法
	 */
	private final SymmetricCryEnum symmetricCryEnum;

	/**
	 * 调用者ID
	 */
	private final String callerId;

	/**
	 * API接口名称
	 */
	private final String api;

	/**
	 * 非对称加密处理器
	 */
	private final AsymmetricCryHandler asymmetricCryHandler;

	/**
	 * 对称加密处理器
	 */
	private final SymmetricCryHandler symmetricCryHandler;

	/**
	 * HTTP建立连接超时时间（单位秒）
	 */
	private final int httpConnectionTimeout;

	/**
	 * HTTP数据传输超时时间（单位秒）
	 */
	private final int httpReadTimeout;

	/**
	 * HTTP请求代理域名
	 */
	private final String httpProxyHost;

	/**
	 * HTTP请求代理端口
	 */
	private final Integer httpProxyPort;

	/**
	 * 是否启用压缩
	 */
	private boolean enableCompress;

	/**
	 * 日志前缀
	 */
	private final ThreadLocal<String> logPrefix = new ThreadLocal<>();


	/**
	 * openapi客户端
	 *
	 * @param baseUrl               openapi基础路径
	 * @param selfPrivateKey        本系统私钥
	 * @param remotePublicKey       远程系统的公钥
	 * @param asymmetricCryEnum     非对称加密算法
	 * @param retDecrypt            返回值是否需要解密
	 * @param cryModeEnum           加密模式
	 * @param symmetricCryEnum      对称加密算法
	 * @param callerId              调用者ID
	 * @param api                   接口名称
	 * @param httpConnectionTimeout HTTP建立连接超时时间（单位秒）
	 * @param httpReadTimeout       HTTP数据传输超时时间（单位秒）
	 * @param httpProxyHost         HTTP请求代理域名
	 * @param httpProxyPort         HTTP请求代理端口
	 * @param enableCompress        是否启用压缩
	 */
	public OpenApiClient(String baseUrl, String selfPrivateKey, String remotePublicKey,
		AsymmetricCryEnum asymmetricCryEnum,
		boolean retDecrypt, CryModeEnum cryModeEnum, SymmetricCryEnum symmetricCryEnum,
		String callerId, String api,
		int httpConnectionTimeout, int httpReadTimeout, String httpProxyHost, Integer httpProxyPort,
		boolean enableCompress) {
		this.baseUrl = baseUrl;
		this.selfPrivateKey = selfPrivateKey;
		this.remotePublicKey = remotePublicKey;
		this.asymmetricCryEnum = asymmetricCryEnum;
		this.retDecrypt = retDecrypt;
		this.cryModeEnum = cryModeEnum;
		this.symmetricCryEnum = symmetricCryEnum;
		this.callerId = callerId;
		this.api = api;
		this.asymmetricCryHandler = AsymmetricCryHandler.handlerMap.get(asymmetricCryEnum);
		this.symmetricCryHandler = SymmetricCryHandler.handlerMap.get(symmetricCryEnum);
		this.httpConnectionTimeout = httpConnectionTimeout;
		this.httpReadTimeout = httpReadTimeout;
		this.httpProxyHost = httpProxyHost;
		this.httpProxyPort = httpProxyPort;
		this.enableCompress = enableCompress;

		//初始化信息打印
		if (LogUtils.isDebugEnabled()) {
			LogUtils.debug("OpenApiClient init:{}", this);
			logCryModel(this.cryModeEnum);
		}
		//重要日志改成info级别
		LogUtils.info("OpenApiClient init succeed. hashcode={}", this.hashCode());
	}

	/**
	 * 调用openapi 注：推荐使用其它重载方法
	 *
	 * @param inParams 入参
	 * @return 返回值
	 */
	public OutParams callOpenApi(InParams inParams) {
		//再次检查入参，可能有直接调用此函数的
		checkInParams(inParams.getCallerId(), inParams.getApi(), inParams.getMethod());

		//没有设置uuid则给设置一个
		if (StrUtil.isBlank(inParams.getUuid())) {
			inParams.setUuid(IdUtil.simpleUUID());
			//设置日志前缀
			logPrefix.set(String.format("uuid=%s:", inParams.getUuid()));
		}
		LogUtils.debug("{}入参：{}", logPrefix.get(), inParams);

		//加密&加签
		encryptAndSign(inParams);

		//调用openapi 并 处理返回值
		OutParams outParams = doCall(inParams);
		LogUtils.debug("{}出参：{}", logPrefix.get(), outParams);
		return outParams;
	}

	/**
	 * 调用openapi 注：请用{@link OpenApiClientBuilder}构建{@link OpenApiClient}对象
	 *
	 * @param method API方法名
	 * @param params API方法参数
	 * @return 返回值
	 */
	public OutParams callOpenApi(String method, Object... params) {
		//检查方法参数
		checkInParams(callerId, api, method);

		//构建InParams对象
		InParams inParams = new InParams();
		inParams.setUuid(IdUtil.simpleUUID());
		inParams.setCallerId(callerId);
		inParams.setApi(api);
		inParams.setMethod(method);

		//设置日志前缀
		logPrefix.set(String.format("uuid=%s:", inParams.getUuid()));

		//设置入参的body
		setInParamsBody(inParams, params);

		//调用openapi
		return this.callOpenApi(inParams);
	}

	/**
	 * 调用openapi 注：请用{@link OpenApiClientBuilder}构建{@link OpenApiClient}对象
	 *
	 * @param api    API接口名
	 * @param method API方法名
	 * @param params API方法参数
	 * @return 返回值
	 */
	public OutParams callOpenApi(String api, String method, Object... params) {
		//检查方法参数
		checkInParams(callerId, api, method);

		//构建InParams对象
		InParams inParams = new InParams();
		inParams.setUuid(IdUtil.simpleUUID());
		inParams.setCallerId(callerId);
		inParams.setApi(api);
		inParams.setMethod(method);

		//设置日志前缀
		logPrefix.set(String.format("uuid=%s:", inParams.getUuid()));

		//设置入参的body
		setInParamsBody(inParams, params);

		//调用openapi
		return this.callOpenApi(inParams);
	}

	/**
	 * 调用openapi
	 *
	 * @param callerId 调用者ID
	 * @param api      API接口名
	 * @param method   API方法名
	 * @param params   API方法参数
	 * @return 返回值
	 */
	public OutParams callOpenApi(String callerId, String api, String method, Object... params) {
		//检查方法参数
		checkInParams(callerId, api, method);

		//构建InParams对象
		InParams inParams = new InParams();
		inParams.setUuid(IdUtil.simpleUUID());
		inParams.setCallerId(callerId);
		inParams.setApi(api);
		inParams.setMethod(method);

		//设置日志前缀
		logPrefix.set(String.format("uuid=%s:", inParams.getUuid()));

		//设置入参的body
		setInParamsBody(inParams, params);

		//调用openapi
		return this.callOpenApi(inParams);
	}

	/**
	 * 设置入参的body
	 *
	 * @param inParams 入参
	 * @param params   方法参数
	 */
	private void setInParamsBody(InParams inParams, Object[] params) {
		String body = null;
		byte[] bodyBytes = null;
		boolean multiParam;
		if (params == null || params.length == 0) {
			//无参函数
			body = StrUtil.EMPTY;
			multiParam = false;
		} else if (params.length == 1) {
			//单参函数
			Object param = params[0];
			Class paramClass = param.getClass();
			if (BinaryUtil.isBinaryParam(param)) {
				BinaryParam binaryParam = this.getBinaryParam(param);
				body = binaryParam.getBinariesStr();
				bodyBytes = BinaryUtil.buildMultiBinaryBytes(binaryParam.getBinaries(), body);
			} else {
				body = StrObjectConvert.objToStr(param, paramClass);
			}
			multiParam = false;
		} else {
			//多参函数
			List<String> paramStrList = new ArrayList<>();
			List<Binary> binaryList = new ArrayList<>();
			for (Object param : params) {
				//按照参数的顺序，依次转换成字符串或二进制数据类型
				if (BinaryUtil.isBinaryParam(param)) {
					BinaryParam binaryParam = this.getBinaryParam(param);
					binaryList.addAll(binaryParam.getBinaries());
					paramStrList.add(binaryParam.getBinariesStr());
				} else {
					paramStrList.add(StrObjectConvert.objToStr(param, param.getClass()));
				}
			}
			body = JSONUtil.toJsonStr(paramStrList);
			if (CollUtil.isNotEmpty(binaryList)) {
				bodyBytes = BinaryUtil.buildMultiBinaryBytes(binaryList, body);
			}
			multiParam = true;
		}
		inParams.setBody(body);
		if (bodyBytes != null) {
			//二进制数据传输
			inParams.setBodyBytes(enableCompress ? CompressUtil.compress(bodyBytes) : bodyBytes);
			inParams.setDataType(DataType.BINARY);
		} else {
			//常规文本传输
			inParams.setBodyBytes(enableCompress ? CompressUtil.compressText(body)
				: body.getBytes(StandardCharsets.UTF_8));
			inParams.setDataType(DataType.TEXT);
		}
		LogUtils.debug("{}请求体的数据类型为：{}", logPrefix.get(), inParams.getDataType());
		inParams.setMultiParam(multiParam);
	}

	/**
	 * 获取二进制类型参数对象
	 *
	 * @param obj 参数对象
	 * @return 二进制类型参数对象
	 */
	private BinaryParam getBinaryParam(Object obj) {
		BinaryParam binaryParam = new BinaryParam();
		List<Binary> binaryList = new ArrayList<>();
		if (obj instanceof Binary) {
			binaryList.add((Binary) obj);
			binaryParam.setBinariesStr(BinaryUtil.getBinaryString((Binary) obj));
		} else if (TypeUtil.isBinaryArray(obj.getClass())) {
			Binary[] binaries = (Binary[]) obj;
			List<Binary> arrayBinaries = new ArrayList<>();
			for (Binary binary : binaries) {
				arrayBinaries.add(binary);
				binaryList.add(binary);
			}
			binaryParam.setBinariesStr(BinaryUtil.getBinariesString(arrayBinaries));
		} else if (TypeUtil.isBinaryCollection(obj)) {
			Collection coll = (Collection) obj;
			List<Binary> listBinaries = new ArrayList<>();
			for (Object element : coll) {
				Binary binary = (Binary) element;
				listBinaries.add(binary);
				binaryList.add(binary);
			}
			binaryParam.setBinariesStr(BinaryUtil.getBinariesString(listBinaries));
		}
		binaryParam.setBinaries(binaryList);
		return binaryParam;
	}

	/**
	 * 加密&加签
	 *
	 * @param inParams 入参
	 */
	private void encryptAndSign(InParams inParams) {
		//加密
		long startTime = System.nanoTime();
		byte[] bodyBytes = inParams.getBodyBytes();
		if (ArrayUtil.isNotEmpty(bodyBytes)) {
			if (this.cryModeEnum == CryModeEnum.SYMMETRIC_CRY) {
				//生成对称密钥key
				byte[] keyBytes = SymmetricCryUtil.getKey(symmetricCryEnum);

				//转成base64不会有问题，如果采用new String("utf-8"),再转回来字节数会变大
				String key = Base64Util.bytesToBase64(keyBytes);

				//对key使用非对称加密
				String cryKey = this.asymmetricCryHandler.cry(remotePublicKey, key);
				inParams.setSymmetricCryKey(cryKey);

				//对内容进行对称加密
				bodyBytes = this.symmetricCryHandler.cry(bodyBytes, keyBytes);
			} else if (this.cryModeEnum == CryModeEnum.ASYMMETRIC_CRY) {
				bodyBytes = this.asymmetricCryHandler.cry(remotePublicKey, bodyBytes);
			} else {
				//不加密模式CryModeEnum.NONE
			}
			inParams.setBodyBytes(bodyBytes);
		}
		this.logCostTime("加密", startTime);

		//加签
		startTime = System.nanoTime();
		byte[] signContent = CommonUtil.getSignContent(inParams);
		String sign = this.asymmetricCryHandler.sign(selfPrivateKey, signContent);
		inParams.setSign(sign);
		this.logCostTime("加签", startTime);
	}

	/**
	 * 调用远程openapi接口
	 *
	 * @param inParams 入参
	 * @return 结果
	 */
	private OutParams doCall(InParams inParams) {
		long startTime = System.nanoTime();
		String url = CommonUtil.completeUrl(baseUrl, Constant.OPENAPI_PATH);
		Map<String, String> headers = this.getHeaders(inParams);
		byte[] bodyBytes = inParams.getBodyBytes();
		LogUtils.debug("{}调用openapi入参:{}", logPrefix.get(), inParams);
		//构造http请求对象
		HttpRequest request = HttpRequest.post(url)
			.setConnectionTimeout(httpConnectionTimeout * 1000)
			.setReadTimeout(httpReadTimeout * 1000)
			.addHeaders(headers)
			.header(cn.hutool.http.Header.ACCEPT, ContentType.OCTET_STREAM.getValue())
			.contentType(ContentType.OCTET_STREAM.getValue())
			.body(bodyBytes);
		//设置http代理
		if (StrUtil.isNotBlank(this.httpProxyHost) && this.httpProxyPort != null) {
			request.setHttpProxy(httpProxyHost, httpProxyPort);
		}
		//执行http请求
		HttpResponse response = request.execute();
		OutParams outParams = getOutParams(response);
		LogUtils.debug("{}调用openapi出参：{}", logPrefix.get(), outParams);
		LogUtils.debug("{}响应体的数据类型为：{}", logPrefix.get(), outParams.getDataType());
		this.logCostTime("调用openapi", startTime);

		if (OutParams.isSuccess(outParams)) {
			//判断是否需要解密数据
			if (retDecrypt) {
				//解密数据
				decryptData(outParams);

				//对称加密密钥清空
				outParams.setSymmetricCryKey(null);
			}

			//返回值字节数组转成字符串
			byte[] retBytes = outParams.getDataBytes();
			if (ArrayUtil.isNotEmpty(retBytes)) {
				if (outParams.getDataType() == DataType.BINARY) {
					//提取参数byte[]
					retBytes = enableCompress ? CompressUtil.decompress(retBytes) : retBytes;
					int paramLength = BinaryUtil.getParamLength(retBytes);
					String paramStr = BinaryUtil.getParamStr(retBytes, paramLength);
					outParams.setData(paramStr);
					//提取二进制数据byte[]
					long binaryLengthStartIndex = BinaryUtil.getBinaryLengthStartIndex(paramLength);
					byte[] binaryDataBytes = BinaryUtil.getBinaryDataBytes(retBytes,
						binaryLengthStartIndex);
					outParams.setBinaryData(binaryDataBytes);
				} else {
					String data = enableCompress ? CompressUtil.decompressToText(retBytes)
						: new String(retBytes, StandardCharsets.UTF_8);
					outParams.setData(data);
				}
				outParams.setDataBytes(null);
				outParams.setDataBytesStr(null);
			}
		} else {
			throw new OpenApiClientException("调用openapi异常:" + outParams);
		}
		return outParams;
	}


	/**
	 * 获取出参
	 *
	 * @param response HTTP响应
	 * @return 出参
	 */
	private OutParams getOutParams(HttpResponse response) {
		OutParams outParams = new OutParams();
		outParams.setUuid(response.header(Header.Response.UUID));
		outParams.setCode(Integer.valueOf(response.header(Header.Response.CODE)));
		outParams.setMessage(response.header(Header.Response.MESSAGE));
		outParams.setSymmetricCryKey(response.header(Header.Response.SYMMETRIC_CRY_KEY));
		outParams.setDataType(
			Enum.valueOf(DataType.class, response.header(Header.Response.DATA_TYPE)));
		outParams.setDataBytes(response.bodyBytes());
		return outParams;
	}

	/**
	 * 构建请求头信息
	 *
	 * @param inParams 入参
	 * @return 请求头
	 */
	private Map<String, String> getHeaders(InParams inParams) {
		Map<String, String> headers = new HashMap<>();
		headers.put(Header.Request.UUID, inParams.getUuid());
		headers.put(Header.Request.CALLER_ID, inParams.getCallerId());
		headers.put(Header.Request.API, inParams.getApi());
		headers.put(Header.Request.METHOD, inParams.getMethod());
		headers.put(Header.Request.SIGN, inParams.getSign());
		headers.put(Header.Request.SYMMETRIC_CRY_KEY, inParams.getSymmetricCryKey());
		headers.put(Header.Request.MULTI_PARAM, String.valueOf(inParams.isMultiParam()));
		headers.put(Header.Request.DATA_TYPE, inParams.getDataType().name());
		return headers;
	}

	/**
	 * 解密数据
	 *
	 * @param outParams 返回值
	 */
	private void decryptData(OutParams outParams) {
		try {
			long startTime = System.nanoTime();
			byte[] dataBytes = outParams.getDataBytes();
			if (ArrayUtil.isNotEmpty(dataBytes)) {
				if (this.cryModeEnum == CryModeEnum.SYMMETRIC_CRY) {
					String key = this.asymmetricCryHandler.deCry(selfPrivateKey,
						outParams.getSymmetricCryKey());
					byte[] keyBytes = Base64Util.base64ToBytes(key);
					dataBytes = this.symmetricCryHandler.deCry(dataBytes, keyBytes);
				} else if (this.cryModeEnum == CryModeEnum.ASYMMETRIC_CRY) {
					dataBytes = this.asymmetricCryHandler.deCry(selfPrivateKey, dataBytes);
				} else {
					//不加密模式CryModeEnum.NONE
				}
				outParams.setDataBytes(dataBytes);
			}
			this.logCostTime("解密", startTime);
		} catch (OpenApiClientException be) {
			String errorMsg = "解密失败：" + be.getMessage();
			LogUtils.error(logPrefix.get() + errorMsg, be);
			throw new OpenApiClientException(errorMsg);
		} catch (Exception ex) {
			LogUtils.error(logPrefix.get() + "解密失败", ex);
			throw new OpenApiClientException("解密失败");
		}
	}

	/**
	 * 检查入参
	 *
	 * @param callerId 调用者ID
	 * @param api      API接口名
	 * @param method   API方法名
	 */
	private void checkInParams(String callerId, String api, String method) {
		if (StrUtil.isBlank(callerId)) {
			throw new OpenApiClientException("调用者ID不能为空");
		}
		if (StrUtil.isBlank(api)) {
			throw new OpenApiClientException("API接口名不能为空");
		}
		if (StrUtil.isBlank(method)) {
			throw new OpenApiClientException("API方法名不能为空");
		}
	}

	/**
	 * 记录加密模式
	 *
	 * @param cryModeEnum 加密模式
	 */
	private void logCryModel(CryModeEnum cryModeEnum) {
		if (cryModeEnum == CryModeEnum.SYMMETRIC_CRY) {
			LogUtils.debug("采用非对称加密{}+对称加密{}模式", asymmetricCryEnum, symmetricCryEnum);
		} else if (cryModeEnum == CryModeEnum.ASYMMETRIC_CRY) {
			LogUtils.debug("仅采用非对称加密{}模式", asymmetricCryEnum);
		} else if (cryModeEnum == CryModeEnum.NONE) {
			LogUtils.debug("采用不加密模式,签名用的非对称加密{}", asymmetricCryEnum);
		}
	}

	/**
	 * 记录操作的耗时
	 *
	 * @param operate   操作
	 * @param startTime 操作开始时间
	 */
	private void logCostTime(String operate, long startTime) {
		LogUtils.debug("{}{}耗时:{}ms", logPrefix.get(), operate,
			(System.nanoTime() - startTime) / 100_0000);
	}

	@Override
	public String toString() {
		return String.format(
			"\nopenApiClient hashCode:%x,\nbaseUrl:%s,\nselfPrivateKey:%s,\nremotePublicKey:%s," +
				"\nasymmetricCryEnum:%s,\nretDecrypt:%s;\ncryModeEnum:%s,\nsymmetricCryEnum:%s," +
				"\ncallerId:%s,\napi:%s,\nhttpConnectionTimeout:%s,\nhttpReadTimeout:%s,\nenableCompress:%s",
			this.hashCode(), baseUrl, selfPrivateKey, remotePublicKey,
			asymmetricCryEnum, retDecrypt, cryModeEnum, symmetricCryEnum,
			callerId, api, httpConnectionTimeout, httpReadTimeout, enableCompress);
	}


}
