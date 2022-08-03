package com.taotao.cloud.sys.biz.api.controller.tools.compiler.dtos;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;
import java.net.URISyntaxException;

public class SourceJavaFileObject extends SimpleJavaFileObject {
    private String className;
    private String source;

    public SourceJavaFileObject(String className, String source) throws URISyntaxException {
        super(new URI(className+".java"),Kind.SOURCE);
        this.className = className;
        this.source = source;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }
}
