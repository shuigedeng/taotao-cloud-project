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

package com.taotao.cloud.gateway.anti_reptile.module;

import java.io.Serializable;

public class VerifyImageDTO implements Serializable {

    private static final long serialVersionUID = 6741944800448697513L;

    private String verifyId;
    private String verifyType;
    private String verifyImgStr;
    private String result;

    public VerifyImageDTO(String verifyId, String verifyType, String verifyImgStr, String result) {
        this.verifyId = verifyId;
        this.verifyType = verifyType;
        this.verifyImgStr = verifyImgStr;
        this.result = result;
    }

    public String getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(String verifyId) {
        this.verifyId = verifyId;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getVerifyImgStr() {
        return verifyImgStr;
    }

    public void setVerifyImgStr(String verifyImgStr) {
        this.verifyImgStr = verifyImgStr;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "VerifyImageDTO{"
                + "verifyId='"
                + verifyId
                + '\''
                + ", verifyType='"
                + verifyType
                + '\''
                + ", verifyImgStr='"
                + verifyImgStr
                + '\''
                + ", result='"
                + result
                + '\''
                + '}';
    }
}
