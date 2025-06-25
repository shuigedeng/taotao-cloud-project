package com.taotao.cloud.ai.alibaba.structured_output.controller;

import com.taotao.cloud.ai.alibaba.structured_output.entity.BeanEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @date 2025/5/22 22:20
 */

@RestController
@RequestMapping("/bean")
public class BeanController {

    private static final Logger log = LoggerFactory.getLogger(BeanController.class);

    private final ChatClient chatClient;
    private final BeanOutputConverter<BeanEntity> converter;

    public BeanController(ChatClient.Builder builder) {
        this.converter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<BeanEntity>() {
                }
        );
        this.chatClient = builder
                .build();
    }

    @GetMapping("/call")
    public String call(@RequestParam(value = "query", defaultValue = "以影子为作者，写一篇200字左右的有关人工智能诗篇") String query) {
        String result = chatClient.prompt(query)
                .call().content();

        log.info("result: {}", result);
        assert result != null;
        try {
            BeanEntity convert = converter.convert(result);
            log.info("反序列成功，convert: {}", convert);
        } catch (Exception e) {
            log.error("反序列化失败");
        }
        return result;
    }

    @GetMapping("/call/format")
    public BeanEntity callFormat(@RequestParam(value = "query", defaultValue = "以影子为作者，写一篇200字左右的有关人工智能诗篇") String query) {
        return chatClient.prompt(query)
                .call().entity(BeanEntity.class);
    }

}
