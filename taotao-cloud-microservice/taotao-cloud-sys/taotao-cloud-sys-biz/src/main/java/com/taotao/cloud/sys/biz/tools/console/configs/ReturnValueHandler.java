package com.taotao.cloud.sys.biz.tools.console.configs;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.sanri.tools.modules.core.dtos.ResponseDto;

public class ReturnValueHandler extends RequestResponseBodyMethodProcessor {

    public ReturnValueHandler(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

        Executable executable = returnType.getExecutable();
        Type type = executable.getAnnotatedReturnType().getType();
        if (type == Void.TYPE){
            returnValue = ResponseDto.ok();
        }else if (returnValue instanceof ResponseEntity){
            ResponseEntity responseEntity = (ResponseEntity) returnValue;
            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode != HttpStatus.OK){
                Object body = responseEntity.getBody();
                logger.error(responseEntity);
                ResponseDto err = ResponseDto.err(responseEntity.getStatusCodeValue() + "");
                if (body instanceof LinkedHashMap){
                    Map map = (Map) body;
                    String message = Objects.toString(map.get("message"));
                    String error = Objects.toString(map.get("error"));
                    String path = Objects.toString(map.get("path"));
                    String format = MessageFormat.format("message:{0},error:{1},path:{2}", message, err, path);
                    err.message(format);
                }
                returnValue = err;
            }
        }else if (type != ResponseDto.class){
            returnValue = ResponseDto.ok().data(returnValue);
        }

        // Try even with null return value. ResponseBodyAdvice could get involved.
        writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
    }
}
