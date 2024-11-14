package com.taotao.cloud.ai.alibaba;

import org.springframework.ai.image.ImageResponse;

public interface TongYiService {

	/**
	 * 基本问答
	 */
	String completion(String message);

	/**
	 * 文生图
	 */
	ImageResponse genImg(String imgPrompt);

	/**
	 * 语音合成
	 */
	String genAudio(String text);

}
