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
package com.taotao.cloud.captcha.support.graphic.definition;


import com.taotao.cloud.captcha.support.core.definition.AbstractGraphicRenderer;
import com.taotao.cloud.captcha.support.core.definition.enums.CaptchaCharacter;
import com.taotao.cloud.captcha.support.core.provider.RandomProvider;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * <p>Description: 基础图像绘制器 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:54:30
 */
public abstract class AbstractBaseGraphicRenderer extends AbstractGraphicRenderer {

	protected String[] getWordCharacters() {
		int number = this.getCaptchaProperties().getGraphics().getLength();
		List<String> words = RandomProvider.randomWords(number);
		String[] content = new String[words.size()];
		return words.toArray(content);
	}

	protected String[] getCharCharacters() {
		int number = getCaptchaProperties().getGraphics().getLength();
		CaptchaCharacter captchaCharacter = getCaptchaProperties().getGraphics().getLetter();
		return RandomProvider.randomCharacters(number, captchaCharacter);
	}

	/**
	 * 需要绘制的验证码字符内容
	 *
	 * @return 需要绘制的字符内容
	 */
	protected abstract String[] getDrawCharacters();

	private BufferedImage createPngBufferedImage(String[] characters, String benchmark,
		boolean isArithmetic) {
		return createBufferedImage(characters, benchmark, isArithmetic, false, 0);
	}

	protected BufferedImage createPngBufferedImage(String[] characters) {
		return createPngBufferedImage(characters, "W", false);
	}

	protected BufferedImage createArithmeticBufferedImage(String[] characters) {
		return createPngBufferedImage(characters, "8", true);
	}

	protected BufferedImage createGifBufferedImage(String[] characters, int alpha) {
		return createBufferedImage(characters, "王", false, true, alpha);
	}

	/**
	 * 绘制验证码图形
	 *
	 * @param isGif        是否是 Gif 类型
	 * @param isArithmetic 是否是算数类型
	 * @param characters   内容字符
	 * @param benchmark    内容宽度标尺
	 * @param alpha        透明度，仅针对 Gif类型。
	 * @return {@link BufferedImage}
	 */
	private BufferedImage createBufferedImage(String[] characters, String benchmark,
		boolean isArithmetic, boolean isGif, int alpha) {

		BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(),
			BufferedImage.TYPE_INT_RGB);
		// 获得图形上下文
		Graphics2D graphics = bufferedImage.createGraphics();

		// 填充背景颜色
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		// 抗锯齿
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		// 画干扰线
		if (!isArithmetic) {
			drawInterfereLine(graphics, isGif);
		}

		Color[] colors = RandomProvider.randomColors(characters.length);

		drawCharacter(graphics, characters, colors, benchmark, isGif, alpha);

		graphics.dispose();

