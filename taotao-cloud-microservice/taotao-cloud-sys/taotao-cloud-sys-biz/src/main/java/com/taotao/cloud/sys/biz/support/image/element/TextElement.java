package com.taotao.cloud.sys.biz.support.image.element;

import com.taotao.cloud.sys.biz.support.image.enums.LineAlign;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import sun.font.FontDesignMetrics;

/**
 * TextElement
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:16:36
 */
public class TextElement extends CombineElement<TextElement> {

	//内部度量对象
	private FontDesignMetrics metrics;

	//基础属性
	private String text;                //文本
	private Font font;                  //字体
	private boolean strikeThrough;      //删除线
	private Color color = new Color(0, 0, 0);   //颜色，默认黑色
	private Integer rotate;             //旋转
	private Integer lineHeight;         //行高（根据设计稿设置具体的值，默认等于metrics.getHeight()）
	private Integer width;              //宽度（只读，计算值）
	private Integer height;             //高度（只读，计算值，单行时等于lineHeight，多行时等于lineHeight*行数）
	private Integer drawY;              //实际绘制用的y（sketch的y与graph2d有所区别，需要换算）

	//换行计算相关属性
	private boolean autoBreakLine = false;          //是否自动换行
	private int maxLineWidth = 600;                 //最大行宽，超出则换行
	private int maxLineCount = 2;                   //最大行数，超出则丢弃
	private LineAlign lineAlign = LineAlign.Left;   //行对齐方式，默认左对齐
	private List<TextElement> breakLineElements;    //换过行的元素们

	/**
	 * @param text 文本内容
	 * @param font Font对象
	 * @param x    x坐标
	 * @param y    y坐标
	 */
	public TextElement(String text, Font font, int x, int y) {
		this.text = text;
		this.font = font;
		super.setX(x);
		super.setY(y);
	}

	/**
	 * @param text     文本内容
	 * @param fontSize 字号
	 * @param x        x坐标
	 * @param y        y坐标
	 */
	public TextElement(String text, int fontSize, int x, int y) {
		this.text = text;
		this.font = new Font("阿里巴巴普惠体", Font.PLAIN, fontSize);
		super.setX(x);
		super.setY(y);
	}

	/**
	 * @param text     文本内容
	 * @param fontName 字体名称
	 * @param fontSize 字号
	 * @param x        x坐标
	 * @param y        y坐标
	 */
	public TextElement(String text, String fontName, int fontSize, int x, int y) {
		this.text = text;
		this.font = new Font(fontName, Font.PLAIN, fontSize);
		super.setX(x);
		super.setY(y);
	}


	/************* 计算属性 *************/
	public Integer getWidth() {
		if (width == null) {
			width = getMetrics().stringWidth(text);
		}
		return width;
	}

	public Integer getHeight() {
		if (height == null) {
			if (autoBreakLine == true) {
				height = getLineHeight() * getBreakLineElements().size();     //如果自动换行，则计算多行高度
			} else {
				height = getLineHeight();
			}
		}
		return height;
	}

	public Integer getDrawY() {
		if (drawY == null) {
			drawY = getY() + (getLineHeight() - getMetrics().getHeight()) / 2
				+ getMetrics().getAscent();
		}
		return drawY;
	}

	public List<TextElement> getBreakLineElements() {
		if (breakLineElements == null) {
			breakLineElements = computeBreakLineElements();
		}
		return breakLineElements;
	}


	/************* 读写属性 *************/
	public String getText() {
		return text;
	}

	public TextElement setText(String text) {
		this.text = text;
		this.resetProperties();
		return this;
	}

	public Font getFont() {
		return font;
	}

	public TextElement setFont(Font font) {
		this.font = font;
		this.resetProperties();
		return this;
	}

	public Integer getRotate() {
		return rotate;
	}

