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

package com.taotao.cloud.media.biz.opencv.demo;

import com.taotao.cloud.media.biz.opencv.common.BaseController;
import com.taotao.cloud.media.biz.opencv.common.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** OpenCV人脸识别demo 创建者 Songer 创建时间 2018年3月9日 */
@Controller
@RequestMapping(value = "demo")
public class DemoController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @RequestMapping(value = "detectFace")
    public void detectFace(HttpServletResponse response, HttpServletRequest request, String url) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        LogUtils.info("===========java.library.path:" + System.getProperty("java.library.path"));
        logger.info("\nRunning DetectFaceDemo");
        String resourcePath =
                getClass().getResource("/lbpcascade_frontalface.xml").getPath().substring(1);
        logger.info("resourcePath============" + resourcePath);

        CascadeClassifier faceDetector = new CascadeClassifier(resourcePath);
        logger.info("url==============" + Constants.PATH + url);
        Mat image = Highgui.imread(Constants.PATH + url);
        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        logger.info(String.format("Detected %s faces", faceDetections.toArray().length));
        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Core.rectangle(
                    image,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        String filename = url.substring(url.lastIndexOf("/"), url.length());
        LogUtils.info(String.format("Writing %s", Constants.PATH + Constants.DEST_IMAGE_PATH + filename));
        Highgui.imwrite(Constants.PATH + Constants.DEST_IMAGE_PATH + filename, image);
        renderString(response, Constants.SUCCESS);
    }

    // public static void main(String[] args) {
    //	LogUtils.info("Hello, OpenCV");
    //	// Load the native library.
    //	System.loadLibrary("opencv_java2413");
    //	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    //	LogUtils.info(
    //		"===========java.library.path:" + System.getProperty("java.library.path"));
    //
    // }
}
