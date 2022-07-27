package com.taotao.cloud.common.utils.io;


import com.google.common.collect.Lists;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.FileTypeConst;
import com.taotao.cloud.common.constant.PathConst;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 路径工具类
 */
public final class PathUtil {

	private PathUtil() {
	}

	/**
	 * 根路径
	 */
	public static final Path ROOT_PATH = Paths.get("/");

	/**
	 * 获取 path 相对于 parentPath 剩余的路径 和 {@link Path#relativize(Path)} 不同，这个结果更加直观。不过性能一般。
	 *
	 * @param parentPath 父类路径
	 * @param path       原始路径
	 * @return 相对结果路径
	 */
	public static String getRelativePath(final Path parentPath,
		final Path path) {
		final String pathStr = path.toString();
		if (ObjectUtil.isNull(parentPath)) {
			return pathStr;
		}

		// 认为是根路径
		if (parentPath.toString().length() <= 1) {
			return pathStr;
		}

		final String parentPathStr = parentPath.toString();
		if (pathStr.startsWith(parentPathStr)) {
			return pathStr.substring(parentPathStr.length() + 1);
		}
		return pathStr;
	}

	/**
	 * 获取共有的路径
	 *
	 * @param pathList 路径列表
	 * @return 结果
	 */
	public static Path getPublicParentPath(final List<Path> pathList) {
		// 直接返回第一个元素的父类路径即可、
		if (pathList.size() == 1) {
			return getParentPath(pathList.get(0));
		}

		// 获取所有的父类文件夹
		List<List<String>> pathStrList = new ArrayList<>(pathList.size());
		for (Path path : pathList) {
			List<String> stringList = CollectionUtil.toStringList(getParentPaths(path));
			pathStrList.add(stringList);
		}

		// 获取共有的父类文件夹
		List<String> publicParentPathStrs = retainAll(pathStrList);

		// 获取最长的一个作为最大的公共路径
		String maxLengthParent = getMaxLength(publicParentPathStrs);
		return Paths.get(maxLengthParent);
	}

	/**
	 * 获取最长的字符串
	 *
	 * @param stringList 字符串列表
	 * @return 最长的结果
	 */
	private static String getMaxLength(final List<String> stringList) {
		String result = StringUtil.EMPTY;

		for (String string : stringList) {
			if (string.length() > result.length()) {
				result = string;
			}
		}
		return result;
	}

	/**
	 * 获取所有的父类路径 1. 不包含本身 2. 递归获取父类，如果父类为 null 则停止（说明到 root 了） 3. 默认 / root 的是所有逻辑的父亲路径，包括 root
	 * 文件夹本身。
	 *
	 * @param path 当前路径
	 * @return 所有的父类列表
	 */
	public static List<Path> getParentPaths(final Path path) {
		if (ObjectUtil.isNull(path)) {
			return Collections.emptyList();
		}

		List<Path> pathList = new ArrayList<>();
		Path parentPath = path.getParent();
		while (ObjectUtil.isNotNull(parentPath)) {
			pathList.add(parentPath);

			parentPath = parentPath.getParent();
		}

		// 如果列表为空，则默认添加 /
		if (CollectionUtil.isEmpty(pathList)) {
			pathList.add(ROOT_PATH);
		}

		return pathList;
	}


	/**
	 * 获取父类路径，避免返回 null 1. 如果为根路径，则依然返回根路径
	 *
	 * @param path 路径
	 * @return 结果
	 */
	public static Path getParentPath(final Path path) {
		Path parentPath = path.getParent();
		if (ObjectUtil.isNull(parentPath)) {
			return ROOT_PATH;
		}
		return parentPath;
	}

	/**
	 * 获取所有集合的交集 1. 如果后续参数为空，则直接返回第一个集合。 2. 如果第一个列表为空，则直接返回第一个集合。
	 *
	 * @param collectionList 原始对象集合
	 * @return 满足条件的结合
	 */
	public static List<String> retainAll(final List<List<String>> collectionList) {
		if (CollectionUtil.isEmpty(collectionList)) {
			return Collections.emptyList();
		}
		if (collectionList.size() == 1) {
			return collectionList.get(0);
		}

		List<String> result = collectionList.get(0);
		for (int i = 1; i < collectionList.size(); i++) {
			result.retainAll(collectionList.get(i));
		}

		return result;
	}

