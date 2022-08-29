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


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.FileTypeConst;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.model.CharPool;
import com.taotao.cloud.common.support.handler.IMapHandler;
import com.taotao.cloud.common.utils.collection.ArrayUtils;
import com.taotao.cloud.common.utils.collection.MapUtils;
import com.taotao.cloud.common.utils.common.ArgUtils;
import com.taotao.cloud.common.utils.exception.ExceptionUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serial;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类 用于获取文件的内容
 */
public final class FileUtils extends org.springframework.util.FileCopyUtils {

	private FileUtils() {
	}

	/**
	 * 获取文件内容
	 *
	 * @param filePath 文件路径
	 * @return 文件不存在或异常等, 直接抛出异常
	 */
	public static String getFileContent(String filePath) {
		return getFileContent(filePath, CommonConstant.UTF8);
	}

	/**
	 * 获取文件内容
	 *
	 * @param filePath 文件路径
	 * @param charset  文件编码
	 * @return 文件不存在或异常等, 直接抛出异常
	 */
	public static String getFileContent(String filePath, final String charset) {
		File file = new File(filePath);
		if (file.exists()) {
			try (InputStream inputStream = new FileInputStream(file)) {
				return getFileContent(inputStream, charset);
			} catch (IOException e) {
				throw new CommonRuntimeException(e);
			}
		}
		throw new CommonRuntimeException(filePath + " is not exists!");
	}

	/**
	 * 获取文件内容 默认编码UTF8
	 *
	 * @param inputStream 输入流
	 * @return 文件内容
	 */
	public static String getFileContent(InputStream inputStream) {
		return getFileContent(inputStream, CommonConstant.UTF8);
	}