		return bufferedImage;
	}

	/**
	 * 设置随机颜色
	 *
	 * @param graphics 图形 {@link Graphics2D}
	 */
	private void drawColor(Graphics2D graphics) {
		graphics.setColor(RandomProvider.randomColor());
	}

	/**
	 * 设置透明度
	 *
	 * @param graphics 图形 {@link Graphics2D}
	 * @param alpha    透明度 {@link Float}
	 */
	private void drawAlpha(Graphics2D graphics, float alpha) {
		AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		graphics.setComposite(alphaComposite);
	}

	/**
	 * 干扰线透明度，主要用于 Gif 验证码
	 *
	 * @param graphics 图形 {@link Graphics2D}
	 */
	private void drawAlphaForLine(Graphics2D graphics) {
		drawAlpha(graphics, 0.7f);
	}


	/**
	 * 获取透明度,从0到1,自动计算步长
	 *
	 * @param length 内容长度
	 * @param alpha  透明度
	 * @param index  绘制内容枚举值
	 * @return 透明度
	 */
	private float getAlpha(int length, int alpha, int index) {
		int num = alpha + index;
		float r = (float) 1 / length;
		float s = (length + 1) * r;
		return num > length ? (num * r - s) : num * r;
	}

	/**
	 * 干扰线透明度，主要用于 Gif 验证码
	 *
	 * @param graphics 图形 {@link Graphics2D}
	 * @param length   内容长度
	 * @param alpha    透明度
	 * @param index    当前内容枚举值
	 */
	private void drawAlphaForCharacter(Graphics2D graphics, int length, int alpha, int index) {
		drawAlpha(graphics, getAlpha(length, alpha, index));
	}

	private int randomCtrlX() {
		return RandomProvider.randomInt(getWidth() / 4, getWidth() / 4 * 3);
	}

	private int randomCtrlY() {
		return RandomProvider.randomInt(5, getHeight() - 5);
	}

	/**
	 * 绘制贝塞尔曲线
	 *
	 * @param graphics 图形 {@link Graphics2D}
	 */
	private void drawBezierCurve(Graphics2D graphics) {
		drawColor(graphics);
		int x1 = 5;
		int y1 = RandomProvider.randomInt(5, getHeight() / 2);
		int x2 = getWidth() - 5;
		int y2 = RandomProvider.randomInt(getHeight() / 2, getHeight() - 5);
		int ctrlx1 = randomCtrlX();
		int ctrly1 = randomCtrlY();
		if (RandomProvider.randomInt(2) == 0) {
			int ty = y1;
			y1 = y2;
			y2 = ty;
		}
		// 二阶贝塞尔曲线
		if (RandomProvider.randomInt(2) == 0) {
			QuadCurve2D shape = new QuadCurve2D.Double();
			shape.setCurve(x1, y1, ctrlx1, ctrly1, x2, y2);
			graphics.draw(shape);
		} else {
			// 三阶贝塞尔曲线
			int ctrlx2 = randomCtrlX();
			int ctrly2 = randomCtrlY();
			CubicCurve2D shape = new CubicCurve2D.Double(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2,
				y2);
			graphics.draw(shape);
		}
	}

	/**
	 * 绘制干扰线
	 *
	 * @param graphics 图形 {@link Graphics2D}
	 * @param isGif    是否是 Gif
	 */
	private void drawInterfereLine(Graphics2D graphics, boolean isGif) {
		if (isGif) {
			// 设置透明度
			drawAlphaForLine(graphics);
		}
		graphics.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		drawBezierCurve(graphics);
	}

	/**
	 * 绘制干扰圆
	 *
	 * @param graphics 图形 {@link Graphics2D}
	 */
	private void drawOval(Graphics2D graphics) {
		int x = RandomProvider.randomInt(getWidth() - 5);
		int y = RandomProvider.randomInt(getHeight() - 5);
		int width = RandomProvider.randomInt(5, 30);
		int height = 5 + RandomProvider.randomInt(5, 30);
		graphics.drawOval(x, y, width, height);
	}

	/**
	 * 绘制验证码
	 *
	 * @param graphics   图形 {@link Graphics2D}
	 * @param characters 验证码内容
	 * @param benchmark  表尺字符
	 * @param isGif      是否设置透明度
	 * @param alpha      透明度
	 */
	private void drawCharacter(Graphics2D graphics, String[] characters, Color[] colors,
		String benchmark, boolean isGif, int alpha) {

		graphics.setFont(getFont());

		FontMetrics fontMetrics = graphics.getFontMetrics();
		// 每一个字符所占的宽度
		int fW = getWidth() / characters.length;
		// 字符的左右边距
		int fSp = (fW - (int) fontMetrics.getStringBounds(benchmark, graphics).getWidth()) / 2;

		for (int i = 0; i < characters.length; i++) {
			// 设置透明度
			if (isGif) {
				drawAlphaForCharacter(graphics, characters.length, alpha, i);
			}
			graphics.setColor(colors[i]);
			drawOval(graphics);
			// 文字的纵坐标
			int fY = getHeight() - (
				(getHeight() - (int) fontMetrics.getStringBounds(String.valueOf(characters[i]),
					graphics).getHeight()) >> 1);
			graphics.drawString(characters[i], i * fW + fSp - 3, fY - 3);
		}
	}
}
