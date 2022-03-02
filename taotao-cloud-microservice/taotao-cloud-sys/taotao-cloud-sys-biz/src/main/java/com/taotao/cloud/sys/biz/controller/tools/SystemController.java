package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.model.Version;
import com.taotao.cloud.sys.biz.service.SystemService;
import com.taotao.cloud.sys.biz.service.VersionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:55:38
 */
@Validated
@RestController
@Tag(name = "工具管理端-system管理API", description = "工具管理端-system管理API")
@RequestMapping("/sys/tools/system")
public class SystemController {

    @Autowired
    private SystemService systemService;

	@Autowired
	private VersionService versionService;

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

	/**
	 * 当前版本
	 */
	@GetMapping
	public String current(){
		return versionService.currentVersion().toString();
	}

	/**
	 * 当前版本详细信息
	 */
	@GetMapping("/detail")
	public Version detail(){
		return versionService.currentVersion();
	}
}
