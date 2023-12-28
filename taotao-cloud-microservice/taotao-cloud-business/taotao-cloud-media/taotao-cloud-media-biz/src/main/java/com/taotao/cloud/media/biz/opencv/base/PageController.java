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

package com.taotao.cloud.media.biz.opencv.base;

import com.taotao.cloud.media.biz.opencv.common.BaseController;
import com.taotao.cloud.media.biz.opencv.common.utils.Constants;
import com.taotao.cloud.media.biz.opencv.common.utils.OpenCVUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Vector;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * PageController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 16:05:06
 */
@Controller
@RequestMapping(value = "page")
public class PageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PageController.class);

    /** 答题卡识别优化 创建者 Songer 创建时间 2018年3月23日 */
    @RequestMapping(value = "pageOCR")
    public void pageOCR(HttpServletResponse response, String imagefile, Integer ocrType) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 页码识别");

        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        // Mat sourceMat = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        // String destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk0.png";
        // Highgui.imwrite(destPath, sourceMat);
        String result = "";
        if (ocrType == 1) { // tesseract ocr 识别方式
            result = getPageNoByTesseract(sourcePath);
        } else if (ocrType == 2) { // 轮廓识别
            result = getPageNoByContours(sourcePath);
        } else {
            result = getPageNoByTemplate(sourcePath);
        }
        renderString(response, result);
    }

    // public static void main(String[] args) {
    // long t1 = new Date().getTime();
    // try {
    // File imageFile = new File("D:\\test\\testImge\\t3.bmp");
    // Tesseract instance = new Tesseract(); // JNA Interface Mapping
    // instance.setLanguage("chi_sim");
    // String result = instance.doOCR(imageFile);
    // LogUtils.info("result=====" + result);
    // long t2 = new Date().getTime();
    // LogUtils.info((t2 - t1));
    // } catch (TesseractException e) {
    // LogUtils.error(e);
    // }
    // }

    /**
     * 使用tesseract方式识别页码，注意tessdata放到tomcat的bin目录下
     *
     * @param filePath
     * @return String
     */
    public String getPageNoByTesseract(String filePath) {
        String result = "";
        try {
            File file = new File(filePath);
            Tesseract instance = new Tesseract(); // JNA Interface Mapping
            instance.setLanguage("chi_sim"); //
            result = instance.doOCR(file);
            logger.info("result====={}", result);
        } catch (TesseractException e) {
            LogUtils.error(e);
        }
        return result;
    }

    /**
     * 使用轮廓识别页码
     *
     * @param filePath
     * @return String
     */
    public String getPageNoByContours(String filePath) {
        Mat source = Highgui.imread(filePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        LogUtils.info("row" + source.rows() + " col " + source.cols());
        // 二值化反转
        Mat bininv = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.threshold(source, bininv, 170, 255, Imgproc.THRESH_BINARY_INV);
        // 腐蚀膨胀，用于消除噪点和干扰项
        Mat destination = OpenCVUtil.eroding(bininv);
        destination = OpenCVUtil.dilation(destination);
        // 轮廓识别
        Vector<MatOfPoint> contours = OpenCVUtil.findContours(destination);
        int pageSize = 0;
        // 原颜色图片加载，用于画出识别轮廓，实际开发不需要
        Mat image = Highgui.imread(filePath, Highgui.CV_LOAD_IMAGE_COLOR);
        for (int i = 0; i < contours.size(); i++) {
            Mat result = new Mat(destination.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
            Imgproc.drawContours(result, contours, i, new Scalar(0, 0, 255), 1);
            MatOfPoint mop = contours.get(i);
            // 获取轮廓面积
            BigDecimal contArea = Math.abs(Imgproc.contourArea(mop, false));
            Rect r = Imgproc.boundingRect(mop);
            LogUtils.info("轮廓面积：" + contArea);
            if (contArea > 1200) { // 此处是根据轮廓面积
                // 红线画出识别的轮廓
                Core.rectangle(
                        image, new Point(r.x, r.y), new Point(r.x + r.width, r.y + r.height), new Scalar(0, 0, 255), 2);
                pageSize++;
            }
        }

        String destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "page0.png";
        Highgui.imwrite(destPath, image);
        LogUtils.info("页码为：" + pageSize);
        return pageSize + "";
    }

    /**
     * 使用模板匹配识别页码
     *
     * @param filePath
     * @return String
     */
    public String getPageNoByTemplate(String filePath) {
        String pageSize = "";
        Mat source = Highgui.imread(filePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        // 二值化反转
        Mat bininv = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.threshold(source, bininv, 170, 255, Imgproc.THRESH_BINARY_INV);
        Vector<MatOfPoint> contours1 = OpenCVUtil.findContours(bininv);
        MatOfPoint mop = contours1.get(0);
        Rect rect = Imgproc.boundingRect(mop);
        Mat matchtemp = source.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
        String page_temp = Constants.PATH + Constants.DEST_IMAGE_PATH + "page_temp.png";
        Highgui.imwrite(page_temp, matchtemp);
        String pagePath = Constants.PATH + Constants.SOURCE_IMAGE_PATH + "shuzi.png";
        Mat pageimage = Highgui.imread(pagePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        int result_cols = pageimage.cols() - matchtemp.cols() + 1;
        int result_rows = pageimage.rows() - matchtemp.rows() + 1;
        Mat destination = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(pageimage, matchtemp, destination, Imgproc.TM_CCOEFF);
        // 矩阵归一化处理
        Core.normalize(destination, destination, 0, 255, Core.NORM_MINMAX, -1, new Mat());
        MinMaxLocResult minmaxLoc = Core.minMaxLoc(destination);
        Point matchLoc = minmaxLoc.maxLoc;
        Core.rectangle(
                pageimage,
                matchLoc,
                new Point(matchLoc.x + matchtemp.cols(), matchLoc.y + matchtemp.rows()),
                new Scalar(0),
                2);
        LogUtils.info(matchLoc.x + "   " + matchLoc.y);
        pageSize = getPage(matchLoc.x) + "";
        String destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "page1.png";
        Highgui.imwrite(destPath, pageimage);
        return pageSize;
    }

    /**
     * 根据横坐标返回页码
     *
     * @param x
     * @return int
     */
    public int getPage(BigDecimal x) {
        // 减去2像素，是因为shuzi.png外边框是预留了2像素的，因此匹配结果坐标为：2,2;72,2;142,2
        // Math.floor 返回不大于的最大整数
        return (int) Math.floor((x - 2) / 70) + 1;
    }

    // public static void main(String[] args) {
    //	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    //	Mat markmat1 = Highgui.imread("D:\\test\\abc\\1.png", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    //	Mat markmat2 = Highgui.imread("D:\\test\\abc\\2.png", Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    //	Vector<MatOfPoint> contours1 = OpenCVUtil.findContours(markmat1);
    //	Vector<MatOfPoint> contours2 = OpenCVUtil.findContours(markmat2);
    //	Mat mat1 = getSimMark("D:\\test\\abc\\1.png");
    //	Mat mat2 = getSimMark("D:\\test\\abc\\2.png");
    //	BigDecimal result1 = Imgproc.matchShapes(contours1.get(0), contours2.get(0),
    //		Imgproc.CV_CONTOURS_MATCH_I1, 0);
    //	BigDecimal result2 = Imgproc.matchShapes(mat1, mat2, Imgproc.CV_CONTOURS_MATCH_I1, 0);
    //	LogUtils.info(result1);
    //	LogUtils.info(result2);
    // }

    private static MatOfPoint getSimMark(String path) {
        Mat markmat = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        // Imgproc.threshold(markmat, markmat, 190, 255, Imgproc.THRESH_BINARY_INV);
        Vector<MatOfPoint> contours = new Vector<MatOfPoint>();
        Mat rsmat = new Mat();
        Imgproc.findContours(markmat, contours, rsmat, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point());
        MatOfPoint markMop = new MatOfPoint();
        Mat result = new Mat(markmat.size(), CvType.CV_8U, new Scalar(255));
        Imgproc.drawContours(result, contours, 0, new Scalar(0), 1);
        String image1 = "d:\\test\\abc\\t.jpg";
        Highgui.imwrite(image1, result);
        for (int i = 0; i < contours.size(); i++) {
            markMop = contours.get(0);
            // MatOfPoint2f mat2f = new MatOfPoint2f();
            // MatOfPoint2f dstmat2f = new MatOfPoint2f();
            // markMop.convertTo(mat2f, CvType.CV_32FC1);
            // // 多边形逼近算法,减少轮廓的顶点，便于对比
            // Imgproc.approxPolyDP(mat2f, dstmat2f, markMop.total() * 0.02, true);
            // dstmat2f.convertTo(markMop, CvType.CV_32S);
        }
        return markMop;
    }
}
