package com.taotao.cloud.seckill.biz.common.limit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;
/**
 * Nginx 限流压测
 * src/mian/resources/nginx
 * src/mian/resources/openresty
 */
public class NginxLimit {
	 //也可以使用AB压测 ab -n1000 -c 10 http://121.42.155.213
	 private static int count = 0;
	 public static void main(String[] args) throws IOException, InterruptedException {
	        final CountDownLatch latch = new CountDownLatch(1);
	        for (int i = 0; i < 80; i++) {
	            Thread t = new Thread(() -> {
					try {
						latch.await();
						String result = NginxLimit.sendGet("http://121.42.155.213");
						System.out.println(result);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
	            t.start();
	        }
	        latch.countDown();
	        Thread.sleep(5000);
	        System.out.println(count);
	       // System.in.read();
	}
	//发送GET请求
	public static String sendGet(String url) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlName = url;
	            URL realUrl = new URL(urlName);
	            URLConnection conn = realUrl.openConnection();// 打开和URL之间的连接
	            conn.setRequestProperty("accept", "*/*");// 设置通用的请求属性
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
	            conn.setConnectTimeout(4000);
	            conn.connect();// 建立实际的连接
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));// 定义BufferedReader输入流来读取URL的响应
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	        	count ++;
	            System.out.println("发送GET请求出现异常！" + e);
	        } finally {// 使用finally块来关闭输入流
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (IOException ex) {
	                System.out.println("关闭流异常");
	            }
	        }
	        return result;
	}
}
