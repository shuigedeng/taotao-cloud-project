package com.taotao.cloud.sys.biz.image_combiner;

import com.taotao.cloud.sys.biz.support.image_combiner.ImageCombiner;
import com.taotao.cloud.sys.biz.support.image_combiner.element.TextElement;
import com.taotao.cloud.sys.biz.support.image_combiner.enums.Direction;
import com.taotao.cloud.sys.biz.support.image_combiner.enums.GradientDirection;
import com.taotao.cloud.sys.biz.support.image_combiner.enums.LineAlign;
import com.taotao.cloud.sys.biz.support.image_combiner.enums.OutputFormat;
import com.taotao.cloud.sys.biz.support.image_combiner.enums.ZoomMode;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	/**
	 * 简单测试
	 *
	 * @throws Exception
	 */
	@Test
	public void simpleTest() throws Exception {

		//合成器和背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
		ImageCombiner combiner = new ImageCombiner(
			"https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png",
			OutputFormat.JPG);

		//加图片元素（居中绘制，圆角，半透明）
		combiner.addImageElement(
				"https://img.thebeastshop.com/image/20201130115835493501.png?x-oss-process=image/resize,m_pad,w_750,h_783/auto-orient,1/quality,q_90/format,jpg",
				0, 300)
			.setCenter(true);

		//加文本元素
		combiner.addTextElement("周末大放送", 60, 100, 960)
			.setColor(Color.red);

		//合成图片
		combiner.combine();

		//保存文件（或getCombinedImageStream()并上传图片服务器）
		combiner.save("d://simpleTest.jpg");

		//或者获取流（并上传oss等）
		//InputStream is = combiner.getCombinedImageStream();
		//String url = ossUtil.upload(is);
	}

	/**
	 * 完整功能测试
	 *
	 * @throws Exception
	 */
	@Test
	public void FullTest() throws Exception {
		String bgImageUrl = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";                       //背景图（测试url形式）
		String qrCodeUrl = "http://imgtest.thebeastshop.com/file/combine_image/qrcodef3d132b46b474fe7a9cc6e76a511dfd5.jpg";     //二维码
		String productImageUrl = "https://img.thebeastshop.com/combine_image/funny_topic/resource/product_3x4.png";             //商品图
		BufferedImage waterMark = ImageIO.read(new URL(
			"https://img.thebeastshop.com/combine_image/funny_topic/resource/water_mark.png"));  //水印图（测试BufferedImage形式）
		BufferedImage avatar = ImageIO.read(new URL(
			"https://img.thebeastshop.com/member/privilege/level-icon/level-three.jpg"));           //头像
		String title = "# 最爱的家居";                                       //标题文本
		String content = "苏格拉底说：“如果没有那个桌子，可能就没有那个水壶”";  //内容文本

		//合成器和背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
		ImageCombiner combiner = new ImageCombiner(bgImageUrl, OutputFormat.PNG);
		combiner.setBackgroundBlur(30);     //设置背景高斯模糊（毛玻璃效果）
		combiner.setCanvasRoundCorner(100); //设置整图圆角（输出格式必须为PNG）

		//商品图（设置坐标、宽高和缩放模式，若按宽度缩放，则高度按比例自动计算）
		combiner.addImageElement(productImageUrl, 0, 160, 837, 0, ZoomMode.Width)
			.setRoundCorner(46)     //设置圆角
			.setCenter(true);       //居中绘制，会忽略x坐标参数，改为自动计算

		//标题（默认字体为“阿里巴巴普惠体”，也可以自己指定字体名称或Font对象）
		combiner.addTextElement(title, 55, 150, 1400);

		//内容（设置文本自动换行，需要指定最大宽度（超出则换行）、最大行数（超出则丢弃）、行高）
		combiner.addTextElement(content, "微软雅黑", 40, 150, 1480)
			.setAutoBreakLine(837, 2, 60);

		//头像（圆角设置一定的大小，可以把头像变成圆的）
		combiner.addImageElement(avatar, 200, 1200, 130, 130, ZoomMode.WidthHeight)
			.setRoundCorner(200)
			.setBlur(5);       //高斯模糊，毛玻璃效果

		//水印（设置透明度，0.0~1.0）
		combiner.addImageElement(waterMark, 630, 1200)
			.setAlpha(.8f)      //透明度，0.0~1.0
			.setRotate(15);     //旋转，0~360，按中心点旋转

		//二维码（强制按指定宽度、高度缩放）
		combiner.addImageElement(qrCodeUrl, 138, 1707, 186, 186, ZoomMode.WidthHeight);

		//元素对象也可以直接new，然后手动加入待绘制列表
		TextElement textPrice = new TextElement("￥1290", 40, 600, 1400);
		textPrice.setStrikeThrough(true);       //删除线
		combiner.addElement(textPrice);         //加入待绘制集合

		//动态计算位置
		int offsetPrice = textPrice.getX() + textPrice.getWidth() + 10;
		combiner.addTextElement("￥999", 60, offsetPrice, 1400)
			.setColor(Color.red);

		//执行图片合并
		combiner.combine();

		//保存文件
		combiner.save("d://fullTest.png");

		//或者获取流（并上传oss等）
		//InputStream is = combiner.getCombinedImageStream();
		//String url = ossUtil.upload(is);
	}

	/**
	 * 旋转测试
	 *
	 * @throws Exception
	 */
	@Test
	public void rotateTest() throws Exception {
		String bg = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";
		ImageCombiner combiner = new ImageCombiner(bg, OutputFormat.JPG);

		combiner.addTextElement("我觉得应该可以正常显示", 80, 300, 300)
			.setCenter(true);
		combiner.addTextElement("我觉得应该可以正常显示", 80, 300, 300).setColor(Color.red)
			.setCenter(true)
			.setRotate(40);

		combiner.addTextElement("测试一下多行文本换行加旋转的动作，不知道是否能正常显示", 80, 300, 600)
			.setStrikeThrough(true)
			.setAutoBreakLine(600, 2, 80);
		combiner.addTextElement("测试一下多行文本换行加旋转的动作，不知道是否能正常显示", 80, 300, 600).setColor(Color.red)
			.setStrikeThrough(true)
			.setAutoBreakLine(600, 2, 80)
			.setRotate(40);

		combiner.addImageElement("http://img.thebeastshop.com/images/index/imgs/8wzZ7St7KH.jpg",
				300, 1000)
			.setCenter(true)
			.setRotate(45);

		combiner.combine();
		combiner.save("d://rotateTest.jpg");
	}

	/**
	 * 动态计算宽度测试
	 *
	 * @throws Exception
	 */
	@Test
	public void dynamicWidthDemoTest() throws Exception {
		String bg = "https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png";
		ImageCombiner combiner = new ImageCombiner(bg, OutputFormat.JPG);

		String str1 = "您出征";
		String str2 = "某城市";     //内容不定，宽度也不定
		String str3 = "，共在前线战斗了";
		String str4 = "365";     //内容不定，宽度也不定
		String str5 = "天！";
		int fontSize = 60;
		int xxxFontSize = 80;

		int offsetX = 20;
		int y = 300;

		TextElement element1 = combiner.addTextElement(str1, fontSize, offsetX, y);
		offsetX += element1.getWidth();

		TextElement element2 = combiner.addTextElement(str2, xxxFontSize, offsetX, y - 20)
			.setColor(Color.red);
		offsetX += element2.getWidth();

		TextElement element3 = combiner.addTextElement(str3, fontSize, offsetX, y);
		offsetX += element3.getWidth();

		TextElement element4 = combiner.addTextElement(str4, xxxFontSize, offsetX, y - 20)
			.setColor(Color.red);
		offsetX += element4.getWidth();

		combiner.addTextElement(str5, fontSize, offsetX, y);

		BufferedImage img = ImageIO.read(
			new URL("http://img.thebeastshop.com/images/index/imgs/8wzZ7St7KH.jpg"));
		combiner.addImageElement(img, 20, 500);

		combiner.combine();
		combiner.save("d://demo.jpg");
	}

	/**
	 * Png透明背景图测试
	 *
	 * @throws IOException
	 */
	@Test
	public void pngTest() throws Exception {

		BufferedImage bgImage = ImageIO.read(new File("d://memberCard.png"));   //背景是圆角透明图
		String content = "2021-12-12 到期";

		//如背景包含透明部分，一定要用OutputFormat.PNG格式，否则合成后透明部分会变黑
		ImageCombiner combiner = new ImageCombiner(bgImage, 1000, 0, ZoomMode.Width,
			OutputFormat.PNG);

		//内容文本
		combiner.addTextElement(content, 38, 72, 260).setColor(Color.white);

		//合成图片
		combiner.combine();

		//上传oss
		combiner.save("d://pngTest.png");
	}

	/**
	 * 矩形（圆形）绘制测试
	 *
	 * @throws Exception
	 */
	@Test
	public void rectangleTest() throws Exception {
		//合成器和背景图（整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
		ImageCombiner combiner = new ImageCombiner(
			"https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png",
			OutputFormat.JPG);

		//加图片元素（居中绘制，圆角，半透明）
		combiner.addImageElement(
				"https://img.thebeastshop.com/image/20201130115835493501.png?x-oss-process=image/resize,m_pad,w_750,h_783/auto-orient,1/quality,q_90/format,jpg",
				0, 300)
			.setCenter(true);

		//加文本元素
		combiner.addTextElement("周末大放送", 60, 200, 960)
			.setColor(Color.red)
			.setAlpha(0.2f);

		//加文本元素
		combiner.addTextElement("周末大放送", 60, 200, 560)
			.setColor(Color.red);

		//加入矩形元素
		combiner.addRectangleElement(200, 500, 300, 300)
			.setColor(Color.BLUE);

		//加入矩形元素（圆角）
		combiner.addRectangleElement(300, 700, 300, 300)
			.setColor(Color.YELLOW)
			.setRoundCorner(100)
			.setAlpha(.8f);

		//加入矩形元素（圆）
		combiner.addRectangleElement(200, 500, 300, 300)
			.setColor(Color.RED)
			.setRoundCorner(300)        //该值大于等于宽高时，就是圆形
			.setAlpha(.8f)
			.setCenter(true);

		//合成图片
		combiner.combine();

		combiner.save("d://rectangleTest.jpg");
	}

	/**
	 * 文本自动换行测试
	 *
	 * @throws Exception
	 */
	@Test
	public void breakLineTest() throws Exception {
		ImageCombiner combiner = new ImageCombiner(
			"https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png",
			OutputFormat.JPG);
		Font font = new Font("阿里巴巴普惠体", Font.PLAIN, 62);

		//添加文字，并设置换行参数
		combiner.addTextElement("测试换行第三方时的文字效果，每行是不是都一样，我们开始吧测试换行时的文字效果，每行是不是都一样，我们开始吧", font, 100,
				0)
			.setColor(Color.red)
			.setCenter(true)
			.setStrikeThrough(true)
			.setAutoBreakLine(630, 8, 100, LineAlign.Right);    //不给LineAlign参数的话，默认左对齐
		//合成图片
		combiner.combine();

		combiner.save("d://breakLineTest.jpg");
	}

	/**
	 * 测试矩形渐变色
	 */
	@Test
	public void GradientTest() throws Exception {
		ImageCombiner combiner = new ImageCombiner(
			"https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png",
			OutputFormat.JPG);

		//四向渐变
		combiner.addRectangleElement(100, 100, 300, 300).setRoundCorner(50)
			.setGradient(Color.blue, Color.red, GradientDirection.TopBottom);
		combiner.addRectangleElement(450, 100, 300, 300)
			.setGradient(Color.blue, Color.red, GradientDirection.LeftRight);
		combiner.addRectangleElement(100, 450, 300, 300)
			.setGradient(Color.blue, Color.red, GradientDirection.LeftTopRightBottom);
		combiner.addRectangleElement(450, 450, 300, 300)
			.setGradient(Color.blue, Color.red, GradientDirection.RightTopLeftBottom);

		//渐变拉伸对比
		combiner.addRectangleElement(100, 800, 300, 300)
			.setGradient(Color.yellow, Color.magenta, GradientDirection.TopBottom);
		combiner.addRectangleElement(450, 800, 300, 300)
			.setGradient(Color.yellow, Color.magenta, 0, 100, GradientDirection.TopBottom);
		combiner.addRectangleElement(800, 800, 300, 300)
			.setGradient(Color.yellow, Color.magenta, 100, 0, GradientDirection.TopBottom);

		//渐变+透明度
		combiner.addTextElement("渐变+透明度", 30, 120, 1300);
		combiner.addRectangleElement(100, 1150, 300, 300)
			.setGradient(Color.yellow, Color.magenta, GradientDirection.TopBottom)
			.setAlpha(.6f);

		//合成图片
		combiner.combine();

		combiner.save("d://GradientTest.jpg");
	}

	/**
	 * 不带背景图的空白画布，画布高度根据内容动态计算
	 *
	 * @throws Exception
	 */
	@Test
	public void ymxkTest() throws Exception {
		BufferedImage imgTop = ImageIO.read(new File("d://ymxk/top.png"));
		BufferedImage imgStar = ImageIO.read(new File("d://ymxk/star.png"));
		BufferedImage imgGrayStar = ImageIO.read(new File("d://ymxk/star_gray.png"));
		String content = "这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度，这是很长一串文字，用于计算背景图的高度";

		//用于临时计算高度
		TextElement tempElement = new TextElement(content, 18, 0, 340);
		tempElement.setCenter(true);
		tempElement.setAutoBreakLine(320, 20, 25);
		int contentHeight = tempElement.getHeight();

		//来一个空的背景
		ImageCombiner combiner = new ImageCombiner(343, 400 + contentHeight, OutputFormat.PNG);
		combiner.setCanvasRoundCorner(60);

		//加入各种元素
		combiner.addImageElement(imgTop, 0, 0);
		combiner.addRectangleElement(0, imgTop.getHeight() - 85, imgTop.getWidth(), 85)
			.setGradient(Color.gray, Color.red, GradientDirection.TopBottom).setAlpha(.3f);  //渐变遮罩
		combiner.addTextElement("德军总部：新血脉", 18, 10, 205).setColor(Color.white);
		combiner.addTextElement("Wolfenstein Yound Blood", 10, 10, 220).setColor(Color.white);
		combiner.addTextElement("第一人称射击", 12, 10, 240).setColor(Color.white);
		combiner.addTextElement("9.0", new Font("微软雅黑", Font.BOLD, 22), 290, 210)
			.setColor(Color.white);
		for (int i = 0; i < 5; i++) {
			combiner.addImageElement(i < 4 ? imgStar : imgGrayStar, 280 + i * 12, 220);
		}
		combiner.addImageElement(imgTop, 10, 270, 40, 40, ZoomMode.WidthHeight).setRoundCorner(300);
		combiner.addTextElement("苏轼的早餐", "微软雅黑", 16, 60, 290);
		combiner.addElement(tempElement);

		combiner.combine();
		combiner.save("d://fonttest.jpg");
	}

	/**
	 * 计算文字高度
	 */
	@Test
	public void testComputeFontHeight() throws Exception {
		ImageCombiner combiner = new ImageCombiner(
			"https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png",
			OutputFormat.JPG);
		combiner.addTextElement("点击画布上方", 60, 0, 0).setLineHeight(200);

		combiner.addRectangleElement(300, 300, 300, 300).setColor(Color.red);
		combiner.combine();
		combiner.save("d://computeHeight.jpg");
	}

	/**
	 * 测试绘制方向
	 */
	@Test
	public void testDirection() throws Exception {
		ImageCombiner combiner = new ImageCombiner(
			"https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png",
			OutputFormat.JPG);
		//单行文本
		combiner.addTextElement("测试绘制方向1", 60, combiner.getCanvasWidth() / 2, 100).setDirection(
			Direction.CenterLeftRight);
		combiner.addTextElement("看看右对齐到效果", 60, 1000, 200).setDirection(Direction.RightLeft);
		//多行文本
		combiner.addTextElement("多行文本多行文本多行文本多行文本多行文本多行文本", 60, 1000, 300)
			.setAutoBreakLine(600, 5, LineAlign.Right)
			.setDirection(Direction.RightLeft);
		//图片
		combiner.addImageElement("http://img.thebeastshop.com/images/index/imgs/8wzZ7St7KH.jpg",
			1000, 600).setDirection(Direction.RightLeft);
		//矩形（右到左）
		combiner.addRectangleElement(1000, 900, 200, 200).setColor(100, 32, 200)
			.setDirection(Direction.RightLeft);
		//矩形（中间到两边）
		combiner.addRectangleElement(1000, 1100, 200, 200).setColor(200, 132, 20)
			.setDirection(Direction.CenterLeftRight);

		combiner.combine();
		combiner.save("d://testDirection.jpg");
	}

	/**
	 * 绘制一个时间轴
	 */
	@Test
	public void testDrawTimeLine() throws Exception {
		ImageCombiner combiner = new ImageCombiner(
			"https://img.thebeastshop.com/combine_image/funny_topic/resource/bg_3x4.png",
			OutputFormat.JPG);
		int startX = 200;
		int startY = 200;
		int stepY = 200;
		int radius = 20;
		int lineWidth = 6;

		for (int i = 0; i < 5; i++) {
			int currentY = startY + stepY * (i + 1);
			//竖线
			if (i < 4) {
				combiner.addRectangleElement(startX, startY + stepY * (i + 1) + radius / 2,
						lineWidth, stepY)
					.setColor(Color.orange)
					.setDirection(Direction.CenterLeftRight);   //用中间到两边，不用考虑限宽问题了（不用再精细的计算x坐标）
			}
			//圆圈
			combiner.addRectangleElement(startX, currentY, radius * 2, radius * 2)
				.setRoundCorner(radius * 2)
				.setColor(Color.green)
				.setDirection(Direction.CenterLeftRight);
			//文本
			TextElement text = combiner.addTextElement("公司于2030年成功在火星设立了办事处", 30,
				startX + radius + 50, currentY);
			combiner.addTextElement("2022-02-16 18:55:26", 26, text.getX() + text.getWidth(),
					currentY + text.getHeight())
				.setDirection(Direction.RightLeft)  //x坐标为上一行文本x+文本宽度，右到左绘制，达到跟随效果
				.setColor(Color.lightGray);
		}

		combiner.combine();
		combiner.save("d://testDrawTimeLine.jpg");
	}

	/**
	 * 显示所有可用字体
	 */
	@Test
	public void showFonts() throws InterruptedException {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontName = e.getAvailableFontFamilyNames();
		for (int i = 0; i < fontName.length; i++) {
			System.out.println(fontName[i]);
		}
	}
}
