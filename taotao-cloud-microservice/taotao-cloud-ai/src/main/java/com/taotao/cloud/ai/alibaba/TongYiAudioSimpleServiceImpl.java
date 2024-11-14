package com.taotao.cloud.ai.alibaba;

import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class TongYiAudioSimpleServiceImpl extends AbstractTongYiServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(TongYiService.class);
    private final SpeechClient speechClient;
    @Autowired
    public TongYiAudioSimpleServiceImpl(SpeechClient client) {
       this.speechClient = client;
    }
    @Override
    public String genAudio(String text) {
       logger.info("gen audio prompt is: {}", text);
       var resWAV = speechClient.call(text);
       // save的代码省略，就是将音频保存到本地而已
       return save(resWAV, SpeechSynthesisAudioFormat.WAV.getValue());
    }
}