	/**
	 * 递归获取所有对应的文件 1. 如果为文件，直接返回本身 2. 如果为文件夹，则递归获取下面的所有文件信息
	 *
	 * @param rootPath 根路径
	 * @return 文件列表
	 */
	public static List<Path> getPathList(final Path rootPath) {
		final List<Path> pathList = new ArrayList<>();

		try {
			if (Files.isDirectory(rootPath)) {
				Files.walkFileTree(rootPath, new SimpleFileVisitor<>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
						throws IOException {
						pathList.add(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc)
						throws IOException {
						pathList.add(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			} else {
				pathList.add(rootPath);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return pathList;
	}

	/**
	 * 获得对应的PATH列表。
	 *
	 * @param dir  文件夹
	 * @param glob 文件正则
	 * @return 路径列表
	 */
	public static List<Path> getPathList(String dir, String glob) {
		List<Path> list = new LinkedList<>();
		Path root = Paths.get(dir);

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(root, glob)) {
			for (Path path : stream) {
				list.add(path);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	/**
	 * 获取指定文件夹下对应的某类型文件
	 *
	 * @param dir  文件夹路径
	 * @param glob 文件正则表达式
	 * @return path list
	 */
	public static List<Path> getDirFileNames(String dir, String glob) {
		List<Path> list = new LinkedList<>();
		Path root = Paths.get(dir);

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(root, glob)) {
			for (Path path : stream) {
				list.add(path.getFileName());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return list;
	}


	/**
	 * 获取某一路径下的所有文件
	 *
	 * @param dir 文件夹
	 * @return 路径列表
	 * @see #getDirFileNames(String, String) 指定此处的glob为 *.*
	 */
	public static List<Path> getAllDirFileNames(String dir) {
		return getDirFileNames(dir, FileTypeConst.Glob.ALL);
	}


	/**
	 * 获得列表下对应的文件字符串形式
	 *
	 * @param dir  文件夹
	 * @param glob 文件正则
	 * @return 文件名称列表
	 */
	public static List<String> getDirFileNameStrs(String dir, String glob) {
		List<String> list = new LinkedList<>();
		Path root = Paths.get(dir);

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(root, glob)) {
			for (Path path : stream) {
				list.add(path.getFileName().toString());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	/**
	 * 结果不确定
	 *
	 * @return 路径
	 * @deprecated (因为结果具有不确定性)
	 */
	@Deprecated
	public static String getPath() {
		return System.getProperty("user.dir");
	}

	/**
	 * 获取的target路径
	 *
	 * @return 路径
	 */
	public static String getRootPath() {
		return Class.class.getClass().getResource("/").getPath();
	}

	/**
	 * 获取项目根路径。
	 */
	public static String getAppRootPath() {
		File emptyFile = new File("");
		return emptyFile.getAbsolutePath();
	}

	/**
	 * 获取资源文件默认存放路径。
	 *
	 * @return 根路径+/src/main/resources
	 */
	public static String getAppResourcesPath() {
		return getAppRootPath() + PathConst.SRC_MAIN_RESOURCES_PATH;
	}

	/**
	 * 获取测试类
	 *
	 * @return 转换后的路径
	 */
	public static String getAppTestResourcesPath() {
		return getAppRootPath() + "/src/test/resources";
	}

	/**
	 * 类似getPath(Class), 只是不包含类的路径,而是获取到当前类包的根路径。 如: filelist:/Users/houbinbin/IT/code/script-generator/script-generator-tool/target/classes/
	 * 转化为: /Users/houbinbin/IT/code/script-generator/script-generator-tool/src/main/java
	 *
	 * @param clazz 类
	 * @return 转换后的路径
	 */
	public static String getRootPath(Class clazz) {
		String uriPath = clazz.getResource(PathConst.ROOT_PATH).toString();
		return uriPath.replace(PathConst.FILE_PATH_PREFIX, "")
			.replace(PathConst.TARGET_CLASSES_PATH_SUFFIX, PathConst.SRC_MAIN_JAVA_PATH);
	}

	/**
	 * 直接class.getResource("")拿到的是编译后的路径。 如: filelist:/Users/houbinbin/IT/code/script-generator/script-generator-tool/target/classes/com/ryo/script-generator/util/
	 * 转化成: /Users/houbinbin/IT/code/script-generator/script-generator-tool/src/main/java/com/ryo/script-generator/util/
	 *
	 * @param clazz 类
	 * @return 转换后的路径
	 */
	public static String getPath(Class clazz) {
		String uriPath = clazz.getResource("").toString();
		return uriPath.replace(PathConst.FILE_PATH_PREFIX, "")
			.replace(PathConst.TARGET_CLASSES_PATH_SUFFIX, PathConst.SRC_MAIN_JAVA_PATH);
	}

	/**
	 * 将包名称转化为对应的路径 com.github.houbinbin TO: com/github/houbinbin
	 *
	 * @param packagePath 包名称
	 * @return 转换后的路径
	 */
	public static String packageToPath(final String packagePath) {
		return packagePath.replaceAll("\\.", "/");
	}

	/**
	 * 读取文件所有行的内容
	 *
	 * @param pathStr 路径
	 * @return 列表
	 */
	public static List<String> readAllLines(final String pathStr) {
		return readAllLines(pathStr, CommonConstant.UTF8);
	}

	/**
	 * 读取文件所有行的内容
	 *
	 * @param pathStr 路径
	 * @param charset 编码
	 * @return 列表
	 */
	public static List<String> readAllLines(final String pathStr,
		final String charset) {
		return readAllLines(pathStr, charset, 0, Integer.MAX_VALUE);
	}

	/**
	 * 读取文件所有的行
	 *
	 * @param pathStr    路径
	 * @param charset    编码
	 * @param startIndex 开始行下标
	 * @param endIndex   结束行下标
	 * @return 列表内容
	 */
	public static List<String> readAllLines(final String pathStr,
		final String charset,
		final int startIndex,
		int endIndex) {
		ArgUtil.notEmpty(pathStr, "pathStr");
		ArgUtil.notEmpty(charset, "charset");
		ArgUtil.assertTrue(endIndex >= startIndex, "endIndex >= startIndex");

		Path path = Paths.get(pathStr);
		try {
			List<String> allLines = Files.readAllLines(path, Charset.forName(charset));
			final int size = allLines.size();
			if (endIndex > size) {
				endIndex = size;
			}
			return allLines.subList(startIndex, endIndex);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 写入行内容到指定列
	 *
	 * @param pathStr 路径
	 * @param lines   行内容数组
	 */
	public static void writeLines(final String pathStr,
		final String... lines) {
		List<String> stringList = Lists.newArrayList(lines);
		writeLines(pathStr, stringList, CommonConstant.UTF8);
	}

	/**
	 * 写入行内容到指定文件
	 *
	 * @param pathStr 路径
	 * @param lines   行内容
	 */
	public static void writeLines(final String pathStr,
		final Collection<String> lines) {
		writeLines(pathStr, lines, CommonConstant.UTF8);
	}

	/**
	 * 行内容添加到到指定列
	 *
	 * @param pathStr 路径
	 * @param lines   行内容数组
	 */
	public static void appendLines(final String pathStr,
		final String... lines) {
		List<String> stringList = Lists.newArrayList(lines);
		writeLines(pathStr, stringList, CommonConstant.UTF8, StandardOpenOption.APPEND);
	}

	/**
	 * 行内容添加到到指定文件
	 *
	 * @param pathStr 路径
	 * @param lines   行内容
	 */
	public static void appendLines(final String pathStr,
		final Collection<String> lines) {
		writeLines(pathStr, lines, CommonConstant.UTF8, StandardOpenOption.APPEND);
	}

	/**
	 * 写入行内容到指定列
	 *
	 * @param pathStr     路径
	 * @param charset     编码
	 * @param lines       行内容
	 * @param openOptions 操作
	 */
	public static void writeLines(final String pathStr,
		final Collection<String> lines,
		final String charset,
		final OpenOption... openOptions) {
		ArgUtil.notEmpty(pathStr, "pathStr");
		ArgUtil.notEmpty(charset, "charset");
		ArgUtil.notEmpty(lines, "lines");

		try {
			Path path = Paths.get(pathStr);
			Files.write(path, lines, Charset.forName(charset), openOptions);
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
	}
}
