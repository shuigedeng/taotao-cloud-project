package com.taotao.cloud.sys.biz.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 依赖libreoffice实现PDF转化
 */
@Component
public class PdfUtils {

	@Value("${castle.upload.fileServerPath}")
	private String originalFilePath;
	@Value("${castle.upload.fileDomain}")
	private String fileDomain;

	/**
	 * @param filePath     原文件路径 本地路径
	 * @param targetFolder 目标文件夹
	 */
	@Async
	public void toPdf(String filePath, String targetFolder) {
		long start = System.currentTimeMillis();
		String srcPath = filePath, desPath = targetFolder;
		System.out.println("源文件：" + filePath);
		System.out.println("目标文件夹：" + targetFolder);
		String command = "";
		String osName = System.getProperty("os.name");
		System.out.println("系统名称：" + osName);
		if (osName.contains("Windows")) {
			command = "soffice --headless --convert-to pdf " + srcPath + " --outdir " + desPath;
			windowExec(command);
		} else {
			command = "libreoffice7.0 --convert-to pdf:writer_pdf_Export " + srcPath + " --outdir "
				+ desPath;
			LinuxExec(command);
		}
		long end = System.currentTimeMillis();
		System.out.println("转换文件耗时：" + (end - start) + "毫秒");
	}

	/**
	 * 获取网络路径的目标文件夹
	 *
	 * @param fileUrl
	 * @return
	 */
	public String getTargetFolder(String fileUrl) {
		int index = fileUrl.indexOf(fileDomain) + fileDomain.length();
		int end = fileUrl.lastIndexOf("/") + 1;
		return originalFilePath + fileUrl.substring(index, end);
	}

	/**
	 * 获取网络文件的服务器物理路径
	 *
	 * @param fileUrl
	 * @return
	 */
	public String getServerPath(String fileUrl) {
		int index = fileUrl.indexOf(fileDomain) + fileDomain.length();
		return originalFilePath + fileUrl.substring(index);
	}

	/**
	 * 获取pdf文件的访问URL
	 *
	 * @param fileUrl
	 * @return
	 */
	public String getPDFUrl(String fileUrl) {
		int dotIndex = fileUrl.lastIndexOf(".");
		return fileUrl.substring(0, dotIndex) + ".pdf";
	}

	/**
	 * 获取png文件的访问URL
	 *
	 * @param fileUrl
	 * @return
	 */
	public List<String> getPngUrl(String fileUrl, int pages) {
		if (pages < 1) {
			return null;
		}
		int dotIndex = fileUrl.lastIndexOf(".");
		List<String> list = new ArrayList<>();
		for (int i = 0; i < pages; i++) {
			list.add(fileUrl.substring(0, dotIndex) + "_" + (i + 1) + ".png");
		}
		return list;
	}

	/**
	 * 获取文件名 不带后缀
	 *
	 * @param fileUrl
	 * @return
	 */
	public static String fileName(String fileUrl) {
		int dotIndex = fileUrl.lastIndexOf(".");
		int lastSeparator = fileUrl.lastIndexOf(File.separator);
		if (lastSeparator == -1) {
			return fileUrl.substring(0, dotIndex);
		} else {
			return fileUrl.substring(lastSeparator + 1, dotIndex);
		}
	}

	private boolean windowExec(String command) {
		Process process;// Process可以控制该子进程的执行或获取该子进程的信息
		try {
			process = Runtime.getRuntime()
				.exec(command);// exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
			// 下面两个可以获取输入输出流
			InputStream errorStream = process.getErrorStream();
			InputStream inputStream = process.getInputStream();
		} catch (IOException e) {
			return false;
		}

		int exitStatus = 0;
		try {
			exitStatus = process.waitFor();// 等待子进程完成再往下执行，返回值是子线程执行完毕的返回值,返回0表示正常结束
			// 第二种接受返回值的方法
			int i = process.exitValue(); // 接收执行完毕的返回值
		} catch (InterruptedException e) {
			return false;
		}
		process.destroy(); // 销毁子进程
		process = null;
		return true;
	}

	private void LinuxExec(String cmd) {
		System.out.println(cmd);
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/***
	 * PDF文件转PNG图片，全部页数
	 *
	 * @param PdfFilePath pdf文件路径 物理路径
	 * @param dstImgFolder 图片存放的文件夹
	 * @param dpi dpi越大转换后越清晰，相对转换速度越慢 dpi为96,100,105,120,150,200中,105显示效果较为清晰,体积稳定,dpi越高图片体积越大,一般电脑显示分辨率为96
	 * @return
	 */
	public int pdf2Image(String PdfFilePath, String dstImgFolder, int dpi) {
		int pages = 1;
		PDDocument pdDocument;
		try {
			File file = new File(PdfFilePath);
			while (!file.exists()) {
				System.out.println("文件未生成，等待1秒");
				Thread.sleep(1000);
			}
			pdDocument = PDDocument.load(file);
			int dot = PdfFilePath.lastIndexOf('.');
			int lastSep = PdfFilePath.lastIndexOf(File.separator) + 1;
			String imagePDFName = PdfFilePath.substring(lastSep, dot); // 获取图片文件名

			PDFRenderer renderer = new PDFRenderer(pdDocument);
			/* dpi越大转换后越清晰，相对转换速度越慢 */
			pages = pdDocument.getNumberOfPages();
			StringBuffer imgFilePath = null;
			for (int i = 0; i < pages; i++) {
				String imgFilePathPrefix = dstImgFolder + File.separator + imagePDFName;
				imgFilePath = new StringBuffer();
				imgFilePath.append(imgFilePathPrefix);
				imgFilePath.append("_");
				imgFilePath.append(String.valueOf(i + 1));
				imgFilePath.append(".png");
				File dstFile = new File(imgFilePath.toString());
				BufferedImage image = renderer.renderImageWithDPI(i, dpi);
				ImageIO.write(image, "png", dstFile);
			}
			System.out.println("PDF文档转PNG图片成功！");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return pages;
	}
}
