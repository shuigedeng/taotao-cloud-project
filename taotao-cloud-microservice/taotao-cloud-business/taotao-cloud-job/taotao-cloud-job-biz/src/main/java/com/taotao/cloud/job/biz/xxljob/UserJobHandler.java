/**
 * Project Name: my-projects Package Name: com.taotao.cloud.sys.biz.job Date: 2020/6/16 14:43
 * Author: shuigedeng
 */
package com.taotao.cloud.job.biz.xxljob;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.xxl.executor.annotation.XxlRegister;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.seata.spring.annotation.GlobalTransactional;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * UserJobHandler
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:43
 */
@Component
public class UserJobHandler {

	@XxlJob("ThrowJobHandler")
	public ReturnT<String> throwJobHandler(String param) throws Exception {
		XxlJobHelper.log("XXL-JOB, throwwwwwwwwwwwwww");

		LogUtils.info("=============xxljob throwwwwwwwwwwwwwwwwwwwwwwwwww");

		throw new Exception("XXL-JOB, throwwwwwwwwwwwwww");
	}

	@XxlJob("TestJobHandler")
	public ReturnT<String> testJobHandler(String param) throws Exception {
		XxlJobHelper.log("XXL-JOB, successsssssssssss");

		LogUtils.info("=============xxljob succcccccccccccccc");

		return ReturnT.SUCCESS;
	}

	@XxlJob("UserJobHandler")
	@XxlRegister(cron = "59 59 23 * * ?", author = "shuigedeng")
	public ReturnT<String> userJobHandler(String param) throws Exception {
		XxlJobHelper.log("XXL-JOB, Hello World.");
		XxlJobHelper.log("XXL-JOB, Hello World.1");
		XxlJobHelper.log("XXL-JOB, Hello World.2");
		XxlJobHelper.log("XXL-JOB, Hello World.3");
		XxlJobHelper.log("XXL-JOB, Hello World.4");

		LogUtils.info("=============xxljob eeeeeeeeeeeeeeeeeeeee");

		//for (int i = 0; i < 5; i++) {
		//    XxlJobHelper.log("beat at:" + i);
		//    System.out.println("XXL-JOB测试-----" + i);
		//    TimeUnit.SECONDS.sleep(2);
		//}

		//throw new RuntimeException("XXL-JOB测试异常");

		return ReturnT.FAIL;
	}


	/**
	 * 1、简单任务示例（Bean模式）
	 */
	@XxlJob("demoJobHandler")
	@XxlRegister(cron = "59 1-2 0 * * ?",
		triggerStatus = 1)
	public void demoJobHandler() throws Exception {
		XxlJobHelper.log("XXL-JOB, Hello World.");

		for (int i = 0; i < 5; i++) {
			XxlJobHelper.log("beat at:" + i);
			TimeUnit.SECONDS.sleep(2);
		}
		// default success
	}


	/**
	 * 2、分片广播任务
	 */
	@XxlJob("shardingJobHandler")
	@XxlRegister(cron = "0 0 0 * * ? *",
		author = "hydra",
		jobDesc = "测试job")
	public void shardingJobHandler() throws Exception {

		// 分片参数
		int shardIndex = XxlJobHelper.getShardIndex();
		int shardTotal = XxlJobHelper.getShardTotal();

		XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

		// 业务逻辑
		for (int i = 0; i < shardTotal; i++) {
			if (i == shardIndex) {
				XxlJobHelper.log("第 {} 片, 命中分片开始处理", i);
			} else {
				XxlJobHelper.log("第 {} 片, 忽略", i);
			}
		}

	}


	/**
	 * 3、命令行任务
	 */
	@XxlJob("commandJobHandler")
	public void commandJobHandler() throws Exception {
		String command = XxlJobHelper.getJobParam();
		int exitValue = -1;

		BufferedReader bufferedReader = null;
		try {
			// command process
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command(command);
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			//Process process = Runtime.getRuntime().exec(command);

			BufferedInputStream bufferedInputStream = new BufferedInputStream(
				process.getInputStream());
			bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

			// command log
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				XxlJobHelper.log(line);
			}

			// command exit
			process.waitFor();
			exitValue = process.exitValue();
		} catch (Exception e) {
			XxlJobHelper.log(e);
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}

