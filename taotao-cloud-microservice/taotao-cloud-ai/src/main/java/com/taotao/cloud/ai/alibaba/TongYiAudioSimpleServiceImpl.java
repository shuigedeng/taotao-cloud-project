//package com.taotao.cloud.ai.alibaba;
//
//import com.taotao.cloud.ai.tongyi.service.AbstractTongYiServiceImpl;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class TongYiAudioSimpleServiceImpl extends AbstractTongYiServiceImpl {
//	private static final Logger logger = LoggerFactory.getLogger(TongYiService.class);
//	private final SpeechClient speechClient;
//
//	@Autowired
//	public TongYiAudioSimpleServiceImpl(SpeechClient client) {
//		this.speechClient = client;
//	}
//
//	@Override
//	public String genAudio(String text) {
//		logger.info("gen audio prompt is: {}", text);
//		var resWAV = speechClient.call(text);
//		// save的代码省略，就是将音频保存到本地而已
//		return save(resWAV, AudioFormat.WAV.getValue());
//	}
//}
