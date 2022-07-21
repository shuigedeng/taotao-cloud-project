package com.taotao.cloud.docx4j.xpdf;

import lombok.SneakyThrows;
import wiki.xsx.core.pdf.component.XEasyPdfComponent;
import wiki.xsx.core.pdf.component.image.XEasyPdfImageType;
import wiki.xsx.core.pdf.doc.XEasyPdfDefaultFontStyle;
import wiki.xsx.core.pdf.doc.XEasyPdfPositionStyle;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class XpdfTest {

	@SneakyThrows
	public static void main(String[] args) {
		// 定义图片路径
		final String imagePath = "E:\\pdf\\test\\doc\\test.png";
		// 定义保存路径
		final String outputPath = "E:\\pdf\\test\\doc\\my.pdf";
		// 定义页眉与页脚字体颜色
		Color headerAndFooterColor = new Color(10, 195, 255);
		// 定义分割线颜色
		Color lineColor = new Color(210, 0, 210);
		// 获取背景图片
		try (InputStream backgroundImageInputStream = new URL(
			"https://i0.hdslb.com/bfs/article/1e60a08c2dfdcfcd5bab0cae4538a1a7fe8fc0f3.png").openStream()) {
			// 设置背景图片
			XEasyPdfHandler.Document.build().setGlobalBackgroundImage(
				// 构建图片并垂直居中
				XEasyPdfHandler.Image.build(backgroundImageInputStream, XEasyPdfImageType.PNG)
					.setVerticalStyle(
						XEasyPdfPositionStyle.CENTER)
				// 设置全局页眉
			).setGlobalHeader(
				// 构建页眉
				XEasyPdfHandler.Header.build(
					// 构建页眉图片，并居中显示
					XEasyPdfHandler.Image.build(new File(imagePath)).setHeight(50F)
						.enableCenterStyle(),
					// 构建页眉文本，并居中显示
					XEasyPdfHandler.Text.build("这是粗体页眉")
						// 设置水平垂直居中
						.enableCenterStyle()
						// 设置字体大小
						.setFontSize(20F)
						// 设置字体颜色
						.setFontColor(headerAndFooterColor)
						// 使用粗体字
						.setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD)
				)
				// 设置全局页脚
			).setGlobalFooter(
				// 构建页脚
				XEasyPdfHandler.Footer.build(
					// 构建页眉图片，并居中显示
					XEasyPdfHandler.Image.build(new File(imagePath)).setHeight(50F)
						.setVerticalStyle(XEasyPdfPositionStyle.BOTTOM),
					// 构建页脚文本（手动分行），并居中显示
					XEasyPdfHandler.Text.build(Arrays.asList("这是粗体页脚第一行", "这是粗体页脚第二行"))
						// 设置水平居中
						.setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
						// 设置垂直居下
						.setVerticalStyle(XEasyPdfPositionStyle.BOTTOM)
						// 设置字体大小
						.setFontSize(20F)
						// 设置字体颜色
						.setFontColor(headerAndFooterColor)
						// 使用粗体字
						.setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD)
				)
				// 添加页面
			).addPage(
				// 构建页面
				XEasyPdfHandler.Page.build(
					// 构建文本
					XEasyPdfHandler.Text.build(
							"x-easypdf简介（细体）"
							// 设置水平居中
						).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
						// 设置字体大小
						.setFontSize(16F)
						// 使用细体字
						.setDefaultFontStyle(XEasyPdfDefaultFontStyle.LIGHT)
						// 开启删除线
						.enableDeleteLine()
					// 构建文本
					, XEasyPdfHandler.Text.build(
						"x-easypdf是一个基于PDFBOX的开源框架，"
					).setFontSize(16F).setFontColor(new Color(51, 0, 153))
					// 构建文本
					, XEasyPdfHandler.Text.build(
							"专注于PDF文件导出功能，"
						).enableTextAppend().setFontSize(16F).setFontColor(new Color(102, 0, 153))
						// 开启下划线并设置为红色
						.enableUnderline().setUnderlineColor(Color.RED)
					// 构建文本
					, XEasyPdfHandler.Text.build(
							"以组件形式进行拼接，"
						).enableTextAppend().setFontSize(16F).setFontColor(new Color(153, 0, 153))
						// 开启高亮并设置为橘色
						.enableHighlight().setHighlightColor(Color.ORANGE)
					// 构建文本
					, XEasyPdfHandler.Text.build(
						"简单、方便，功能丰富，"
					).enableTextAppend().setFontSize(16F).setFontColor(new Color(204, 0, 153))
					// 构建文本
					, XEasyPdfHandler.Text.build(
						"欢迎大家试用并提出宝贵意见。"
					).enableTextAppend().setFontSize(16F).setFontColor(new Color(255, 0, 153))
					// 构建文本
					, XEasyPdfHandler.Text.build(
							"-- by xsx"
						).setHorizontalStyle(XEasyPdfPositionStyle.RIGHT).setMarginTop(10F)
						.setMarginRight(10F)
					// 构建文本
					, XEasyPdfHandler.Text.build(
							"2021.10.10"
						).setHorizontalStyle(XEasyPdfPositionStyle.RIGHT).setMarginTop(10F)
						.setMarginRight(10F)
					// 构建实线分割线
					, XEasyPdfHandler.SplitLine.SolidLine.build().setMarginTop(10F)
						.setColor(lineColor).setContentMode(XEasyPdfComponent.ContentMode.PREPEND)
					// 构建虚线分割线
					, XEasyPdfHandler.SplitLine.DottedLine.build().setLineLength(10F)
						.setMarginTop(10F).setLineWidth(10F).setColor(lineColor)
						.setContentMode(XEasyPdfComponent.ContentMode.PREPEND)
					// 构建实线分割线
					, XEasyPdfHandler.SplitLine.SolidLine.build().setMarginTop(10F)
						.setColor(lineColor).setContentMode(XEasyPdfComponent.ContentMode.PREPEND)
					// 构建表格
					, XEasyPdfHandler.Table.build(
							// 构建行
							XEasyPdfHandler.Table.Row.build(
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
									// 添加文本
									XEasyPdfHandler.Text.build("第一行第一列")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
									XEasyPdfHandler.Text.build("第一行第二列")
								),
								// 构建单元格并设置字体大小为15，边框颜色为绿色
								XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
									XEasyPdfHandler.Text.build("第一行第三列")
								).setFontSize(15F).setBorderColor(Color.GREEN),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
									XEasyPdfHandler.Text.build("第一行第四列")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
									XEasyPdfHandler.Text.build("第一行第五列")
								)
								// 设置该行字体大小为20
							).setFontSize(20F),
							// 构建行
							XEasyPdfHandler.Table.Row.build(
								// 构建单元格，合并三行
								XEasyPdfHandler.Table.Row.Cell.build(100F, 300F).addContent(
									XEasyPdfHandler.Text.build(Arrays.asList("第二行", "第一列", "合并三行"))
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(300F).addContent(
									XEasyPdfHandler.Text.build("第二行第二列，合并三列")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F, 300F).addContent(
									XEasyPdfHandler.Text.build("第二行第三列，合并三行")
								)
								// 设置行高为100（合并行需要设置平均行高）
							).setHeight(100F),
							// 构建行
							XEasyPdfHandler.Table.Row.build(
								// 构建单元格，开启垂直合并
								XEasyPdfHandler.Table.Row.Cell.build(100F).enableVerticalMerge(),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F).addContent(
									XEasyPdfHandler.Text.build("第三行第一列")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F).addContent(
									XEasyPdfHandler.Text.build("第三行第二列")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F).addContent(
									XEasyPdfHandler.Text.build("第三行第三列")
								)
								// 设置行高为100，设置边框颜色为红色，设置字体颜色为蓝色
							).setHeight(100F).setBorderColor(Color.RED).setFontColor(Color.BLUE),
							// 构建行（单元格已设置高度，则合并行无需设置行高）
							XEasyPdfHandler.Table.Row.build(
								// 构建单元格，开启垂直合并
								XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).enableVerticalMerge(),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(300F, 100F).addContent(
									XEasyPdfHandler.Text.build("第四行第一列，合并三列")
								)
							),
							// 构建行（根据文本高度自适应行高）
							XEasyPdfHandler.Table.Row.build(
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F).addContent(
									XEasyPdfHandler.Text.build("第五行第一列，自适应高度！")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F).addContent(
									XEasyPdfHandler.Text.build("第五行第二列，自适应高度！！！！！！！！！！")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F).addContent(
									XEasyPdfHandler.Text.build("第五行第三列，自适应高度！！！！！！！！！！！！！！！！！！！！！！")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F).addContent(
									XEasyPdfHandler.Text.build("第五行第四列，自适应高度！！！！！！！！！！")
								),
								// 构建单元格
								XEasyPdfHandler.Table.Row.Cell.build(100F).addContent(
									XEasyPdfHandler.Text.build("第五行第五列，自适应高度！")
								)
							),
							// 构建行
							XEasyPdfHandler.Table.Row.build(
								// 构建单元格，并设置边框颜色为橘色
								XEasyPdfHandler.Table.Row.Cell.build(500F, 100F).addContent(
									XEasyPdfHandler.Text.build("分页测试1")
								).setBorderColor(Color.ORANGE).setFontColor(Color.PINK)
							)
							// 设置表头行
						).setTileRow(
							// 构建行
							XEasyPdfHandler.Table.Row.build(
								// 构建单元格，并设置边框颜色为黑色，字体大小为30，字体颜色为紫色
								XEasyPdfHandler.Table.Row.Cell.build(500F, 100F).addContent(
										XEasyPdfHandler.Text.build("表头")
									).setBorderColor(Color.BLACK).setFontSize(30F)
									.setFontColor(Color.MAGENTA)
							)
							// 开启表格内容上下左右居中
						).enableCenterStyle()
						// 设置左边距为50
						.setMarginLeft(50F)
						// 设置上边距为10
						.setMarginTop(10F)
						// 设置边框颜色为灰色
						.setBorderColor(Color.GRAY)
						// 开启自动表头（分页时，自动添加表头行）
						.enableAutoTitle()
				)
				// 保存、关闭
			).save(outputPath).close();
		}
	}
}