	/**
	 * 获取文件内容 默认编码UTF8
	 *
	 * @param file    文件
	 * @param charset 文件编码
	 * @return 文件内容
	 */
	public static String getFileContent(final File file,
		final String charset) {
		try (InputStream inputStream = new FileInputStream(file)) {
			return getFileContent(inputStream, charset);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 获取文件内容 默认编码UTF8
	 *
	 * @param file 文件
	 * @return 文件内容
	 */
	public static String getFileContent(final File file) {
		try (InputStream inputStream = new FileInputStream(file)) {
			return getFileContent(inputStream);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 获取文件内容
	 *
	 * @param inputStream 文件输入流
	 * @param charset     文件编码
	 * @return 文件内容字符串
	 */
	public static String getFileContent(InputStream inputStream, String charset) {
		Charset charsetVal = Charset.forName(charset);
		return getFileContent(inputStream, 0, Integer.MAX_VALUE, charsetVal);
	}

	/**
	 * 获取文件内容
	 *
	 * @param inputStream 输入流
	 * @param startIndex  开始下标
	 * @param endIndex    结束下标
	 * @param charset     编码
	 * @return 结果
	 */
	public static String getFileContent(final InputStream inputStream,
		int startIndex,
		int endIndex,
		final Charset charset) {
		try {
			// 参数纠正
			endIndex = Math.min(endIndex, inputStream.available());
			startIndex = Math.max(0, startIndex);

			// 跳过指定长度
			inputStream.skip(startIndex);

			// 这个读取的数据可能不正确
			// InputStream.read(byte[] b) 无法保证读取的结果正确。
			final int count = endIndex - startIndex;
			byte[] bytes = new byte[count];
			// 已经成功读取的字节的个数
			// -1 也代表结束
			int readCount = 0;
			while (readCount < count && readCount != -1) {
				readCount += inputStream.read(bytes, readCount, count - readCount);
			}

			return new String(bytes, charset);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取文件后缀
	 *
	 * @param fileName 文件名称
	 * @return 文件后缀
	 */
	public static String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.') + 1);
	}


	/**
	 * 获取指定路径文件的每一行内容
	 *
	 * @param filePath 文件路径
	 * @param initLine 初始化行数
	 * @return 内容列表
	 */
	public static List<String> getFileContentEachLine(String filePath, int initLine) {
		File file = new File(filePath);
		return getFileContentEachLine(file, initLine);
	}

	/**
	 * 获取指定路径文件的每一行内容 1.初始化行数默认为0
	 *
	 * @param filePath 文件路径
	 * @return 内容列表
	 * @see #getFileContentEachLine(String, int) 获取指定路径文件的每一行内容
	 */
	public static List<String> getFileContentEachLine(String filePath) {
		File file = new File(filePath);
		return getFileContentEachLine(file, 0);
	}


	/**
	 * 获取指定文件的每一行内容。并对内容进行trim()操作。
	 *
	 * @param filePath 文件路径
	 * @param initLine 初始化行数
	 * @return 内容列表
	 */
	public static List<String> getFileContentEachLineTrim(String filePath, int initLine) {
		List<String> stringList = getFileContentEachLine(filePath, initLine);
		List<String> resultList = new LinkedList<>();

		for (String string : stringList) {
			resultList.add(string.trim());
		}

		return resultList;
	}

	/**
	 * 获取指定文件的每一行内容 默认初始行数为0
	 *
	 * @param file 文件
	 * @return 内容列表
	 */
	public static List<String> getFileContentEachLine(File file) {
		return getFileContentEachLine(file, 0);
	}

	/**
	 * 获取指定文件的每一行内容 [TWR](http://blog.csdn.net/doctor_who2004/article/details/50901195)
	 *
	 * @param file     指定读取文件
	 * @param initLine 初始读取行数
	 * @return 错误返回空列表
	 */
	public static List<String> getFileContentEachLine(File file, int initLine) {
		List<String> contentList = new LinkedList<>();

		if (!file.exists()) {
			return contentList;
		}

		//暂时使用此编码
		String charset = "UTF-8";
		try (FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
		) {
			// 用于记录行号
			int lineNo = 0;
			while (lineNo < initLine) {
				lineNo++;
				String ignore = bufferedReader.readLine();
			}

			String dataEachLine;   //每一行的内容
			while ((dataEachLine = bufferedReader.readLine()) != null) {
				lineNo++;
				//跳过空白行
				if (Objects.equals("", dataEachLine)) {
					continue;
				}
				contentList.add(dataEachLine);
			}
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}

		return contentList;
	}


	/**
	 * 获取文件内容的列表
	 *
	 * @param file     文件
	 * @param initLine 0 开始
	 * @param endLine  下标从0开始
	 * @param charset  编码
	 * @return string list
	 */
	@Deprecated
	public static List<String> getFileContentEachLine(final File file, final int initLine,
		final int endLine, final String charset) {
		List<String> contentList = new LinkedList<>();

		if (!file.exists()) {
			return contentList;
		}

		try (FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charset);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
		) {
			// 用于记录行号
			int lineNo = 0;
			while (lineNo < initLine) {
				lineNo++;
				String ignore = bufferedReader.readLine();
			}

			//每一行的内容
			String dataEachLine;
			while ((dataEachLine = bufferedReader.readLine()) != null
				&& lineNo < endLine) {
				lineNo++;
				contentList.add(dataEachLine);
			}
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}

		return contentList;
	}

	/**
	 * 获取每一行的文件内容 （1）如果文件不存在，直接返回空列表。
	 *
	 * @param file        文件信息
	 * @param charset     编码
	 * @param initLine    初始化行
	 * @param endLine     结束航
	 * @param ignoreEmpty 是否跳过空白行
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final File file,
		final String charset,
		final int initLine,
		final int endLine,
		final boolean ignoreEmpty) {
		ArgUtils.notNull(file, "file");
		ArgUtils.notEmpty(charset, "charset");
		if (!file.exists()) {
			throw new CommonRuntimeException("File not exists!");
		}

		try (FileInputStream inputStream = new FileInputStream(file)) {
			return readAllLines(inputStream, charset, initLine, endLine, ignoreEmpty);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 获取每一行的文件内容 （1）如果文件不存在，直接返回空列表。
	 *
	 * @param inputStream 文件输入流
	 * @param charset     编码
	 * @param initLine    初始化行
	 * @param endLine     结束航
	 * @param ignoreEmpty 是否跳过空白行
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final InputStream inputStream,
		final String charset,
		final int initLine,
		final int endLine,
		final boolean ignoreEmpty) {
		ArgUtils.notNull(inputStream, "inputStream");
		ArgUtils.notEmpty(charset, "charset");

		List<String> contentList = new LinkedList<>();
		try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
		) {
			// 用于记录行号
			int lineNo = 0;
			while (lineNo < initLine) {
				lineNo++;
				String ignore = bufferedReader.readLine();
			}

			//每一行的内容
			String dataEachLine;
			while ((dataEachLine = bufferedReader.readLine()) != null
				&& lineNo < endLine) {
				lineNo++;

				// 跳过空白行且内容为空，则不进行计入结果
				if (ignoreEmpty && StringUtils.isEmpty(dataEachLine)) {
					// ignore
					continue;
				} else {
					contentList.add(dataEachLine);
				}
			}
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return contentList;
	}

	/**
	 * 获取每一行的文件内容 （1）如果文件不存在，直接返回空列表。
	 *
	 * @param inputStream 文件输入流
	 * @param charset     编码
	 * @param initLine    初始化行
	 * @param endLine     结束航
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final InputStream inputStream,
		final String charset,
		final int initLine,
		final int endLine) {
		return readAllLines(inputStream, charset, initLine, endLine, true);
	}

	/**
	 * 获取每一行的文件内容 （1）如果文件不存在，直接返回空列表。
	 *
	 * @param inputStream 文件输入流
	 * @param charset     编码
	 * @param initLine    初始化行
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final InputStream inputStream,
		final String charset,
		final int initLine) {
		return readAllLines(inputStream, charset, initLine, Integer.MAX_VALUE);
	}

	/**
	 * 获取每一行的文件内容 （1）如果文件不存在，直接返回空列表。
	 *
	 * @param inputStream 文件输入流
	 * @param charset     编码
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final InputStream inputStream,
		final String charset) {
		return readAllLines(inputStream, charset, 0);
	}

	/**
	 * 获取每一行的文件内容 （1）如果文件不存在，直接返回空列表。
	 *
	 * @param inputStream 文件输入流
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final InputStream inputStream) {
		return readAllLines(inputStream, CommonConstant.UTF8);
	}

	/**
	 * 获取每一行的文件内容
	 *
	 * @param filePath    文件路径
	 * @param charset     文件编码
	 * @param ignoreEmpty 是否跳过空白行
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final String filePath,
		final String charset,
		final boolean ignoreEmpty) {
		File file = new File(filePath);
		return readAllLines(file, charset, 0, Integer.MAX_VALUE, ignoreEmpty);
	}

	/**
	 * 获取每一行的文件内容
	 *
	 * @param file        文件路径
	 * @param charset     文件编码
	 * @param ignoreEmpty 是否跳过空白行
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final File file,
		final String charset,
		final boolean ignoreEmpty) {
		return readAllLines(file, charset, 0, Integer.MAX_VALUE, ignoreEmpty);
	}

	/**
	 * 获取每一行的文件内容
	 *
	 * @param file    文件路径
	 * @param charset 文件编码
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final File file,
		final String charset) {
		return readAllLines(file, charset, false);
	}


	/**
	 * 获取每一行的文件内容
	 *
	 * @param file 文件路径
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final File file) {
		return readAllLines(file, CommonConstant.UTF8);
	}

	/**
	 * 获取每一行的文件内容
	 *
	 * @param filePath 文件路径
	 * @param charset  文件编码
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final String filePath,
		final String charset) {
		return readAllLines(filePath, charset, false);
	}


	/**
	 * 获取每一行的文件内容
	 *
	 * @param filePath 文件路径
	 * @return 结果列表
	 */
	public static List<String> readAllLines(final String filePath) {
		return readAllLines(filePath, CommonConstant.UTF8);
	}

	/**
	 * 复制文件夹
	 *
	 * @param sourceDir 原始文件夹
	 * @param targetDir 目标文件夹
	 * @throws IOException if any
	 */
	public static void copyDir(String sourceDir, String targetDir) throws IOException {
		File file = new File(sourceDir);
		String[] filePath = file.list();

		if (!(new File(targetDir)).exists()) {
			(new File(targetDir)).mkdir();
		}

		if (ArrayUtils.isNotEmpty(filePath)) {

			for (String aFilePath : filePath) {
				if ((new File(sourceDir + File.separator + aFilePath)).isDirectory()) {
					copyDir(sourceDir + File.separator + aFilePath,
						targetDir + File.separator + aFilePath);
				}

				if (new File(sourceDir + File.separator + aFilePath).isFile()) {
					copyFile(sourceDir + File.separator + aFilePath,
						targetDir + File.separator + aFilePath);
				}

			}

		}

	}


	/**
	 * 复制文件
	 *
	 * @param sourceFile 原始路径
	 * @param targetPath 目标路径
	 * @throws IOException if any
	 */
	public static void copyFile(String sourceFile, String targetPath) throws IOException {
		File oldFile = new File(sourceFile);
		File file = new File(targetPath);

		try (FileInputStream in = new FileInputStream(oldFile);
			FileOutputStream out = new FileOutputStream(file)) {

			byte[] buffer = new byte[2097152];

			while ((in.read(buffer)) != -1) {
				out.write(buffer);
			}

		}

	}

	/**
	 * 写入文件信息 （1）默认 utf-8 编码 （2）默认新建一个文件 （3）默认为一行
	 *
	 * @param filePath    文件路径
	 * @param line        行信息
	 * @param openOptions 操作属性
	 */
	public static void write(final String filePath, final CharSequence line,
		OpenOption... openOptions) {
		write(filePath, Collections.singletonList(line), openOptions);
	}

	/**
	 * 写入文件信息 （1）默认 utf-8 编码 （2）默认新建一个文件
	 *
	 * @param filePath    文件路径
	 * @param lines       行信息
	 * @param openOptions 文件选项
	 */
	public static void write(final String filePath, final Iterable<? extends CharSequence> lines,
		OpenOption... openOptions) {
		write(filePath, lines, CommonConstant.UTF8, openOptions);
	}

	/**
	 * 写入文件信息
	 *
	 * @param filePath    文件路径
	 * @param lines       行信息
	 * @param charset     文件编码
	 * @param openOptions 文件操作选项
	 */
	public static void write(final String filePath, final Iterable<? extends CharSequence> lines,
		final String charset, OpenOption... openOptions) {
		try {
			// ensure lines is not null before opening file
			ArgUtils.notNull(lines, "charSequences");
			CharsetEncoder encoder = Charset.forName(charset).newEncoder();
			final Path path = Paths.get(filePath);

			// 创建父类文件夹
			Path pathParent = path.getParent();
			// 路径判断空
			if (pathParent != null) {
				File parent = pathParent.toFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
			}

			OutputStream out = path.getFileSystem().provider().newOutputStream(path, openOptions);
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, encoder))) {
				for (CharSequence line : lines) {
					writer.append(line);
					writer.newLine();
				}
			}
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 创建文件 （1）文件路径为空，则直接返回 false （2）如果文件已经存在，则返回 true （3）如果文件不存在，则创建文件夹，然后创建文件。 3.1
	 * 如果父类文件夹创建失败，则直接返回 false.
	 *
	 * @param filePath 文件路径
	 * @return 是否成功
	 * @throws CommonRuntimeException 运行时异常，如果创建文件异常。包括的异常为 {@link IOException} 文件异常.
	 */
	public static boolean createFile(final String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return false;
		}

		if (FileUtils.exists(filePath)) {
			return true;
		}

		File file = new File(filePath);

		// 父类文件夹的处理
		File dir = file.getParentFile();
		if (dir != null && FileUtils.notExists(dir)) {
			boolean mkdirResult = dir.mkdirs();
			if (!mkdirResult) {
				return false;
			}
		}
		// 创建文件
		try {
			return file.createNewFile();
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}


	/**
	 * 文件是否存在
	 *
	 * @param filePath 文件路径
	 * @param options  连接选项
	 * @return 是否存在
	 */
	public static boolean exists(final String filePath, LinkOption... options) {
		if (StringUtils.isEmpty(filePath)) {
			return false;
		}

		Path path = Paths.get(filePath);
		return Files.exists(path, options);
	}

	/**
	 * 文件是否不存在
	 *
	 * @param filePath 文件路径
	 * @param options  连接选项
	 * @return 是否存在
	 */
	public static boolean notExists(final String filePath, LinkOption... options) {
		return !exists(filePath, options);
	}


	/**
	 * 文件是否不存在
	 *
	 * @param file 文件
	 * @return 是否存在
	 */
	public static boolean notExists(final File file) {
		ArgUtils.notNull(file, "file");
		return !file.exists();
	}

	/**
	 * 判断文件是否为空 （1）文件不存在，返回 true （2）文件存在，且 {@link File#length()} 为0，则认为空。 （3）文件存在，且length大于0，则认为不空
	 *
	 * @param filePath 文件路径
	 * @return 内容是否为空
	 */
	public static boolean isEmpty(final String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return true;
		}
		File file = new File(filePath);
		return file.length() <= 0;
	}

	/**
	 * 内容是否为不空
	 *
	 * @param filePath 文件路径
	 * @return 内容是否为不空
	 */
	public static boolean isNotEmpty(final String filePath) {
		return !isEmpty(filePath);
	}

	/**
	 * 获取文件字节数组
	 *
	 * @param file 文件信息
	 * @return 字节数组
	 */
	public static byte[] getFileBytes(final File file) {
		ArgUtils.notNull(file, "file");

		try (FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 获取文件字节流
	 *
	 * @param filePath 文件路径
	 * @return 字节数组
	 */
	public static byte[] getFileBytes(final String filePath) {
		ArgUtils.notNull(filePath, "filePath");

		File file = new File(filePath);
		return getFileBytes(file);
	}

	/**
	 * 根据字节信息创建文件
	 *
	 * @param filePath 文件路径
	 * @param bytes    字节数组
	 * @see #createFileAssertSuccess 断言创建成功
	 */
	public static void createFile(final String filePath, final byte[] bytes) {
		File file = createFileAssertSuccess(filePath);
		try (FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);) {
			bos.write(bytes);
		} catch (Exception e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 创建文件
	 *
	 * @param filePath 文件路径
	 * @return 文件信息
	 * @throws CommonRuntimeException 运行时异常，如果创建文件异常。包括的异常为 {@link IOException} 文件异常.
	 */
	public static File createFileAssertSuccess(final String filePath) {
		ArgUtils.notEmpty(filePath, "filePath");

		// 判断文件是否存在
		File file = new File(filePath);
		if (file.exists()) {
			return file;
		}

		// 父类文件夹的处理
		File dir = file.getParentFile();
		if (FileUtils.notExists(dir)) {
			boolean mkdirResult = dir.mkdirs();
			if (!mkdirResult) {
				throw new CommonRuntimeException("Parent file create fail " + filePath);
			}
		}

		try {
			// 创建文件
			boolean createFile = file.createNewFile();
			if (!createFile) {
				throw new CommonRuntimeException("Create new file fail for path " + filePath);
			}
			return file;
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 删除文件
	 *
	 * @param file 文件信息
	 */
	public static void deleteFile(final File file) {
		ArgUtils.notNull(file, "file");

		if (file.exists()) {
			boolean result = file.delete();
			if (!result) {
				throw new CommonRuntimeException(
					"Delete file fail for path " + file.getAbsolutePath());
			}
		}
	}

	/**
	 * 删除文件
	 *
	 * @param filePath 文件信息
	 */
	public static void deleteFile(final String filePath) {
		ArgUtils.notEmpty(filePath, "filePath");
		File file = new File(filePath);
		deleteFile(file);
	}

	/**
	 * 创建临时文件
	 *
	 * @param name   文件名称
	 * @param suffix 文件后缀
	 * @return 临时文件
	 */
	public static File createTempFile(final String name, final String suffix) {
		try {
			ArgUtils.notEmpty(name, "prefix");
			ArgUtils.notEmpty(suffix, "suffix");

			return File.createTempFile(name, suffix);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 创建临时文件
	 *
	 * @param nameWithSuffix 文件名称全称
	 * @return 临时文件
	 */
	public static File createTempFile(final String nameWithSuffix) {
		try {
			ArgUtils.notEmpty(nameWithSuffix, "fileName");
			String[] strings = nameWithSuffix.split("\\.");
			return File.createTempFile(strings[0], strings[1]);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 是否为图片格式
	 *
	 * @param string 原始字符串
	 * @return 是否为图片
	 */
	public static boolean isImage(final String string) {
		if (StringUtils.isEmpty(string)) {
			return false;
		}

		return string.endsWith(FileTypeConst.Image.PNG)
			|| string.endsWith(FileTypeConst.Image.JPEG)
			|| string.endsWith(FileTypeConst.Image.JPG)
			|| string.endsWith(FileTypeConst.Image.GIF);
	}

	/**
	 * 将文件内容转换为 map
	 *
	 * @param inputStream 输入流
	 * @param charset     文件编码
	 * @param mapHandler  转换实现
	 * @param <K>         key 泛型
	 * @param <V>         value 泛型
	 * @return 结果
	 */
	public static <K, V> Map<K, V> readToMap(final InputStream inputStream,
		final String charset,
		final IMapHandler<K, V, String> mapHandler) {
		List<String> allLines = FileUtils.readAllLines(inputStream, charset);
		return MapUtils.toMap(allLines, mapHandler);
	}

	/**
	 * 将文件内容转换为 map
	 *
	 * @param path       文件路径
	 * @param charset    文件编码
	 * @param mapHandler 转换实现
	 * @param <K>        key 泛型
	 * @param <V>        value 泛型
	 * @return 结果
	 */
	public static <K, V> Map<K, V> readToMap(final String path,
		final String charset,
		final IMapHandler<K, V, String> mapHandler) {
		List<String> allLines = FileUtils.readAllLines(path, charset);
		return MapUtils.toMap(allLines, mapHandler);
	}

	/**
	 * 将文件内容转换为 map
	 *
	 * @param path       文件路径
	 * @param mapHandler 转换实现
	 * @param <K>        key 泛型
	 * @param <V>        value 泛型
	 * @return 结果
	 */
	public static <K, V> Map<K, V> readToMap(final String path,
		final IMapHandler<K, V, String> mapHandler) {
		return readToMap(path, CommonConstant.UTF8, mapHandler);
	}

	/**
	 * 将文件内容转换为 map
	 * <p>
	 * （1）直接拆分。取第一个值和第一个值 （2）默认使用空格分隔
	 *
	 * @param path 文件路径
	 * @return 结果
	 */
	public static Map<String, String> readToMap(final String path) {
		return readToMap(path, " ");
	}

	/**
	 * 将文件内容转换为 map
	 * <p>
	 * （1）直接拆分。取第一个值和第一个值
	 *
	 * @param path     文件路径
	 * @param splitter 分隔符号
	 * @return 结果
	 */
	public static Map<String, String> readToMap(final String path,
		final String splitter) {
		return readToMap(path, new IMapHandler<String, String, String>() {
			@Override
			public String getKey(String o) {
				return o.split(splitter)[0];
			}

			@Override
			public String getValue(String o) {
				return o.split(splitter)[1];
			}
		});
	}

	/**
	 * 获取文件名称
	 *
	 * @param path 完整路径
	 * @return 名称
	 */
	public static String getFileName(final String path) {
		if (StringUtils.isEmptyTrim(path)) {
			return StringUtils.EMPTY;
		}

		File file = new File(path);
		String name = file.getName();

		return name.substring(0, name.lastIndexOf('.'));
	}

	/**
	 * 获取父类路径
	 *
	 * @param path 当前路径
	 * @return 父类路径
	 */
	public static String getDirPath(final String path) {
		Path path1 = Paths.get(path);
		return path1.getParent().toAbsolutePath().toString() + File.separator;
	}


	/**
	 * 移除 windows 中禁止出现的特殊符号名称
	 *
	 * @param name 名称
	 * @return 结果
	 */
	public static String trimWindowsSpecialChars(final String name) {
		if (StringUtils.isEmpty(name)) {
			return name;
		}

		return name.replaceAll("[?/\\\\*<>|:\"]", "");
	}

	/**
	 * 重命名
	 *
	 * @param sourcePath 原始路径
	 * @param targetPath 结果路径
	 * @return 重命名结果
	 */
	public static boolean rename(final String sourcePath,
		final String targetPath) {
		File sourceFile = new File(sourcePath);
		File targetFile = new File(targetPath);
		return sourceFile.renameTo(targetFile);
	}

	/**
	 * 文件合并
	 *
	 * @param result  结果路径
	 * @param sources 其他待合并文件路径
	 */
	public static void merge(final String result,
		final String... sources) {
		ArgUtils.notEmpty(result, "result");
		ArgUtils.notEmpty(sources, "sources");

		try (OutputStream os = new FileOutputStream(result)) {
			for (String source : sources) {
				byte[] bytes = getFileBytes(source);
				os.write(bytes);
			}
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 将指定的数组信息合并到指定的文件中
	 *
	 * @param result     结果路径
	 * @param byteArrays 其他待合并文件路径
	 */
	public static void merge(final String result,
		final byte[]... byteArrays) {
		ArgUtils.notEmpty(result, "result");
		ArgUtils.notEmpty(byteArrays, "byteArrays");

		try (OutputStream os = new FileOutputStream(result)) {
			for (byte[] bytes : byteArrays) {
				os.write(bytes);
			}
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 将指定的数组信息合并到指定的文件中
	 *
	 * @param result        结果路径
	 * @param byteArrayList 其他待合并文件字节数组
	 */
	public static void merge(final String result,
		final List<byte[]> byteArrayList) {
		ArgUtils.notEmpty(result, "result");
		ArgUtils.notEmpty(byteArrayList, "byteArrayList");

		try (OutputStream os = new FileOutputStream(result)) {
			for (byte[] bytes : byteArrayList) {
				os.write(bytes);
			}
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 写入字节到文件
	 *
	 * @param filePath 文件路径
	 * @param bytes    字节信息
	 */
	public static void write(final String filePath,
		final byte[] bytes) {
		ArgUtils.notEmpty(filePath, "filePath");

		try (OutputStream os = new FileOutputStream(filePath)) {
			os.write(bytes);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 转义 windows 下的特殊字符
	 *
	 * @param fileName 文件名称
	 * @return 转义后的字符串
	 */
	public static String escapeWindowsSpecial(final String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return fileName;
		}
		return fileName.replaceAll("[\"<>/\\\\|:*?]", "");
	}

	/**
	 * 创建文件夹
	 *
	 * @param dir 文件夹
	 * @return 结果
	 */
	public static boolean createDir(final String dir) {
		if (StringUtils.isEmpty(dir)) {
			return false;
		}

		File file = new File(dir);
		if (file.isDirectory()) {
			return file.mkdirs();
		}
		return false;
	}

	/**
	 * 清空文件内容
	 *
	 * @param filePath 文件路径
	 */
	public static void truncate(final String filePath) {
		FileUtils.write(filePath, StringUtils.EMPTY, StandardOpenOption.TRUNCATE_EXISTING);
	}

	/**
	 * 追加文件内容
	 *
	 * @param filePath 文件路径
	 * @param line     文件内容
	 */
	public static void append(final String filePath, final String line) {
		FileUtils.write(filePath, line, StandardOpenOption.APPEND);
	}

	/**
	 * 追加文件内容
	 *
	 * @param filePath   文件路径
	 * @param collection 文件内容
	 */
	public static void append(final String filePath, final Collection<String> collection) {
		FileUtils.write(filePath, collection, StandardOpenOption.APPEND);
	}

	/**
	 * 将文件转成 base64 字符串
	 * <p>
	 * https://www.cnblogs.com/darkhumor/p/7525392.html
	 * https://blog.csdn.net/phoenix_cat/article/details/84676302
	 * https://blog.csdn.net/myloverisxin/article/details/117530365
	 * https://www.cnblogs.com/yejg1212/p/11926649.html
	 * <p>
	 * 不同规范编码不同，会导致出现换行符号，但是解码的时候会被忽略。
	 *
	 * @param filePath 文件路径
	 * @return base64 字符串
	 */
	public static String fileToBase64(String filePath) {
		//File file = new File(filePath);
		//
		//try(FileInputStream inputFile = new FileInputStream(file)) {
		//    byte[] buffer = new byte[(int)file.length()];
		//    inputFile.read(buffer);
		//    String plainText = new BASE64Encoder().encode(buffer);
		//
		//    return plainText.replaceAll("\r", "")
		//            .replaceAll("\n", "");
		//} catch (IOException e) {
		//    throw new RuntimeException(e);
		//}
		return filePath;
	}

	/**
	 * 将base64字符解码保存文件
	 *
	 * @param base64Code base64 内容
	 * @param targetPath 目标文件
	 */
	public static void base64ToFile(String base64Code, String targetPath) {
		//FileUtil.createFile(targetPath);
		//
		//try(FileOutputStream out = new FileOutputStream(targetPath);) {
		//    byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
		//    out.write(buffer);
		//} catch (IOException e) {
		//    throw new RuntimeException(e);
		//}
	}

	/**
	 * 默认为true
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 19:41:13
	 */
	public static class TrueFilter implements FileFilter, Serializable {

		@Serial
		private static final long serialVersionUID = -6420452043795072619L;

		public final static TrueFilter TRUE = new TrueFilter();

		@Override
		public boolean accept(File pathname) {
			return true;
		}
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param path 路径
	 * @return 文件集合
	 */
	public static List<File> list(String path) {
		File file = new File(path);
		return list(file, TrueFilter.TRUE);
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param path            路径
	 * @param fileNamePattern 文件名 * 号
	 * @return 文件集合
	 */
	public static List<File> list(String path, final String fileNamePattern) {
		File file = new File(path);
		return list(file, pathname -> {
			String fileName = pathname.getName();
			return PatternMatchUtils.simpleMatch(fileNamePattern, fileName);
		});
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param path   路径
	 * @param filter 文件过滤
	 * @return 文件集合
	 */
	public static List<File> list(String path, FileFilter filter) {
		File file = new File(path);
		return list(file, filter);
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param file 文件
	 * @return 文件集合
	 */
	public static List<File> list(File file) {
		List<File> fileList = new ArrayList<>();
		return list(file, fileList, TrueFilter.TRUE);
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param file            文件
	 * @param fileNamePattern "xxx*", "*xxx", "*xxx*" and "xxx*yyy"
	 * @return 文件集合
	 */
	public static List<File> list(File file, final String fileNamePattern) {
		List<File> fileList = new ArrayList<>();
		return list(file, fileList, pathname -> {
			String fileName = pathname.getName();
			return PatternMatchUtils.simpleMatch(fileNamePattern, fileName);
		});
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param file             文件
	 * @param fileNamePatterns "xxx*", "*xxx", "*xxx*" and "xxx*yyy"
	 * @return 文件集合
	 */
	public static List<File> list(File file, final String... fileNamePatterns) {
		List<File> fileList = new ArrayList<>();
		return list(file, fileList, pathname -> {
			String fileName = pathname.getName();
			return PatternMatchUtils.simpleMatch(fileNamePatterns, fileName);
		});
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param file   文件
	 * @param filter 文件过滤
	 * @return 文件集合
	 */
	public static List<File> list(File file, FileFilter filter) {
		List<File> fileList = new ArrayList<>();
		return list(file, fileList, filter);
	}

	/**
	 * 扫描目录下的文件
	 *
	 * @param file   文件
	 * @param filter 文件过滤
	 * @return 文件集合
	 */
	private static List<File> list(File file, List<File> fileList, FileFilter filter) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					list(f, fileList, filter);
				}
			}
		} else {
			// 过滤文件
			boolean accept = filter.accept(file);
			if (file.exists() && accept) {
				fileList.add(file);
			}
		}
		return fileList;
	}

	/**
	 * 获取文件后缀名
	 *
	 * @param path 文件路径
	 * @return {String}
	 */
	@Nullable
	public static String getFilename(@Nullable String path) {
		return org.springframework.util.StringUtils.getFilename(path);
	}

	/**
	 * 获取文件后缀名 jpg
	 *
	 * @param fullName 文件全名
	 * @return {String}
	 */
	@Nullable
	public static String getFileExtension(@Nullable String fullName) {
		return org.springframework.util.StringUtils.getFilenameExtension(fullName);
	}

	/**
	 * 获取文件后缀名 .jpg
	 *
	 * @param fullName 文件全名
	 * @return {String}
	 */
	@Nullable
	public static String getFileExtensionWithDot(@Nullable String fullName) {
		if (fullName == null) {
			return null;
		}

		int extIndex = fullName.lastIndexOf(CharPool.DOT);
		if (extIndex == -1) {
			return null;
		}

		int folderIndex = fullName.lastIndexOf(CharPool.SLASH);
		if (folderIndex > extIndex) {
			return null;
		}

		return fullName.substring(extIndex);
	}

	/**
	 * 获取文件名，去除后缀名
	 *
	 * @param path 文件
	 * @return {String}
	 */
	@Nullable
	public static String getPathWithoutExtension(@Nullable String path) {
		if (path == null) {
			return null;
		}
		return org.springframework.util.StringUtils.stripFilenameExtension(path);
	}

	/**
	 * Returns the path to the system temporary directory.
	 *
	 * @return the path to the system temporary directory.
	 */
	public static String getTempDirPath() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 拼接临时文件目录.
	 *
	 * @return 临时文件目录.
	 */
	public static String toTempDirPath(String subDirFile) {
		return FileUtils.toTempDir(subDirFile).getAbsolutePath();
	}

	/**
	 * Returns a {@link File} representing the system temporary directory.
	 *
	 * @return the system temporary directory.
	 */
	public static File getTempDir() {
		return new File(getTempDirPath());
	}

	/**
	 * 拼接临时文件目录.
	 *
	 * @return the system temporary directory.
	 */
	public static File toTempDir(String subDirFile) {
		String tempDirPath = FileUtils.getTempDirPath();
		if (subDirFile.startsWith(StringPool.SLASH)) {
			subDirFile = subDirFile.substring(1);
		}
		String fullPath = tempDirPath.concat(subDirFile);
		File fullFilePath = new File(fullPath);
		File dir = fullFilePath.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return fullFilePath;
	}

	/**
	 * Reads the contents of a file into a String. The file is always closed.
	 *
	 * @param file the file to read, must not be {@code null}
	 * @return the file contents, never {@code null}
	 */
	public static String readToString(final File file) {
		return readToString(file, StandardCharsets.UTF_8);
	}

	/**
	 * Reads the contents of a file into a String. The file is always closed.
	 *
	 * @param file     the file to read, must not be {@code null}
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @return the file contents, never {@code null}
	 */
	public static String readToString(final File file, final Charset encoding) {
		try (InputStream in = Files.newInputStream(file.toPath())) {
			return IoUtils.readToString(in, encoding);
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Reads the contents of a file into a String. The file is always closed.
	 *
	 * @param file the file to read, must not be {@code null}
	 * @return the file contents, never {@code null}
	 */
	public static byte[] readToByteArray(final File file) {
		try {
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Writes a String to a file creating the file if it does not exist.
	 *
	 * @param file the file to write
	 * @param data the content to write to the file
	 */
	public static void writeToFile(final File file, final String data) {
		writeToFile(file, data, StandardCharsets.UTF_8, false);
	}

	/**
	 * Writes a String to a file creating the file if it does not exist.
	 *
	 * @param file   the file to write
	 * @param data   the content to write to the file
	 * @param append if {@code true}, then the String will be added to the end of the file rather
	 *               than overwriting
	 */
	public static void writeToFile(final File file, final String data, final boolean append) {
		writeToFile(file, data, StandardCharsets.UTF_8, append);
	}

	/**
	 * Writes a String to a file creating the file if it does not exist.
	 *
	 * @param file     the file to write
	 * @param data     the content to write to the file
	 * @param encoding the encoding to use, {@code null} means platform default
	 */
	public static void writeToFile(final File file, final String data, final Charset encoding) {
		writeToFile(file, data, encoding, false);
	}

	/**
	 * Writes a String to a file creating the file if it does not exist.
	 *
	 * @param file     the file to write
	 * @param data     the content to write to the file
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @param append   if {@code true}, then the String will be added to the end of the file rather
	 *                 than overwriting
	 */
	public static void writeToFile(final File file, final String data, final Charset encoding,
		final boolean append) {
		try (OutputStream out = new FileOutputStream(file, append)) {
			IoUtils.write(data, out, encoding);
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 转成file
	 *
	 * @param multipartFile MultipartFile
	 * @param file          File
	 */
	public static void toFile(MultipartFile multipartFile, final File file) {
		try {
			FileUtils.toFile(multipartFile.getInputStream(), file);
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 转成file
	 *
	 * @param in   InputStream
	 * @param file File
	 */
	public static void toFile(InputStream in, final File file) {
		try (OutputStream out = new FileOutputStream(file)) {
			FileUtils.copy(in, out);
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Moves a file.
	 * <p>
	 * When the destination file is on another file system, do a "copy and delete".
	 *
	 * @param srcFile  the file to be moved
	 * @param destFile the destination file
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException          if source or destination is invalid
	 * @throws IOException          if an IO error occurs moving the file
	 */
	public static void moveFile(final File srcFile, final File destFile) throws IOException {
		Assert.notNull(srcFile, "Source must not be null");
		Assert.notNull(destFile, "Destination must not be null");
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' is a directory");
		}
		if (destFile.exists()) {
			throw new IOException("Destination '" + destFile + "' already exists");
		}
		if (destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' is a directory");
		}
		final boolean rename = srcFile.renameTo(destFile);
		if (!rename) {
			FileUtils.copy(srcFile, destFile);
			if (!srcFile.delete()) {
				FileUtils.deleteQuietly(destFile);
				throw new IOException(
					"Failed to delete original file '" + srcFile + "' after copy to '" + destFile
						+ "'");
			}
		}
	}

	/**
	 * Deletes a file, never throwing an exception. If file is a directory, delete it and all
	 * sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
	 * </ul>
	 *
	 * @param file file or directory to delete, can be {@code null}
	 * @return {@code true} if the file or directory was deleted, otherwise {@code false}
	 */
	public static boolean deleteQuietly(@Nullable final File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				FileSystemUtils.deleteRecursively(file);
			}
		} catch (final Exception ignored) {
		}

		try {
			return file.delete();
		} catch (final Exception ignored) {
			return false;
		}
	}

	/**
	 * NIO 按行读取文件
	 *
	 * @param path 文件路径
	 * @return 行列表
	 */
	public static List<String> readLines(String path) {
		return readLines(Paths.get(path));
	}

	/**
	 * NIO 按行读取文件
	 *
	 * @param file 文件
	 * @return 行列表
	 */
	public static List<String> readLines(File file) {
		return readLines(file.toPath());
	}

	/**
	 * NIO 按行读取文件
	 *
	 * @param path 文件路径
	 * @return 行列表
	 */
	public static List<String> readLines(Path path) {
		return readLines(path, StandardCharsets.UTF_8);
	}

	/**
	 * NIO 按行读取文件
	 *
	 * @param path 文件路径
	 * @param cs   字符集
	 * @return 行列表
	 */
	public static List<String> readLines(String path, Charset cs) {
		return readLines(Paths.get(path), cs);
	}

	/**
	 * NIO 按行读取文件
	 *
	 * @param file 文件
	 * @param cs   字符集
	 * @return 行列表
	 */
	public static List<String> readLines(File file, Charset cs) {
		return readLines(file.toPath(), cs);
	}

	/**
	 * NIO 按行读取文件
	 *
	 * @param path 文件路径
	 * @param cs   字符集
	 * @return 行列表
	 */
	public static List<String> readLines(Path path, Charset cs) {
		try {
			return Files.readAllLines(path, cs);
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 文件是否存在
	 *
	 * @param filepath 文件路径
	 * @return boolean
	 * @since 2021-09-02 16:46:10
	 */
	public static boolean existFile(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}

	/**
	 * 获取文件目录路径
	 *
	 * @param path 文件路径
	 * @return 文件目录路径
	 * @since 2021-09-02 16:46:03
	 */
	public static String getDirectoryPath(String path) {
		File file = new File(path);
		return file.getAbsolutePath();
	}

	/**
	 * 获取文件目录路径
	 *
	 * @param cls cls
	 * @return 文件目录路径
	 * @since 2021-09-02 16:45:57
	 */
	public static String getDirectoryPath(Class<?> cls) {
		File file = getJarFile(cls);
		if (file == null) {
			return null;
		}
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		return file.getAbsolutePath();
	}

	/**
	 * 获取文件
	 *
	 * @param cls 类型
	 * @return 文件对象
	 * @since 2021-09-02 16:45:49
	 */
	public static File getJarFile(Class<?> cls) {
		String path = cls.getProtectionDomain().getCodeSource().getLocation().getFile();
		try {
			// 转换处理中文及空格
			path = java.net.URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return new File(path);
	}

	// /**
	//  * 获取文件路径
	//  *
	//  * @param paths 路径列表
	//  * @return java.lang.String
	//  * @since 2020/10/15 15:03
	//  * @version 2022.03
	//  */
	// public String getFilePath(String... paths) {
	//     StringBuilder sb = new StringBuilder();
	//     for (String path : paths) {
	//         sb.append(org.springframework.util.StringUtils.trimTrailingCharacter(path, File.separatorChar));
	//         sb.append(File.separator);
	//     }
	//     return org.springframework.util.StringUtils.trimTrailingCharacter(sb.toString(), File.separatorChar);
	// }


	/**
	 * 创建目录
	 *
	 * @param path 文件路径
	 * @return 是否成功
	 * @since 2021-09-02 16:45:40
	 */
	public static Boolean createDirectory(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		if (!file.exists()) {
			return file.mkdirs();
		} else {
			return false;
		}
	}

	/**
	 * 追加文件内容
	 *
	 * @param path     文件路径
	 * @param contents 内容
	 * @since 2021-09-02 16:45:30
	 */
	public static void appendAllText(String path, String contents) {
		try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
			File f = new File(path);
			try (FileWriter fw = new FileWriter(f, true)) {
				try (PrintWriter pw = new PrintWriter(fw)) {
					pw.println(contents);
					pw.flush();
					fw.flush();
				}
			}
		} catch (IOException exp) {
			throw new BaseException("追加文件异常", exp);
		}
	}

	/**
	 * 写文件内容
	 *
	 * @param path     文件路径
	 * @param contents 内容
	 * @since 2021-09-02 16:45:20
	 */
	public static void writeAllText(String path, String contents) {
		try {
			File f = new File(path);
			if (f.exists()) {
				f.delete();
			}

			if (f.createNewFile()) {
				try (BufferedWriter output = new BufferedWriter(new FileWriter(f))) {
					output.write(contents);
				}
			}
		} catch (IOException exp) {
			throw new BaseException("写文件异常", exp);
		}
	}

	/**
	 * 读取文件内容
	 *
	 * @param path 文件路径
	 * @return 文件路径
	 * @since 2021-09-02 16:45:10
	 */
	public static String readAllText(String path) {
		try {
			File f = new File(path);
			if (f.exists()) {
				//获取文件长度
				long fileLength = f.length();
				byte[] fileContent = new byte[(int) fileLength];
				try (FileInputStream in = new FileInputStream(f)) {
					in.read(fileContent);
				}
				//返回文件内容,默认编码
				return new String(fileContent);
			} else {
				throw new FileNotFoundException(path);
			}
		} catch (IOException exp) {
			throw new BaseException("读文件异常", exp);
		}
	}

	/**
	 * 获取行分隔符
	 *
	 * @return 获取行分隔符
	 * @since 2021-09-02 16:45:02
	 */
	public static String lineSeparator() {
		return System.getProperty("line.separator");
	}

	///**
	// * 根据文件路径获取文件名
	// *
	// * @param filePath 文件路径
	// * @return 文件路径
	// * @since 2021-09-02 16:44:55
	// */
	//public static String getFileName(String filePath) {
	//	String path = filePath.replace("\\\\", "/");
	//	return path.substring(path.lastIndexOf("/") + 1, path.length());
	//}

	/**
	 * 根据原图生成规定尺寸的图片
	 *
	 * @param url    连接
	 * @param width  宽
	 * @param height 高
	 * @return 缩略图全路径
	 */
	public static String getUrl(String url, Integer width, Integer height) {
		//缩略图全路径
		return url + "?x-oss-process=style/" + width + "X" + height;
	}

}
