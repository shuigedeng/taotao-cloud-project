package com.taotao.cloud.sys.biz.tools.core.controller;

import com.taotao.cloud.sys.biz.tools.core.dtos.SpiderDataParam;
import com.taotao.cloud.sys.biz.tools.core.service.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.tools.core.service.data.JsoupSpiderDataService;
import com.taotao.cloud.sys.biz.tools.core.service.data.RandomDataService;
import com.taotao.cloud.sys.biz.tools.core.service.data.RegexRandomDataService;
import com.taotao.cloud.sys.biz.tools.core.service.data.regex.OrdinaryNode;
import com.taotao.cloud.sys.biz.tools.core.service.data.regex.exception.RegexpIllegalException;
import com.taotao.cloud.sys.biz.tools.core.service.data.regex.exception.TypeNotMatchException;
import com.taotao.cloud.sys.biz.tools.core.service.data.regex.exception.UninitializedException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
@Validated
public class RandomDataController {
    @Autowired
    private RandomDataService randomDataService;
    @Autowired
    private RegexRandomDataService regexRandomDataService;
    @Autowired
    private JsoupSpiderDataService jsoupSpiderDataService;

    @Autowired
    private ClassloaderService classloaderService;

    /**
     * 随机填充数据
     * @param className 类名
     * @param classloaderName 类加载器名称
     * @return
     * @throws ClassNotFoundException
     */
    @GetMapping("/random")
    public Object randomData(@NotNull String className, @NotNull String classloaderName) throws ClassNotFoundException {
        ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        return randomDataService.randomData(className,classloader);
    }

    /**
     * 随机填充列表数据
     * @param className 类名
     * @param classloaderName 类加载器名称
     * @return
     * @throws ClassNotFoundException
     */
    @GetMapping("/random/list")
    public List<Object> randomListData(@NotNull String className, @NotNull String classloaderName,@NotBlank String length) throws ClassNotFoundException {
        List<Object> list = new ArrayList<>();
        ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        for (int i = 0; i < NumberUtils.toInt(length,1); i++) {
            Object randomData = randomDataService.randomData(className, classloader);
            list.add(randomData);
        }
        return list;
    }

    /**
     * 使用正则表达式随机填充数据
     * @param className         类名
     * @param classloaderName   类加载器名称
     * @return
     */
    @PostMapping("/random/regex")
    public List<String> regexRandomData(@NotNull String regexBase64, @NotBlank String length) throws RegexpIllegalException, TypeNotMatchException, UninitializedException {
        String regex = new String(Base64.decodeBase64(regexBase64),StandardCharsets.UTF_8);
        if (StringUtils.isBlank(regex)){
            return new ArrayList<>();
        }

        // 去掉首尾限制符
        if (regex.startsWith("^")){
            regex = regex.substring(1);
        }
        if (regex.endsWith("$")){
            regex = regex.substring(0,regex.length() - 1);
        }
        OrdinaryNode ordinaryNode = new OrdinaryNode(regex);
        List<String> values = new ArrayList<>();
        for (int i = 0; i < NumberUtils.toInt(length,1); i++) {
            String random = ordinaryNode.random();
            values.add(random);
        }
        return values;
    }


    /**
     * 爬取数据
     * 这里提供的类 需要有 @Request 标记
     * @param spiderDataParam 爬取数据参数
     * @return
     */
    @PostMapping("/spider")
    public Object spiderData(@RequestBody SpiderDataParam spiderDataParam) throws IOException, ClassNotFoundException {
        String classloaderName = spiderDataParam.getClassloaderName();
        String className = spiderDataParam.getClassName();
        Map<String, String> params = spiderDataParam.getParams();

        ClassLoader classloader = classloaderService.getClassloader(classloaderName);
        return jsoupSpiderDataService.spiderData(className,classloader,params);
    }
}
