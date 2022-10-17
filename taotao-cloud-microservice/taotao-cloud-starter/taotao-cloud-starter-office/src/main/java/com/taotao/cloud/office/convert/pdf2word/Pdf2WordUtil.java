package com.taotao.cloud.office.convert.pdf2word;

import com.spire.doc.Document;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.widget.PdfPageCollection;
import com.taotao.cloud.common.utils.log.LogUtils;

import java.io.File;

/**
 * <p>
 * Pdf 转 Word 工具类
 * </p>
 *
 * @description
 * @since 2021/1/28 9:33
 */
public class Pdf2WordUtil {

	// 涉及到的路径
	// 1、pdf所在的路径，真实测试种是从外部引入的

	/**
	 * 2、如果是大文件，需要进行切分，保存的子pdf路径
	 */
	String splitPath = "./split/";

	/**
	 * 3、如果是大文件，需要对子pdf文件一个一个进行转化
	 */
	String docPath = "./doc/";

	public void pdf2Word(String srcPath, String desPath) {
		// 4、desPath:最终生成的doc所在的目录，默认是和引入的一个地方，开源时对外提供下载的接口。
		boolean result = false;
		try {
			// 0、判断输入的是否是pdf文件
			// 第一步：判断输入的是否合法
			boolean flag = isPDFFile(srcPath);
			// 第二步：在输入的路径下新建文件夹
			boolean flag1 = create();

			if (flag && flag1) {
				// 1、加载pdf
				PdfDocument pdf = new PdfDocument();
				pdf.loadFromFile(srcPath);
				PdfPageCollection num = pdf.getPages();

				// 2、如果pdf的页数小于11，那么直接进行转化
				if (num.getCount() <= 10) {
					pdf.saveToFile(desPath, com.spire.pdf.FileFormat.DOCX);
				}
				// 3、否则输入的页数比较多，就开始进行切分再转化
				else {
					// 第一步：将其进行切分,每页一张pdf
					pdf.split(splitPath + "test{0}.pdf", 0);

					// 第二步：将切分的pdf，一个一个进行转换
					File[] fs = getSplitFiles(splitPath);
					for (File f : fs) {
						PdfDocument sonpdf = new PdfDocument();
						sonpdf.loadFromFile(f.getAbsolutePath());
						sonpdf.saveToFile(docPath + f.getName().substring(0, f.getName().length() - 4) + ".docx",
							FileFormat.DOCX);
					}
					// 第三步：对转化的doc文档进行合并，合并成一个大的word
					try {
						result = this.merge(docPath, desPath);
						LogUtils.debug(String.valueOf(result));
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			} else {
				LogUtils.debug("输入的不是pdf文件");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 4、把刚刚缓存的split和doc删除
			if (result) {
				this.clearFiles(splitPath);
				this.clearFiles(docPath);
			}
		}
		LogUtils.debug("转换成功");
	}

	private boolean create() {
		File f = new File(splitPath);
		File f1 = new File(docPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		if (!f.exists()) {
			f1.mkdirs();
		}
		return true;
	}

	/**
	 * 判断是否是pdf文件
	 */
	private boolean isPDFFile(String srcPath2) {
		File file = new File(srcPath2);
		String filename = file.getName();
		if (filename.endsWith(".pdf")) {
			return true;
		}
		return false;
	}

	/**
	 * 取得某一路径下所有的pdf
	 */
	private File[] getSplitFiles(String path) {
		File f = new File(path);
		File[] fs = f.listFiles();
		if (fs == null) {
			return null;
		}
		return fs;
	}

	public void clearFiles(String workspaceRootPath) {
		File file = new File(workspaceRootPath);
		if (file.exists()) {
			deleteFile(file);
		}
	}

	public void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}

	private boolean merge(String docPath, String desPath) {
		File[] fs = this.getSplitFileList(docPath);
		System.out.println(docPath);
		Document document = new Document(docPath + "test0.docx");

		for (int i = 1; i < fs.length; i++) {
			document.insertTextFromFile(docPath + "test" + i + ".docx", com.spire.doc.FileFormat.Docx_2013);
		}
		// 第四步：对合并的doc进行保存2
		document.saveToFile(desPath);
		return true;
	}

	/**
	 * 取得某一路径下所有的pdf
	 */
	private File[] getSplitFileList(String path) {
		File f = new File(path);
		return f.listFiles();
	}

}
