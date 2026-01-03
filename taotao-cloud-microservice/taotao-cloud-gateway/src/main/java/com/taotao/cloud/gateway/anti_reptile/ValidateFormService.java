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

package com.taotao.cloud.gateway.anti_reptile;

import com.taotao.cloud.gateway.anti_reptile.module.VerifyImageDTO;
import com.taotao.cloud.gateway.anti_reptile.module.VerifyImageVO;
import com.taotao.cloud.gateway.anti_reptile.rule.RuleActuator;
import com.taotao.cloud.gateway.anti_reptile.util.VerifyImageUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.BeanUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * ValidateFormService
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ValidateFormService {

    private final RuleActuator actuator;
    private final VerifyImageUtil verifyImageUtil;

    public ValidateFormService( RuleActuator actuator, VerifyImageUtil verifyImageUtil ) {
        this.actuator = actuator;
        this.verifyImageUtil = verifyImageUtil;
    }

    public String validate( ServerWebExchange exchange ) throws UnsupportedEncodingException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> items = new ArrayList<>();

        // try {
        //	items = upload.parseRequest(exchange.getRequest().);
        // } catch (FileUploadException e) {
        //	LogUtils.error(e);
        // }

        Map<String, String> params = new HashMap<>();
        params.putAll(Objects.requireNonNull(exchange.getFormData().block()).toSingleValueMap());

        for (Object object : items) {
            FileItem fileItem = (FileItem) object;
            if (fileItem.isFormField()) {
                params.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
            }
        }

        String verifyId = params.get("verifyId");
        String result = params.get("result");
        String realRequestUri = params.get("realRequestUri");
        String actualResult = verifyImageUtil.getVerifyCodeFromRedis(verifyId);

        if (actualResult != null && actualResult.equals(result.toLowerCase())) {
            actuator.reset(exchange, realRequestUri);
            return "{\"result\":true}";
        }
        return "{\"result\":false}";
    }

    public String refresh( ServerRequest request ) {
        String verifyId = request.queryParam("verifyId").get();
        verifyImageUtil.deleteVerifyCodeFromRedis(verifyId);
        VerifyImageDTO verifyImage = verifyImageUtil.generateVerifyImg();
        verifyImageUtil.saveVerifyCodeToRedis(verifyImage);
        VerifyImageVO verifyImageVO = new VerifyImageVO();
        BeanUtils.copyProperties(verifyImage, verifyImageVO);
        return "{\"verifyId\": \""
                + verifyImageVO.getVerifyId()
                + "\",\"verifyImgStr\": \""
                + verifyImageVO.getVerifyImgStr()
                + "\"}";
    }
}
