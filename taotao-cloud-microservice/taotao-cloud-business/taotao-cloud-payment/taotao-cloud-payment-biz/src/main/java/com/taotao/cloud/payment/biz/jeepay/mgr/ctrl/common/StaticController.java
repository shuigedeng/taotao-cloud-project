/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.biz.jeepay.mgr.ctrl.common;

import com.taotao.cloud.payment.biz.jeepay.mch.ctrl.CommonCtrl;
import com.taotao.cloud.payment.biz.jeepay.oss.config.OssYmlConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * 静态文件下载/预览 ctrl
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:08
 */
@Controller
public class StaticController extends CommonCtrl {

    @Autowired
    private OssYmlConfig ossYmlConfig;

    /** 图片预览 * */
    @GetMapping("/api/anon/localOssFiles/**/*.*")
    public ResponseEntity<?> imgView() {

        try {

            // 查找图片文件
            File imgFile = new File(ossYmlConfig.getOss().getFilePublicPath()
                    + File.separator
                    + request.getRequestURI().substring(24));
            if (!imgFile.isFile() || !imgFile.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 输出文件流（图片格式）
            HttpHeaders httpHeaders = new HttpHeaders();
            //            httpHeaders.setContentType(MediaType.IMAGE_JPEG);  //图片格式
            InputStream inputStream = new FileInputStream(imgFile);
            return new ResponseEntity<>(new InputStreamResource(inputStream), httpHeaders, HttpStatus.OK);

        } catch (FileNotFoundException e) {
            logger.error("static file error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
