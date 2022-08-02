package com.taotao.cloud.sys.biz.modules.core.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统运行时工具类，用于执行系统命令的工具
 *
 * @author Looly
 * @since 3.1.1
 */
public class RuntimeUtil {

	/**
	 * 执行系统命令，使用系统默认编码
	 *
	 * @param cmds 命令列表，每个元素代表一条命令
	 * @return 执行结果
	 * @ IO异常
	 */
	public static String execForStr(File dir,String... cmds) throws IOException {
		return execForStr(dir,StandardCharsets.UTF_8, cmds);
	}

	/**
	 * 执行系统命令，使用系统默认编码
	 *
	 * @param charset 编码
	 * @param cmds    命令列表，每个元素代表一条命令
	 * @return 执行结果
	 * @ IO异常
	 * @since 3.1.2
	 */
	public static String execForStr(File dir,Charset charset, String... cmds) throws IOException {
		return getResult(exec(cmds), charset);
	}

	/**
	 * 执行系统命令，使用系统默认编码
	 *
	 * @param cmds 命令列表，每个元素代表一条命令
	 * @return 执行结果，按行区分
	 * @ IO异常
	 */
	public static List<String> execForLines(String... cmds) throws IOException {
		return execForLines(StandardCharsets.UTF_8, cmds);
	}

	/**
	 * 执行系统命令，使用系统默认编码
	 *
	 * @param charset 编码
	 * @param cmds    命令列表，每个元素代表一条命令
	 * @return 执行结果，按行区分
	 * @ IO异常
	 * @since 3.1.2
	 */
	public static List<String> execForLines(Charset charset, String... cmds) throws IOException {
		return getResultLines(exec(cmds), charset);
	}

	/**
	 * 执行命令<br>
	 * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
	 *
	 * @param cmds 命令
	 * @return {@link Process}
	 */
	public static Process exec(File dir,String... cmds) throws IOException {
		if (ArrayUtils.isEmpty(cmds)) {
			throw new NullPointerException("Command is empty !");
		}

		// 单条命令的情况
		if (1 == cmds.length) {
			final String cmd = cmds[0];
			if (StringUtils.isBlank(cmd)) {
				throw new NullPointerException("Command is empty !");
			}
			cmds = StringUtils.split(cmd, ' ');
		}

		return new ProcessBuilder(cmds).directory(dir).redirectErrorStream(true).start();
	}

	/**
	 * 执行命令<br>
	 * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
	 *
	 * @param envp 环境变量参数，传入形式为key=value，null表示继承系统环境变量
	 * @param cmds 命令
	 * @return {@link Process}
	 * @since 4.1.6
	 */
	public static Process exec(String[] envp, String... cmds) throws IOException {
		return exec(envp, null, cmds);
	}

	/**
	 * 执行命令<br>
	 * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
	 *
	 * @param envp 环境变量参数，传入形式为key=value，null表示继承系统环境变量
	 * @param dir  执行命令所在目录（用于相对路径命令执行），null表示使用当前进程执行的目录
	 * @param cmds 命令
	 * @return {@link Process}
	 * @since 4.1.6
	 */
	public static Process exec(String[] envp, File dir, String... cmds) throws IOException {
		if (ArrayUtils.isEmpty(cmds)) {
			throw new NullPointerException("Command is empty !");
		}

		// 单条命令的情况
		if (1 == cmds.length) {
			final String cmd = cmds[0];
			if (StringUtils.isBlank(cmd)) {
				throw new NullPointerException("Command is empty !");
			}
			cmds = StringUtils.split(cmd, ' ');
		}
		return Runtime.getRuntime().exec(cmds, envp, dir);
	}

	// -------------------------------------------------------------------------------------------------- result

	/**
	 * 获取命令执行结果，使用系统默认编码，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @return 命令执行结果列表
	 */
	public static List<String> getResultLines(Process process) throws IOException {
		return getResultLines(process, StandardCharsets.UTF_8);
	}

	/**
	 * 获取命令执行结果，使用系统默认编码，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @param charset 编码
	 * @return 命令执行结果列表
	 * @since 3.1.2
	 */
	public static List<String> getResultLines(Process process, Charset charset) throws IOException {
		InputStream in = null;
		try {
			in = process.getInputStream();
			return IOUtils.readLines(in,charset);
		} finally {
			IOUtils.closeQuietly(in);
			destroy(process);
		}
	}

	/**
	 * 获取命令执行结果，使用系统默认编码，，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @return 命令执行结果列表
	 * @since 3.1.2
	 */
	public static String getResult(Process process) throws IOException {
		return getResult(process, StandardCharsets.UTF_8);
	}

	/**
	 * 获取命令执行结果，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @param charset 编码
	 * @return 命令执行结果列表
	 * @since 3.1.2
	 */
	public static String getResult(Process process, Charset charset) throws IOException {
		InputStream in = null;
		try {
			in = process.getInputStream();
			return IOUtils.toString(in, StandardCharsets.UTF_8);
		} finally {
			IOUtils.closeQuietly(in);
			destroy(process);
		}
	}

	/**
	 * 获取命令执行异常结果，使用系统默认编码，，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @return 命令执行结果列表
	 * @since 4.1.21
	 */
	public static String getErrorResult(Process process) throws IOException {
		return getErrorResult(process, StandardCharsets.UTF_8);
	}

	/**
	 * 获取命令执行异常结果，获取后销毁进程
	 *
	 * @param process {@link Process} 进程
	 * @param charset 编码
	 * @return 命令执行结果列表
	 * @since 4.1.21
	 */
	public static String getErrorResult(Process process, Charset charset) throws IOException {
		InputStream in = null;
		try {
			in = process.getErrorStream();
			return IOUtils.toString(in, StandardCharsets.UTF_8);
		} finally {
			IOUtils.closeQuietly(in);
			destroy(process);
		}
	}

	/**
	 * 销毁进程
	 *
	 * @param process 进程
	 * @since 3.1.2
	 */
	public static void destroy(Process process) {
		if (null != process) {
			process.destroy();
		}
	}

	/**
	 * 增加一个JVM关闭后的钩子，用于在JVM关闭时执行某些操作
	 *
	 * @param hook 钩子
	 * @since 4.0.5
	 */
	public static void addShutdownHook(Runnable hook) {
		Runtime.getRuntime().addShutdownHook((hook instanceof Thread) ? (Thread) hook : new Thread(hook));
	}

	/**
	 * 获得JVM可用的处理器数量（一般为CPU核心数）
	 *
	 * @return 可用的处理器数量
	 * @since 5.3.0
	 */
	public static int getProcessorCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	/**
	 * 获得JVM中剩余的内存数，单位byte
	 *
	 * @return JVM中剩余的内存数，单位byte
	 * @since 5.3.0
	 */
	public static long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * 获得JVM已经从系统中获取到的总共的内存数，单位byte
	 *
	 * @return JVM中剩余的内存数，单位byte
	 * @since 5.3.0
	 */
	public static long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * 获得JVM中可以从系统中获取的最大的内存数，单位byte，以-Xmx参数为准
	 *
	 * @return JVM中剩余的内存数，单位byte
	 * @since 5.3.0
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * 获得JVM最大可用内存，计算方法为：<br>
	 * 最大内存-总内存+剩余内存
	 *
	 * @return 最大可用内存
	 */
	public final long getUsableMemory() {
		return getMaxMemory() - getTotalMemory() + getFreeMemory();
	}
}
