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
        return "VerifyImageDTO{" +
                "verifyId='" + verifyId + '\'' +
                ", verifyType='" + verifyType + '\'' +
                ", verifyImgStr='" + verifyImgStr + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
