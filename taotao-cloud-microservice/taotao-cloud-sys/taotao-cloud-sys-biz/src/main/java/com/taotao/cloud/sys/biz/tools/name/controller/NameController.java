package com.taotao.cloud.sys.biz.tools.name.controller;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/name")
@Validated
public class NameController {
    @Autowired
    private BizTranslate bizTranslate;

    @Autowired
    private NameService nameService;

    @GetMapping("/translate")
    public Set<String> translate(@NotNull String orginChars, @NotNull String tokenizer, String[] tranlates, String[] bizs){
        return nameService.translate(orginChars,tokenizer, bizs,tranlates);
    }

    @GetMapping("/tokenizers")
    public List<String> tokenizers(){
       return nameService.tokenizers();
    }

    @GetMapping("/bizs")
    public List<String> bizs(){
        return bizTranslate.bizs();
    }

    @GetMapping("/englishs")
    public List<String> supportEnglishs(){
        return nameService.englishTranslate();
    }

    @GetMapping("/detail/{biz}")
    public List<String> bizMirrors(@PathVariable("biz") String biz) throws IOException {
        return bizTranslate.mirrors(biz);
    }

    @GetMapping("/content/bizs")
    public List<String> bizsContent(String [] bizs) throws IOException {
        List<String> contentMerge = new ArrayList<>();
        for (String biz : bizs) {
            List<String> mirrors = bizTranslate.mirrors(biz);
            contentMerge.addAll(mirrors);
        }
        return contentMerge;
    }

    @PostMapping("/mirror/write/{biz}")
    public void writeMirror(@PathVariable("biz") String biz,@RequestBody String content) throws IOException {
        bizTranslate.writeMirror(biz,content);
    }

    /**
     * 多列翻译
     * @param words
     * @param english
     * @return
     */
    @GetMapping("/multiTranslate")
    public List<String> multiTranslate(String [] words,String english){
        return nameService.multiTranslate(words,english);
    }

    /**
     * 多列翻译 , 转下划线版本
     */
    Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
    @GetMapping("/mutiTranslateUnderline")
    public List<String> mutiTranslateUnderline(String [] words,@NotNull String english){
        List<String> results = nameService.multiTranslate(words, english);
        List<String> collect = results.stream().map(converter::convert).collect(Collectors.toList());
        return collect;
    }
}
