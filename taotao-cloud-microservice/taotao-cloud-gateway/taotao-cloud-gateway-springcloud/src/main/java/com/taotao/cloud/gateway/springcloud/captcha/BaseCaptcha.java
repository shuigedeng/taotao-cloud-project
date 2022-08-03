package com.taotao.cloud.gateway.springcloud.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.OutputStream;

public abstract class BaseCaptcha extends Randoms {

	protected Font font = new Font("Arial", Font.BOLD, 32); // 字体Verdana
	protected int len = 4; // 验证码随机字符长度
	protected int width = 130; // 验证码显示宽度
	protected int height = 48; // 验证码显示高度
	protected String chars = null; // 当前验证码
	protected int charType = TYPE_DEFAULT;  // 验证码类型，1字母数字混合，2纯数字，3纯字母
	public static final int TYPE_DEFAULT = 1;  // 字母数字混合
	public static final int TYPE_ONLY_NUMBER = 2;  // 纯数字
	public static final int TYPE_ONLY_CHAR = 3;  // 纯字母
	public static final int TYPE_ONLY_UPPER = 4;  // 纯大写字母
	public static final int TYPE_ONLY_LOWER = 5;  // 纯小写字母
	public static final int TYPE_NUM_AND_UPPER = 6;  // 数字大写字母
	// 常用颜色
	public static final int[][] COLOR = {{0, 135, 255}, {51, 153, 51}, {255, 102, 102},
		{255, 153, 0}, {153, 102, 0}, {153, 102, 153}, {51, 153, 153}, {102, 102, 255},
		{0, 102, 204}, {204, 51, 51}, {0, 153, 204}, {0, 51, 102}};

	/**
	 * 生成随机验证码
	 *
	 * @return 验证码字符数组
	 */
	protected char[] alphas() {
		char[] cs = new char[len];
		for (int i = 0; i < len; i++) {
			switch (charType) {
				case 2:
					cs[i] = alpha(numMaxIndex);
					break;
				case 3:
					cs[i] = alpha(charMinIndex, charMaxIndex);
					break;
				case 4:
					cs[i] = alpha(upperMinIndex, upperMaxIndex);
					break;
				case 5:
					cs[i] = alpha(lowerMinIndex, lowerMaxIndex);
					break;
				case 6:
					cs[i] = alpha(upperMaxIndex);
					break;
				default:
					cs[i] = alpha();
			}
		}
		chars = new String(cs);
		return cs;
	}

	/**
	 * 给定范围获得随机颜色
	 *
	 * @param fc 0-255
	 * @param bc 0-255
	 * @return 随机颜色
	 */
	protected Color color(int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + num(bc - fc);
		int g = fc + num(bc - fc);
		int b = fc + num(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 获取随机常用颜色
	 *
	 * @return 随机颜色
	 */
	protected Color color() {
		int[] color = COLOR[num(COLOR.length)];
		return new Color(color[0], color[1], color[2]);
	}

	/**
	 * 验证码输出,抽象方法，由子类实现
	 *
	 * @param os 输出流
	 * @return 是否成功
	 */
	public abstract boolean out(OutputStream os);

	/**
	 * 获取当前的验证码
	 *
	 * @return 字符串
	 */
	public String text() {
		checkAlpha();
		return chars;
	}

	/**
	 * 获取当前验证码的字符数组
	 *
	 * @return 字符数组
	 */
	public char[] textChar() {
		checkAlpha();
		return chars.toCharArray();
	}

	/**
	 * 检查验证码是否生成，没有这立即生成
	 */
	public void checkAlpha() {
		if (chars == null) {
			alphas(); // 生成验证码
		}
	}

	/**
	 * 随机画干扰线
	 *
	 * @param num 数量
	 * @param g   Graphics2D
	 */
	public void drawLine(int num, Graphics2D g) {
		drawLine(num, null, g);
	}

	/**
	 * 随机画干扰线
	 *
	 * @param num   数量
	 * @param color 颜色
	 * @param g     Graphics2D
	 */
	public void drawLine(int num, Color color, Graphics2D g) {
		for (int i = 0; i < num; i++) {
			g.setColor(color == null ? color(150, 250) : color);
			int x1 = num(-10, width - 10);
			int y1 = num(5, height - 5);
			int x2 = num(10, width + 10);
			int y2 = num(2, height - 2);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	/**
	 * 随机画干扰圆
	 *
	 * @param num 数量
	 * @param g   Graphics2D
	 */
	public void drawOval(int num, Graphics2D g) {
		for (int i = 0; i < num; i++) {
			g.setColor(color(100, 250));
			g.drawOval(num(width), num(height), 10 + num(20), 10 + num(20));
		}
	}

	/**
	 * 随机画干扰圆
	 *
	 * @param num   数量
	 * @param color 颜色
	 * @param g     Graphics2D
	 */
	public void drawOval(int num, Color color, Graphics2D g) {
		for (int i = 0; i < num; i++) {
			g.setColor(color == null ? color(100, 250) : color);
			g.drawOval(num(width), num(height), 10 + num(20), 10 + num(20));
		}
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCharType() {
		return charType;
	}

	public void setCharType(int charType) {
		this.charType = charType;
	}
}
