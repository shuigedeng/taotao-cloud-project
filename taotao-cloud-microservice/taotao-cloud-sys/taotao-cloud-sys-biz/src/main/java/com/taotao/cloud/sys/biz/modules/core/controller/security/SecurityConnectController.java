package com.taotao.cloud.sys.biz.modules.core.controller.security;

import com.taotao.cloud.sys.biz.modules.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.modules.core.service.connect.dtos.ConnectInput;
import com.taotao.cloud.sys.biz.modules.core.service.connect.dtos.ConnectOutput;
import com.taotao.cloud.sys.biz.modules.core.service.connect.dtos.ConnectTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/security/connect")
public class SecurityConnectController {
    private final ConnectService connectService;

    public SecurityConnectController(ConnectService connectService) {
        this.connectService = connectService;
    }

    /**
     * 模块列表
     */
    @GetMapping("/modules")
    public List<String> modules(){
        return connectService.modules();
    }

    /**
     * 加载模块连接模板
     * @param module 模块名
     * @return
     */
    @GetMapping("/{module}/template")
    public ConnectTemplate connectTemplate(@NotBlank @PathVariable("module") String module){
        return connectService.connectTemplate(module);
    }

    /**
     * 所有可以访问的连接列表
     */
    @GetMapping("/connects")
    public List<ConnectOutput> connects(){
        return connectService.connects();
    }

    /**
     * 模块可访问连接列表
     * @param module 模块名
     */
	@GetMapping("/{module}/connects")
    public List<ConnectOutput> moduleConnects(@NotBlank @PathVariable("module") String module){
        return connectService.moduleConnects(module);
    }

    /**
     * 连接列表, 只有名称信息
     * @param module
     * @return
     */
    @GetMapping("/{module}/connectNames")
    public List<String> moduleConnectNames(@NotBlank @PathVariable("module") String module){
        final List<ConnectOutput> connectOutputs = connectService.moduleConnects(module);
        return connectOutputs.stream().map(ConnectOutput::getConnectInput).map(ConnectInput::getBaseName).collect(Collectors.toList());
    }

    /**
     * 连接配置详情
     * @param module 模块名
     * @param baseName 基础名
     */
    @GetMapping("/loadContent")
    public String loadContent(@NotBlank String module,@NotBlank String baseName) throws IOException {
        return connectService.loadContent(module,baseName);
    }

    /**
     * 写入配置
     * @param connectInput 配置信息
     */
    @PostMapping("/writeConfig")
    public void writeConfig(@RequestBody @Validated ConnectInput connectInput) throws IOException {
        connectService.updateConnect(connectInput);
    }

    /**
     * 删除连接
     * @param module 模块名
     * @param baseName 基础名
     */
    @PostMapping("/delConnect")
    public void delConnect(@NotBlank String module,@NotBlank String baseName){
        connectService.deleteConnect(module,baseName);
    }

}
