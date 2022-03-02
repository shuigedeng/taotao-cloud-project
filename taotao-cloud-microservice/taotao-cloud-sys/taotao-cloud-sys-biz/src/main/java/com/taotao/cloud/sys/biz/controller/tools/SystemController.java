package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.sys.biz.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SystemService systemService;

    /**
     * 下载公钥
     */
    @GetMapping("/download/publicKey")
    public ResponseEntity downloadPublicKey() throws IOException {
        final File file = systemService.publicKeyFile();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "id_rsa.pub");
        headers.add("fileName","id_rsa.pub");
        headers.add("Access-Control-Expose-Headers", "fileName");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        final FileSystemResource fileSystemResource = new FileSystemResource(file);

        ResponseEntity<Resource> body = ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileSystemResource.contentLength())
                .body(fileSystemResource);
        return body;
    }
}
