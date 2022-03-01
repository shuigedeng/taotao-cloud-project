package com.taotao.cloud.sys.biz.tools.name.service;

import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Component
public class BizTranslate {
    @Autowired
    private FileManager fileManager;

    public static final String MODULE = "translate";

    public void doTranslate(String[] bizs, TranslateCharSequence translateCharSequence) {
        if (bizs == null) {
            return ;
        }

        // 按顺序找到所有的业务词映射, 后面的覆盖前面的
        Properties properties = new Properties();
        for (String biz : bizs) {
            try {
                Properties mirrmorKeyValue = bizMirrmorKeyValue(biz);
                properties.putAll(mirrmorKeyValue);
            } catch (IOException e) {
                log.error("translate load biz word error : {}",e.getMessage(),e);
            }
        }

        // 进行业务词翻译
        Set<String> needTranslateWords = translateCharSequence.getNeedTranslateWords();
        for (String needTranslateWord : needTranslateWords) {
            Object object = properties.get(needTranslateWord);
            if (object == null){
                continue;
            }
            String bizMirror = Objects.toString(object);
            if (StringUtils.isNotBlank(bizMirror)) {
                translateCharSequence.addTranslate(needTranslateWord, bizMirror);
                translateCharSequence.setTranslate(true, needTranslateWord);
            }
        }
    }

    public List<String> bizs() {
        return fileManager.simpleConfigNames(MODULE);
    }

    public List<String> mirrors(String biz) throws IOException {
        String content = fileManager.readConfig(MODULE, biz);
        return Arrays.asList(StringUtils.split(content,'\n'));
    }

    public Properties bizMirrmorKeyValue(String biz) throws IOException {
        String content = fileManager.readConfig(MODULE, biz);
        StringReader stringReader = new StringReader(content);
        Properties properties = new Properties();
        properties.load(stringReader);
        stringReader.close();
        return properties;
    }


    public void writeMirror(String biz, String content) throws IOException {
        fileManager.writeConfig(MODULE,biz,content);
    }
}
