//package com.taotao.cloud.auth.biz.authentication.login.extension.face.baidutmp;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FaceService {
//
//    @Autowired
//    private BaiduAiUtils baiduAiUtils;
//
//    /**
//     * 人脸登录
//     */
//    public String loginByFace(StringBuffer imagebast64) {
//    	// 处理base64编码内容
//        String image = imagebast64.substring(imagebast64.indexOf(",") + 1, imagebast64.length());
//        String userId = baiduAiUtils.faceSearch(image);
//        return userId;
//    }
//
//    /**
//     * 人脸注册
//     */
//    public Boolean registerFace(String userId, StringBuffer imagebast64) {
//  	    // 处理base64编码内容
//        String image = imagebast64.substring(imagebast64.indexOf(",") + 1, imagebast64.length());
//        Boolean registerFace = baiduAiUtils.faceRegister(userId, image);
//        return registerFace;
//    }
//}
//
