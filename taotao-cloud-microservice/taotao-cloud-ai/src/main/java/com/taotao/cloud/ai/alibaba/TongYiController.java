package com.taotao.cloud.ai.alibaba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public class TongYiController {
    @Autowired
    @Qualifier("tongYiSimpleServiceImpl")
    private TongYiService tongYiSimpleService;

    @GetMapping("/simple")
    public String completion(
            @RequestParam(value = "message", defaultValue = "AI时代下Java开发者该何去何从？")
            String message
    ) {
        return tongYiSimpleService.completion(message);
    }
}
