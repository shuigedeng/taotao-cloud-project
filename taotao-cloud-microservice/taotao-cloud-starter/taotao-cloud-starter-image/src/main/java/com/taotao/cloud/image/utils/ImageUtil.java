package com.taotao.cloud.image.utils;

import com.freewayso.image.combiner.ImageCombiner;
import com.freewayso.image.combiner.enums.OutputFormat;

import java.awt.*;

public class ImageUtil {
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
}
