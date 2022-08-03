package com.taotao.cloud.sys.biz.api.controller.tools.core.utils;

import com.taotao.cloud.sys.biz.api.controller.tools.core.exception.ToolException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * 作者:sanri <br>
 * 时间:2017-4-24下午4:09:47<br>
 * 功能:zip 处理工具类,需要 apache-commons-compress 支持 <br>
 */
@Slf4j
public class ZipUtil {

	/**
	 * 
	 * 作者:sanri <br>
	 * 时间:2017-4-24下午4:11:31<br>
	 * 功能: 压缩文件 <br>
	 * @param dirOrFile 需要压缩的文件或目录
	 * @param zipFile 压缩文件路径和名称
	 */
	public static void zip(File dirOrFile, File zipFile) {
		List<File> files = new ArrayList<File>();
		findFiles(dirOrFile, files);
		String basePath = dirOrFile.getParent();
		ZipArchiveOutputStream zaos = null;
		try {
			zaos = new ZipArchiveOutputStream(zipFile);
			zaos.setUseZip64(Zip64Mode.AsNeeded);
			for (File file : files) {
				// 需要获取文件的相对路径,相对于打包路径
				String currentPath = file.getPath();
				String relativePath = currentPath.replace(basePath + File.separator, "");
				relativePath = relativePath.replaceAll("\\\\", "/");			//兼容linux 环境
				ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, relativePath);
				zaos.putArchiveEntry(zipArchiveEntry);
				if (file.isDirectory()) {
					zaos.closeArchiveEntry();
					continue;
				}
				FileInputStream inputStream = new FileInputStream(file);
				IOUtils.copy(inputStream, zaos);
				zaos.closeArchiveEntry();
				IOUtils.closeQuietly(inputStream);
			}
			zaos.finish();
		} catch (IOException e) {
			log.error("ZipUtil zip error: {}",e.getMessage(),e);
		} finally {
			IOUtils.closeQuietly(zaos);
		}
	}
	
	/**
	 * 
	 * 作者:sanri <br>
	 * 时间:2017-7-22下午3:13:08<br>
	 * 功能:对 zip 方法的优化,直接在当前目录创建压缩文件  <br>
	 * @param dirOrFile 需要压缩的文件或目录
	 * @return 
	 */
	public static File zip(File dirOrFile){
		File zipFile = new File(dirOrFile.getParentFile(),dirOrFile.getName()+".zip");
		zip(dirOrFile, zipFile);
		return zipFile;
	}

	/**
	 * 
	 * 作者:sanri <br>
	 * 时间:2017-4-24下午4:34:31<br>
	 * 功能:递归目录下所有文件 <br>
	 * 入参: <br>
	 */
	private static void findFiles(File dirOrFile, List<File> files) {
		if (dirOrFile.isDirectory()) {
			files.add(dirOrFile); // 把目录也加上
			File[] listFiles = dirOrFile.listFiles();
			for (File file : listFiles) {
				findFiles(file, files);
			}
		} else {
			files.add(dirOrFile);
		}
	}

	/**
	 * 
	 * 作者:sanri <br>
	 * 时间:2017-4-24下午5:10:32<br>
	 * 功能: zip 解压缩<br>
	 * 入参: <br>
	 */
	public static File unzip(File zipFile, File outputDir) throws IOException {
		if (zipFile == null){
			throw new ToolException("zip 文件不能为空");
		}
		if (outputDir == null){
			outputDir = zipFile.getParentFile();
		}
		if (!outputDir.isAbsolute()){
			outputDir = new File(zipFile.getParentFile(),outputDir.getPath());
		}
//		if (!outputDir.isDirectory()){
//			throw new ToolException("zip 解压输出路径必须是一个路径");
//		}
		outputDir.mkdirs();

		// 解压出来的最终路径
		File unzipDir = null;

		try(InputStream is = new FileInputStream(zipFile);
			ZipArchiveInputStream zais = new ZipArchiveInputStream(is);) {

			ArchiveEntry archiveEntry = null;
			while ((archiveEntry = zais.getNextEntry()) != null) {
				String entryFileName = archiveEntry.getName();
				File entryFile = new File(outputDir,entryFileName);
				if(archiveEntry.isDirectory()){
					entryFile.mkdirs();
					continue;
				}
				try(FileOutputStream fos = new FileOutputStream(entryFile);){
					IOUtils.copy(zais, fos);
				}
			}
		}

		return outputDir;
	}

	public static final class UnzipConfig{

	}

	/**
	 * Zip 文件, 用于压缩或者解压缩
	 */
	public static final class ZipFile implements Closeable{
		private File zipFile;
		private ZipArchiveOutputStream outputStream;

		public ZipFile(File zipFile) throws IOException {
			zipFile.getParentFile().mkdirs();

			final String extension = FilenameUtils.getExtension(zipFile.getName());
			if (!"zip".equalsIgnoreCase(extension)){
				// 如果没有 zip 后缀名, 自动添加 zip 后缀名
				this.zipFile = new File(zipFile.getParentFile(),zipFile.getName() + ".zip");
			}else {
				this.zipFile = zipFile;
			}

			// 如果 zip 文件不存在, 则创建文件用于压缩, 否则就是需要解压的文件
			if (!this.zipFile.exists()){
				this.zipFile.createNewFile();

				this.outputStream = new ZipArchiveOutputStream(this.zipFile);
				outputStream.setUseZip64(Zip64Mode.AsNeeded);
			}
		}

		/**
		 * 解压到目录
		 * @param file
		 * @return
		 * @throws IOException
		 */
		public File unzipTo(File file) throws IOException {
			if (!file.exists() || file.isDirectory()){
				throw new ToolException("目录不存在或者不是一个目录");
			}
			// 新建同名目录进行解压
			final File unpackDir = new File(file, zipFile.getName());

			try(InputStream is = new FileInputStream(zipFile);
				ZipArchiveInputStream zais = new ZipArchiveInputStream(is);) {

				ArchiveEntry archiveEntry = null;
				while ((archiveEntry = zais.getNextEntry()) != null) {
					String entryFileName = archiveEntry.getName();
					File entryFile = new File(unpackDir,entryFileName);
					if(archiveEntry.isDirectory()){
						entryFile.mkdirs();
						continue;
					}
					try(FileOutputStream fos = new FileOutputStream(entryFile);){
						IOUtils.copy(zais, fos);
					}
				}
			}

			return unpackDir;
		}

		/**
		 * 完成 zip 文件创建
		 * @throws IOException
		 */
		public void finishZip() throws IOException {
			if (outputStream != null){
				outputStream.finish();
			}
		}

		/**
		 * 添加文件列表到 zip 文件中
		 * @param files
		 */
		public void addFiles(File...files) throws IOException {
			for (File file : files) {
				if (file.isFile()){
					addFile(file,file.getName());
				}else if (file.isDirectory()){
					addDirectory(file,new OnlyPath(file.getParentFile()));
				}
			}
		}

		/**
		 * 添加文件列表并完成压缩
		 * @param files
		 * @throws IOException
		 */
		public void addFilesAndFinish(File...files) throws IOException {
			addFiles(files);

			finishZip();
		}

		/**
		 * 添加一个文件到 zip 文件中
		 * @param file
		 */
		protected void addFile(File file,String path) throws IOException {
			ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, path);
			outputStream.putArchiveEntry(zipArchiveEntry);
			try(final FileInputStream fileInputStream = new FileInputStream(file)){
				IOUtils.copy(fileInputStream,outputStream);
				outputStream.closeArchiveEntry();
			}
		}

		/**
		 * 添加一个目录到 zip 文件中
		 * @param dir
		 * @param path
		 */
		protected void addDirectory(File dir,OnlyPath path) throws IOException {
			final Collection<File> listFilesAndDirs = FileUtils.listFilesAndDirs(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			for (File listFilesAndDir : listFilesAndDirs) {
				final String relativePath = path.relativize(new OnlyPath(listFilesAndDir)).toString();
				if (listFilesAndDir.isDirectory()) {
					// 如果是目录, 先添加一个 entry
					JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(relativePath + "/");
					outputStream.putArchiveEntry(jarArchiveEntry);
					outputStream.closeArchiveEntry();
					continue;
				}
				addFile(listFilesAndDir,relativePath);
			}
		}

		@Override
		public void close() throws IOException {
			if (outputStream != null){
				outputStream.flush();
				outputStream.close();
			}
		}
	}



}
