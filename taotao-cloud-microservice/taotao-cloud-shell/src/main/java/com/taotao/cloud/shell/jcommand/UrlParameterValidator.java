package com.taotao.cloud.shell.jcommand;

import java.net.MalformedURLException;

public class UrlParameterValidator implements IParameterValidator {
    @Override
    public void validate(String key, String value) throws ParameterException {
        try {
            new URL(value);
        } catch (MalformedURLException e) {
            throw new ParameterException("参数 " + key + " 的值必须是 URL 格式");
        }
    }
}
