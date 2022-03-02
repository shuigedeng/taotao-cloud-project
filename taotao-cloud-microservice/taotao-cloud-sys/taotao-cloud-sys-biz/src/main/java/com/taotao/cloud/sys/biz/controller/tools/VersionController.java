package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.sys.biz.service.VersionService;
import com.taotao.cloud.sys.biz.tools.core.utils.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
public class VersionController {
   @Autowired
   private VersionService versionService;

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
