package com.taotao.cloud.health.dump;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.model.HealthException;
import com.taotao.cloud.health.utils.ProcessUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: chejiangyi
 * @version: 2019-09-07 13:36
 **/
public class DumpProvider {

	private static Long lastDumpTime = 0L;

	public File[] getList() {
		File file = new File(".");
		return file.listFiles((dir, name) -> name.contains(".hprof"));
	}

	public void list() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(("不要多次dump,一分钟仅限一次,仅限linux系统有效<br/><a href='do/'>立即Dump</a><br/>"));
		for (File f : getList()) {
			if (!f.getName().endsWith(".tar")) {
				stringBuilder.append(
					String.format("%s (%s M)<a href='zip/?name=%s'>压缩</a><br/>", f.getName(),
						f.length() / 1024 / 1024, f.getName()));
			} else {
				stringBuilder.append(
					String.format("%s (%s M)<a href='download/?name=%s'>下载</a><br/>", f.getName(),
						f.length() / 1024 / 1024, f.getName()));
			}
		}
		response(stringBuilder.toString());
	}

	public void zip(String name) {
		for (File f : getList()) {
			if (name != null && name.equals(f.getName())) {
				try {
					Runtime.getRuntime().exec(String.format("tar -zcvf %s.tar %s",
						name, name));
					response("压缩成功,请等待耐心等待,不要重复执行!");
				} catch (Exception exp) {
					LogUtil.error(StarterNameConstant.HEALTH_STARTER, "zip 出错", exp);
					response("压缩出错:" + exp.getMessage());
				}
			}
		}
	}

	public void dump() {
		try {
			if (System.currentTimeMillis() - lastDumpTime < TimeUnit.MINUTES.toMillis(1)) {
				throw new HealthException("dump过于频繁,请等待后1分钟重试");
			}
			Runtime.getRuntime().exec(String.format("jmap -dump:format=b,file=heap.%s.hprof %s",
				new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()),
				ProcessUtils.getProcessID()));
			lastDumpTime = System.currentTimeMillis();
			response("dump成功,请等待耐心等待,不要重复执行!");
		} catch (Exception exp) {
			LogUtil.error(StarterNameConstant.HEALTH_STARTER, "dump 出错", exp);
			response("dump出错:" + exp.getMessage());
		}
	}

	public void download(String name) {
		for (File f : getList()) {
			if (name != null && name.equals(f.getName())) {
				HttpServletResponse response = RequestUtil.getResponse();
				response.reset();
				response.setContentType("application/x-download");
				response.addHeader("Content-Disposition", "attachment;filename=" + f.getName());
				response.addHeader("Content-Length", "" + f.length());
				response.setHeader("Content-type", "");
				try {
					try (FileInputStream fs = new FileInputStream(f)) {
						try (InputStream fis = new BufferedInputStream(fs)) {
							try (OutputStream out = new BufferedOutputStream(
								response.getOutputStream())) {
								response.setContentType("application/octet-stream");
								byte[] buffer = new byte[1024];
								int i = -1;
								while ((i = fis.read(buffer)) != -1) {
									out.write(buffer, 0, i);
								}
								fis.close();
								out.flush();
							}
						}
					}
				} catch (Exception exp) {
					LogUtil.error(StarterNameConstant.HEALTH_STARTER, "download 出错",
						exp);
					response("下载出错:" + exp.getMessage());
				}
			}
		}
	}

	private void response(String html) {
		try {
			HttpServletResponse response = RequestUtil.getResponse();
			response.reset();
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append(html);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}
}
