package com.taotao.cloud.ai.alibaba;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class TongYiImagesServiceImpl extends AbstractTongYiServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(TongYiService.class);
    private final ImageClient imageClient;
    @Autowired
    public TongYiImagesServiceImpl(ImageClient client) {
       this.imageClient = client;
    }
    @Override
    public ImageResponse genImg(String imgPrompt) {
       var prompt = new ImagePrompt(imgPrompt);
       return imageClient.call(prompt);
    }
}