		if (exitValue == 0) {
			// default success
		} else {
			XxlJobHelper.handleFail("command exit value(" + exitValue + ") is failed");
		}

	}


	/**
	 * 4、跨平台Http任务 参数示例： "url: http://www.baidu.com\n" + "method: get\n" + "data: content\n";
	 */
	@XxlJob("httpJobHandler")
	public void httpJobHandler() throws Exception {

		// param parse
		String param = XxlJobHelper.getJobParam();
		if (param == null || param.trim().length() == 0) {
			XxlJobHelper.log("param[" + param + "] invalid.");

			XxlJobHelper.handleFail();
			return;
		}

		String[] httpParams = param.split("\n");
		String url = null;
		String method = null;
		String data = null;
		for (String httpParam : httpParams) {
			if (httpParam.startsWith("url:")) {
				url = httpParam.substring(httpParam.indexOf("url:") + 4).trim();
			}
			if (httpParam.startsWith("method:")) {
				method = httpParam.substring(httpParam.indexOf("method:") + 7).trim().toUpperCase();
			}
			if (httpParam.startsWith("data:")) {
				data = httpParam.substring(httpParam.indexOf("data:") + 5).trim();
			}
		}

		// param valid
		if (url == null || url.trim().length() == 0) {
			XxlJobHelper.log("url[" + url + "] invalid.");

			XxlJobHelper.handleFail();
			return;
		}
		if (method == null || !Arrays.asList("GET", "POST").contains(method)) {
			XxlJobHelper.log("method[" + method + "] invalid.");

			XxlJobHelper.handleFail();
			return;
		}
		boolean isPostMethod = "POST".equals(method);

		// request
		HttpURLConnection connection = null;
		BufferedReader bufferedReader = null;
		try {
			// connection
			URL realUrl = new URL(url);
			connection = (HttpURLConnection) realUrl.openConnection();

			// connection setting
			connection.setRequestMethod(method);
			connection.setDoOutput(isPostMethod);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setReadTimeout(5 * 1000);
			connection.setConnectTimeout(3 * 1000);
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");

			// do connection
			connection.connect();

			// data
			if (isPostMethod && data != null && data.trim().length() > 0) {
				DataOutputStream dataOutputStream = new DataOutputStream(
					connection.getOutputStream());
				dataOutputStream.write(data.getBytes("UTF-8"));
				dataOutputStream.flush();
				dataOutputStream.close();
			}

			// valid StatusCode
			int statusCode = connection.getResponseCode();
			if (statusCode != 200) {
				throw new RuntimeException("Http Request StatusCode(" + statusCode + ") Invalid.");
			}

			// result
			bufferedReader = new BufferedReader(
				new InputStreamReader(connection.getInputStream(), "UTF-8"));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}
			String responseMsg = result.toString();

			XxlJobHelper.log(responseMsg);

			return;
		} catch (Exception e) {
			XxlJobHelper.log(e);

			XxlJobHelper.handleFail();
			return;
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e2) {
				XxlJobHelper.log(e2);
			}
		}

	}

	/**
	 * 5、生命周期任务示例：任务初始化与销毁时，支持自定义相关逻辑；
	 */
	@XxlJob(value = "demoJobHandler2", init = "init", destroy = "destroy")
	public void demoJobHandler2() throws Exception {
		XxlJobHelper.log("XXL-JOB, Hello World.");
	}

	public void init() {
		LogUtils.info("init");
	}

	public void destroy() {
		LogUtils.info("destory");
	}


	/**
	 * 多服务调用
	 */
	@GlobalTransactional(rollbackFor = Exception.class)
	@XxlJob("multiServiceHandler")
	public void multiServiceHandler() throws Exception {
		//LoginUser admin = remoteUserService.getUserInfo("admin");
		//XxlJobHelper.log("XXL-JOB, multiServiceHandler result: {}", admin.toString());
		//SysUser sysUser = new SysUser();
		//sysUser.setUserName("test");
		//sysUser.setNickName("test");
		//remoteUserService.registerUserInfo(sysUser);
	}

}
