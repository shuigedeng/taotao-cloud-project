package com.taotao.cloud.sys.biz.tools.swaggerdoc.controller;

import com.taotao.cloud.sys.biz.tools.swaggerdoc.service.SwaggerJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
@RequestMapping("/swagger")
@Validated
public class SwaggerDocController {
    @Autowired
    private SwaggerJsonParser swaggerJsonParser;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    /**
     *
     * @param url 需要是能获取 json 文档的地址 http://10.101.72.76:9949/v2/api-docs
     * @return
     * @throws IOException
     */
    @GetMapping("/doc")
    public ModelAndView generaterDoc(@NotNull String url) throws IOException {
        url = parseUrl(url);
        Doc doc = swaggerJsonParser.doc(url);
        ModelAndView modelAndView = new ModelAndView("word");
        modelAndView.addObject("doc",doc);
        return modelAndView;
    }

    @GetMapping("/doc/download")
    public ResponseEntity<ByteArrayResource> docWord(@NotNull String url) throws IOException {
        url = parseUrl(url);
        Doc doc = swaggerJsonParser.doc(url);
        Context context = new Context();
        context.setVariable("doc",doc);
        String process = springTemplateEngine.process("word.html", context);

        ByteArrayResource byteArrayResource = new ByteArrayResource(process.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "swagger.doc");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("fileName","swagger.doc");
        headers.add("Access-Control-Expose-Headers", "fileName");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");

        ResponseEntity<ByteArrayResource> body = ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .body(byteArrayResource);
        return body;
    }

    /**
     *
     * @param address
     * @return
     */
    private String parseUrl(String address){
        try {
            URL url = new URL(address);
            String host = url.getHost();
            int port = url.getPort();
            String protocol = url.getProtocol();

            return protocol+"://"+host+":"+port+"/v2/api-docs";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return address;
    }
}
