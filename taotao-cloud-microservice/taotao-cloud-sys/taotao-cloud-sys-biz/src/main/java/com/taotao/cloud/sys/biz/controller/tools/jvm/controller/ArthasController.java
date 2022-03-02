package com.taotao.cloud.sys.biz.controller.tools.jvm.controller;

import com.taotao.cloud.sys.biz.controller.tools.jvm.service.ArthasService;
import java.io.IOException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arthas")
public class ArthasController {

    @Autowired
    private ArthasService arthasService;

    public void linkArthas(String connName,String port) throws IOException {
        arthasService.linkArthas(connName, NumberUtils.toInt(port));
    }

    /**
     * 执行一个命令
     * @param command
     * @return
     */
    @GetMapping("/execCommand")
    public Object execCommand(String connName, String command) throws IOException {
        return arthasService.sendCommand(connName,command);
    }
}
