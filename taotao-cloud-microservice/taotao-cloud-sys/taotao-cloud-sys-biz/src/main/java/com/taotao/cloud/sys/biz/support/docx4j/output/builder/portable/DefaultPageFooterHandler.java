package com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 默认页码信息页脚 最终页脚格式为left + 当前页码 + center + 总页数 + right
 *
 */
public class DefaultPageFooterHandler extends PdfPageEventHelper {

	/**
	 * 页码左边信息
	 */
	private final String left;
	/**
	 * 页码右边信息
	 */
	private final String center;
	/**
	 * 总页数右边信息
	 */
	private final String right;
	private PdfTemplate template;

	public DefaultPageFooterHandler(String left, String center, String right) {
		this.left = left;
		this.center = center;
		this.right = right;
	}

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		this.template = PdfTemplate.createTemplate(writer, 50.0F, 50.0F);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();
		cb.beginText();
		cb.setColorFill(BaseColor.GRAY);
		cb.setFontAndSize(Fonts.BASE_CN_FONT, Fonts.HEADER_FOOTER.size);
		String text = this.left + document.getPageNumber() + this.center;
		// 中间位置
		float centerAlign = document.getPageSize().getWidth() / 2.0F;
		cb.showTextAligned(Element.ALIGN_CENTER, text, centerAlign, 10.0F, 0.0F);
		// 总页数模版偏移量
		float offset =
			centerAlign + Fonts.BASE_CN_FONT.getWidthPoint(text, Fonts.HEADER_FOOTER.size) / 2.0F;
		cb.addTemplate(this.template, offset, 10.0F);
		cb.endText();
		cb.restoreState();
	}

	@Override
	public void onCloseDocument(PdfWriter writer, Document document) {
		this.template.beginText();
		this.template.setColorFill(BaseColor.GRAY);
		this.template.setFontAndSize(Fonts.BASE_CN_FONT, Fonts.HEADER_FOOTER.size);
		this.template.showText((document.getPageNumber() - 1) + this.right);
		this.template.endText();
		this.template.closePath();
	}
}
