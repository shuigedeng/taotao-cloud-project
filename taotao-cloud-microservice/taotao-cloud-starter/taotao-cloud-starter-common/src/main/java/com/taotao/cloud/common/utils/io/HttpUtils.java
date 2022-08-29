package com.taotao.cloud.common.utils.io;


import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.collection.MapUtils;
import com.taotao.cloud.common.utils.common.ArgUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP 工具类
 */
public final class HttpUtils {

    private HttpUtils(){}

    /**
     * get 请求
     */
    public static final String GET = "GET";

    /**
     * post 请求
     */
    public static final String POST = "POST";

    /**
     * http get 请求
     * @param requestUrl 请求地址
     * @return 结果
     */
    public static String getRequest(String requestUrl) {
        return request(requestUrl, GET);
    }

    /**
     * http post 请求
     * @param requestUrl 请求地址
     * @return 结果
     */
    public static String postRequest(String requestUrl) {
        return request(requestUrl, POST);
    }

    /**
     * http 请求
     * @param requestUrl 请求地址
     * @param requestMethod 方法
     * @return 结果
     */
    public static String request(String requestUrl, String requestMethod) {
        return request(requestUrl, requestMethod, CommonConstant.UTF8, null);
    }

    /**
     * http 请求
     * @param requestUrl 请求地址
     * @param requestMethod 方法
     * @param headerMap 头信息
     * @return 结果
     */
    public static String request(String requestUrl, String requestMethod,
                                 final Map<String, String> headerMap) {
        return request(requestUrl, requestMethod, CommonConstant.UTF8, headerMap);
    }

    /**
     * http 请求
     * @param requestUrl 请求地址
     * @param requestMethod 方法
     * @param charset 流编码
     * @param headerMap 头信息
     * @return 结果
     */
    public static String request(String requestUrl, String requestMethod,
                                 final String charset,
                                 final Map<String, String> headerMap) {
        StringBuilder buffer = new StringBuilder();
        try {
            URL url = new URL(requestUrl);
            // http协议传输
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            //SSL
//            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
//            sslContext.init(null,null,null);
//            SSLContext.setDefault(sslContext);

            // 设置 header 属性
            if(MapUtils.isNotEmpty(headerMap)) {
                for(Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpUrlConn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (GET.equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            httpUrlConn.disconnect();
            return buffer.toString();
        } catch (Exception e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 构建文件头信息
     * @param filePath 文件路径
     * @return map 信息
     */
    public static Map<String, String> buildHeaderMap(final String filePath) {
        List<String> stringList = FileUtils.readAllLines(filePath);

        Map<String, String> map = new HashMap<>(stringList.size());
        for (String line : stringList) {
            int index = line.indexOf(PunctuationConst.COLON);
            String key = line.substring(0, index).trim();
            String value = line.substring(index + 1).trim();
            map.put(key, value);
        }

        return map;
    }

    /**
     * 将远程的文件下载到本地
     * @param remoteUrl 远程的 url
     * @param localUrl 本地 url
     */
    public static void download(final String remoteUrl,
                                final String localUrl) {
        download(remoteUrl, localUrl, null);
    }

    /**
     * 将远程的文件下载到本地
     * @param remoteUrl 远程的 url
     * @param localUrl 本地 url
     * @param headerMap 头信息 map
     */
    public static void download(final String remoteUrl,
                                final String localUrl,
                                final Map<String, String> headerMap) {
        ArgUtils.notEmpty(remoteUrl, "remoteUrl");
        ArgUtils.notEmpty(localUrl, "localUrl");

        try {
            URL url = new URL(remoteUrl);
            URLConnection conn = url.openConnection();
            if(MapUtils.isNotEmpty(headerMap)) {
                for(Map.Entry<String, String> entry : headerMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    conn.setRequestProperty(key, value);
                }
            }

            try(DataInputStream dataInputStream = new DataInputStream(conn.getInputStream());
                FileOutputStream fileOutputStream = new FileOutputStream(new File(localUrl))) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int length;

                while ((length = dataInputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                fileOutputStream.write(output.toByteArray());
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
    }



	/**
	 * 发送GET请求
	 *
	 * @param requestUrl  requestUrl
	 * @param charSetName charSetName
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 16:44:09
	 */
	public static Object getRequest(String requestUrl, String charSetName) {
		String res = "";
		StringBuilder buffer = new StringBuilder();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			if (200 == urlCon.getResponseCode()) {
				InputStream is = urlCon.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, charSetName);
				BufferedReader br = new BufferedReader(isr);
				String str = null;
				while ((str = br.readLine()) != null) {
					buffer.append(str);
				}
				br.close();
				isr.close();
				is.close();
				res = buffer.toString();
				return res;
			} else {
				throw new Exception("连接失败");
			}
		} catch (Exception e) {
			LogUtils.error(e);
		}
		return null;
	}

	/**
	 * 发送POST请求
	 *
	 * @param path path
	 * @param post post
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 16:44:22
	 */
	public static Object postRequest(String path, String post) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			// 提交模式
			httpURLConnection.setRequestMethod("POST");
			//连接超时 单位毫秒
			httpURLConnection.setConnectTimeout(10000);
			//读取超时 单位毫秒
			httpURLConnection.setReadTimeout(2000);
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			//post的参数 xx=xx&yy=yy
			printWriter.write(post);
			// flush输出流的缓冲
			printWriter.flush();
			//开始获取数据
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();

			return bos.toString("utf-8");
		} catch (Exception e) {
			LogUtils.error(e);
		}
		return null;
	}
}
