package com.taotao.cloud.sys.biz.tools.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.sys.biz.tools.core.dtos.ConnectDto;
import com.taotao.cloud.sys.biz.tools.core.service.file.ConnectServiceOldFileBase;
import com.taotao.cloud.sys.biz.tools.core.validation.custom.EnumStringValue;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * @resourceName 连接管理
 * @parentMenu menu_level_1_basedata
 */
@RestController
@RequestMapping("/connect")
@Validated
public class ConnectController {
    @Autowired
    private ConnectServiceOldFileBase connectService;

    /**
     * 模块列表
     * @return 模块列表
     */
    @GetMapping("/modules")
    public List<String> modules(){
        return connectService.modules();
    }

    /**
     * 创建一个新模块
     * @param name 模块名称
     */
    @PostMapping("/createModule")
    public void createModule(@NotNull @EnumStringValue({"database","kafka","redis","zookeeper","mongo","git"}) String name){
        connectService.createModule(name);
    }

    /**
     * 删除一个模块
     * @param name 模块名称
     * @throws IOException
     */
    @PostMapping("/deleteModule")
    public void deleteModule(@NotNull String name) throws IOException {connectService.dropModule(name);}

    /**
     * 指定模块下的连接列表
     * @param module 模块名称
     * @return
     */
    @GetMapping("/{module}/names")
    public List<String> names(@PathVariable("module") String module){
        return connectService.names(module);
    }

    /**
     * 列出所有连接
     * @return 连接列表
     */
    @GetMapping("/all")
    public List<ConnectDto> connects(){
        return connectService.selectAll();
    }
    /**
     * 获取连接详情
     * @param module 模块名
     * @param connName 连接名
     * @return
     */
    @GetMapping("/{module}/{connName}")
    public String content(@PathVariable("module") String module,@PathVariable("connName")String connName) throws IOException {
        return connectService.content(module,connName);
    }

    /**
     * 创建连接
     * @param module 模块名称
     * @param data 动态数据 ; {@link  AbstractConnectParam}
     * @throws IOException
     */
    @PostMapping("/create/{module}")
    public void createConnect(@PathVariable("module") String module, @RequestBody JSONObject data) throws IOException {
        connectService.createConnect(module,data.toJSONString());
    }

    /**
     * 删除连接; 这个删除不会删除真实连接,真实连接会在项目关闭后释放连接
     * @param module 模块名称
     * @param connName 连接名
     */
    @PostMapping("/dropConnect/{module}/{connName}")
    public void dropConnect(@PathVariable("module") String module,@PathVariable("connName")String connName){
        connectService.dropConnect(module,connName);
    }

    /**
     * 获取连接示例
     * @param module 模块名称
     * @param format 连接格式名称
     * @return
     * @throws IOException
     */
    @GetMapping("/{module}/{format}/example")
    public String connectExampleClass(@PathVariable String module,@PathVariable String format) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("examples/"+module + "." + format + ".example");
        if (classPathResource.exists()){
            return IOUtils.toString(classPathResource.getInputStream(), "utf-8");
        }
        return "";
    }
}