	public TextElement setRotate(Integer rotate) {
		this.rotate = rotate;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public TextElement setColor(Color color) {
		this.color = color;
		return this;
	}

	public TextElement setColor(int r, int g, int b) {
		return setColor(new Color(r, g, b));
	}

	public Integer getLineHeight() {
		if (lineHeight == null) {
			lineHeight = getMetrics().getHeight();      //未设置lineHeight则默认取文本高度
		}
		return lineHeight;
	}

	public TextElement setLineHeight(Integer lineHeight) {
		this.lineHeight = lineHeight;
		this.resetProperties();
		return this;
	}

	public boolean isStrikeThrough() {
		return strikeThrough;
	}

	public TextElement setStrikeThrough(boolean strikeThrough) {
		this.strikeThrough = strikeThrough;
		return this;
	}

	public boolean isAutoBreakLine() {
		return autoBreakLine;
	}

	/**
	 * 设置自动换行（默认左对齐）
	 *
	 * @param maxLineWidth 最大宽度（超出则换行）
	 * @param maxLineCount 最大行数（超出则丢弃）
	 * @param lineHeight   行高
	 * @return
	 */
	public TextElement setAutoBreakLine(int maxLineWidth, int maxLineCount, int lineHeight) {
		this.autoBreakLine = true;
		this.maxLineWidth = maxLineWidth;
		this.maxLineCount = maxLineCount;
		this.lineHeight = lineHeight;
		return this;
	}

	/**
	 * 设置自动换行（默认左对齐）
	 *
	 * @param maxLineWidth 最大宽度（超出则换行）
	 * @param maxLineCount 最大行数（超出则丢弃）
	 * @return
	 */
	public TextElement setAutoBreakLine(int maxLineWidth, int maxLineCount) {
		this.autoBreakLine = true;
		this.maxLineWidth = maxLineWidth;
		this.maxLineCount = maxLineCount;
		return this;
	}

	/**
	 * 设置自动换行
	 *
	 * @param maxLineWidth 最大宽度（超出则换行）
	 * @param maxLineCount 最大行数（超出则丢弃）
	 * @param lineHeight   行高
	 * @param lineAlign    行对齐方式
	 * @return
	 */
	public TextElement setAutoBreakLine(int maxLineWidth, int maxLineCount, int lineHeight,
		LineAlign lineAlign) {
		this.autoBreakLine = true;
		this.maxLineWidth = maxLineWidth;
		this.maxLineCount = maxLineCount;
		this.lineHeight = lineHeight;
		this.lineAlign = lineAlign;
		return this;
	}

	/**
	 * 设置自动换行
	 *
	 * @param maxLineWidth 最大宽度（超出则换行）
	 * @param maxLineCount 最大行数（超出则丢弃）
	 * @param lineAlign    行对齐方式
	 * @return
	 */
	public TextElement setAutoBreakLine(int maxLineWidth, int maxLineCount, LineAlign lineAlign) {
		this.autoBreakLine = true;
		this.maxLineWidth = maxLineWidth;
		this.maxLineCount = maxLineCount;
		this.lineAlign = lineAlign;
		return this;
	}

	public LineAlign getLineAlign() {
		return lineAlign;
	}


	/************* 私有方法 *************/
	private void resetProperties() {
		//如果设置了影响布局的字段，需要重置这几个计算值
		this.width = null;
		this.height = null;
		this.drawY = null;
		this.breakLineElements = null;
	}

	private FontDesignMetrics getMetrics() {
		if (metrics == null) {
			metrics = FontDesignMetrics.getMetrics(font);
		}
		return metrics;
	}

	private List<TextElement> computeBreakLineElements() {
		List<TextElement> breakLineElements = new ArrayList<>();
		List<String> breakLineTexts = computeLines(text);
		int currentY = getY();
		for (int i = 0; i < breakLineTexts.size(); i++) {
			if (i < maxLineCount) {
				String text = breakLineTexts.get(i);
				//如果计该行是要取的最后一行，但不是整体最后一行，则加...
				if (i == maxLineCount - 1 && i < breakLineTexts.size() - 1) {
					text = text.substring(0, text.length() - 1) + "...";
				}
				TextElement textLineElement = new TextElement(text, font, getX(), currentY);
				textLineElement.setColor(color);
				textLineElement.setStrikeThrough(strikeThrough);
				textLineElement.setCenter(isCenter());
				textLineElement.setAlpha(getAlpha());
				textLineElement.setRotate(rotate);
				textLineElement.setLineHeight(getLineHeight());
				textLineElement.setDirection(getDirection());
				breakLineElements.add(textLineElement);

				currentY += getLineHeight();

			} else {
				break;
			}
		}
		return breakLineElements;
	}

	private List<String> computeLines(String text) {
		List<String> computedLines = new ArrayList<>();     //最终要返回的多行文本（不超限定宽度）
		String strToComputer = "";
		String word = "";             //一个完整单词
		boolean hasWord = false;      //是否获得一个完整单词
		char[] chars = text.toCharArray();
		int count = 0;

		//遍历每个字符，拆解单词（一个中文算一个单词，其他字符直到碰到空格算一个单词）
		for (int i = 0; i < chars.length; i++) {
			if (count++ > 2000) {
				break;      //防止意外情况进入死循环
			}
			char c = chars[i];          //当前字符
			if (isChineseChar(c) || c == ' ' || i == (chars.length - 1)) {
				word += c;             //如果是中文或空格或最后一个字符，一个中文算一个单词, 其他字符遇到空格认为单词结束
				hasWord = true;
			} else {
				word += c;             //英文或其他字符，加入word，待组成单词
			}

			//获得了一个完整单词，加入当前行，并计算限宽
			if (hasWord) {

				//计算现有文字宽度
				int originWidth = getMetrics().stringWidth(strToComputer);

				//计算单个单词宽度（防止一个单词就超限宽的情况）
				int wordWidth = getMetrics().stringWidth(word);

				//单词加入待计算字符串
				strToComputer += word;

				//加入了新单词之后的宽度
				int newWidth = originWidth + wordWidth;

				//一个单词就超限，要暴力换行
				if (wordWidth > maxLineWidth) {
					//按比例计算要取几个字符（不是特别精准）
					int fetch = (int) ((float) (maxLineWidth - originWidth) / (float) wordWidth
						* word.length());   //本行剩余宽度所占word宽度比例，乘以字符长度（字符不等宽的时候不太准）
					strToComputer = strToComputer.substring(0,
						strToComputer.length() - word.length() + fetch);     //去除最后的word的后半截
					computedLines.add(strToComputer);                      //加入计算结果列表
					strToComputer = "";
					i -= (word.length()
						- fetch);                          //遍历计数器回退word.length()-fetch个
				}
				//行宽度超出限宽，则去除最后word，加入计算结果列表
				else if (newWidth > maxLineWidth) {
					strToComputer = strToComputer.substring(0,
						strToComputer.length() - word.length());      //去除最后word
					computedLines.add(strToComputer);                       //加入计算结果列表
					strToComputer = "";
					i -= word.length();                                     //遍历计数器回退word.length()个
				}

				word = "";
				hasWord = false;        //重置标记
			}
		}

		if (strToComputer != "") {
			computedLines.add(strToComputer);                               //加入计算结果列表
		}

		return computedLines;
	}

	private boolean isChineseChar(char c) {
		return String.valueOf(c).matches("[\u4e00-\u9fa5]");
	}
}
