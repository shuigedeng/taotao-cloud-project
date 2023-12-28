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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
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
 * RectificationController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 16:06:36
 */
@Controller
@RequestMapping(value = "rect")
public class RectificationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RectificationController.class);

    /** 图像矫正透视变换 创建者 Songer 创建时间 2018年4月10日 */
    @RequestMapping(value = "rectification")
    public void rectification(HttpServletResponse response, String imagefile, Integer markType) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 图像矫正透视变换");

        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        // 加载为灰度图显示
        Mat source1 = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat source2 = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Point anchor01 = new Point();
        Point anchor02 = new Point();
        Point anchor03 = new Point();
        Point anchor04 = new Point();
        if (markType == 1) { // 模板匹配识别定位点
            String matchPath = Constants.PATH + Constants.SOURCE_IMAGE_PATH + "z1_temp.png";
            fetchAnchorPoints1(sourcePath, matchPath, anchor01, anchor02, anchor03, anchor04);
        } else if (markType == 2) { // 霍夫圆检测识别定位点
            fetchAnchorPoints2(sourcePath, anchor01, anchor02, anchor03, anchor04);
        }
        MatOfPoint mop = new MatOfPoint(anchor01, anchor02, anchor03, anchor04);
        MatOfPoint2f mat2f = new MatOfPoint2f();
        MatOfPoint2f refmat2f = new MatOfPoint2f();
        mop.convertTo(mat2f, CvType.CV_32FC1);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        contours.add(mop);
        Core.polylines(source2, contours, true, new Scalar(0, 0, 255), 1);
        String destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect1.png";
        Highgui.imwrite(destPath, source2);
        // Point point11 = anchor01;
        // Point point12 = new Point(anchor02.x, anchor01.y);
        // Point point13 = new Point(anchor01.x, anchor03.y);
        // Point point14 = new Point(anchor02.x, anchor03.y);
        Point point11 = new Point(99, 200);
        Point point12 = new Point(2317, 200);
        Point point13 = new Point(99, 3300);
        Point point14 = new Point(2317, 3300);

        Mat dst_vertices = new MatOfPoint(point11, point12, point13, point14);
        dst_vertices.convertTo(refmat2f, CvType.CV_32FC1);
        Mat warpMatrix = Imgproc.getPerspectiveTransform(mat2f, refmat2f);

        Mat dst = new Mat(source1.rows(), source1.cols(), source1.type());
        LogUtils.info(source1.rows() + " " + source1.cols());
        Imgproc.warpPerspective(
                source1, dst, warpMatrix, dst.size(), Imgproc.INTER_LINEAR, 0, new Scalar(255, 255, 255));
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect2.png";
        Highgui.imwrite(destPath, dst);
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(dst);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 获得锚点(定位点) 方法1，通过模板匹配圆心，应该换成正方形也可以，之前模板匹配不行是因为模板图形不是最小的
     *
     * @param anchor01
     * @param anchor02
     * @param anchor03
     * @param anchor04 void
     */
    public static void fetchAnchorPoints1(
            String sourcePath, String matchPath, Point anchor01, Point anchor02, Point anchor03, Point anchor04) {
        Mat imagematch = new Mat();
        Mat colorimage = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        Point maxLoc01, maxLoc02, maxLoc03, maxLoc04;
        int srcRows = colorimage.rows();
        int srcCols = colorimage.cols();
        Mat src01 = colorimage.submat(new Rect(0, 0, srcCols / 2, srcRows / 2));
        Mat src02 = colorimage.submat(new Rect(srcCols / 2, 0, srcCols / 2, srcRows / 2));
        Mat src03 = colorimage.submat(new Rect(0, srcRows / 2, srcCols / 2, srcRows / 2));
        Mat src04 = colorimage.submat(new Rect(srcCols / 2, srcRows / 2, srcCols / 2, srcRows / 2));
        // Highgui.imwrite("D://ttt/t1.jpg", src01);
        // Highgui.imwrite("D://ttt/t2.jpg", src02);
        // Highgui.imwrite("D://ttt/t3.jpg", src03);
        // Highgui.imwrite("D://ttt/t4.jpg", src04);
        Mat mattmp = Highgui.imread(matchPath, Highgui.CV_LOAD_IMAGE_COLOR);
        Imgproc.matchTemplate(mattmp, src01, imagematch, Imgproc.TM_CCOEFF_NORMED);
        // Core.normalize(imagematch, imagematch, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        MinMaxLocResult minmaxLoc1 = Core.minMaxLoc(imagematch);
        LogUtils.info("minmaxLoc1.maxVal:" + minmaxLoc1.maxVal);
        maxLoc01 = minmaxLoc1.maxLoc;
        anchor01.x = maxLoc01.x;
        anchor01.y = maxLoc01.y;
        Core.circle(colorimage, maxLoc01, 3, new Scalar(0, 0, 255), 3);
        String destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect_c1.png";
        Highgui.imwrite(destPath, colorimage);

        Imgproc.matchTemplate(mattmp, src02, imagematch, Imgproc.TM_CCOEFF_NORMED);
        // Core.normalize(imagematch, imagematch, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        MinMaxLocResult minmaxLoc2 = Core.minMaxLoc(imagematch);
        LogUtils.info("minmaxLoc2.maxVal:" + minmaxLoc2.maxVal);
        maxLoc02 = minmaxLoc2.maxLoc;
        anchor02.x = maxLoc02.x + srcCols / 2;
        anchor02.y = maxLoc02.y;
        Core.circle(colorimage, anchor02, 3, new Scalar(0, 0, 255), 3);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect_c2.png";
        Highgui.imwrite(destPath, colorimage);

        Imgproc.matchTemplate(mattmp, src03, imagematch, Imgproc.TM_CCOEFF_NORMED);
        // Core.normalize(imagematch, imagematch, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        MinMaxLocResult minmaxLoc3 = Core.minMaxLoc(imagematch);
        LogUtils.info("minmaxLoc3.maxVal:" + minmaxLoc3.maxVal);
        maxLoc03 = minmaxLoc3.maxLoc;
        anchor03.x = maxLoc03.x;
        anchor03.y = maxLoc03.y + srcRows / 2;
        Core.circle(colorimage, anchor03, 3, new Scalar(0, 0, 255), 3);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect_c3.png";
        Highgui.imwrite(destPath, colorimage);

        Imgproc.matchTemplate(mattmp, src04, imagematch, Imgproc.TM_CCOEFF_NORMED);
        // Core.normalize(imagematch, imagematch, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        MinMaxLocResult minmaxLoc4 = Core.minMaxLoc(imagematch);
        LogUtils.info("minmaxLoc4.maxVal:" + minmaxLoc4.maxVal);
        maxLoc04 = minmaxLoc4.maxLoc;
        anchor04.x = maxLoc04.x + srcCols / 2;
        anchor04.y = maxLoc04.y + srcRows / 2;
        Core.circle(colorimage, anchor04, 3, new Scalar(0, 0, 255), 3);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect_c4.png";
        Highgui.imwrite(destPath, colorimage);
    }

    /**
     * 获得锚点(定位点) 方法2，霍夫曼圆变换查找定位点
     *
     * @param anchor01
     * @param anchor02
     * @param anchor03
     * @param anchor04 void @Date 2018年2月7日
     */
    public static void fetchAnchorPoints2(
            String sourcePath, Point anchor01, Point anchor02, Point anchor03, Point anchor04) {
        Mat src = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Mat colorimage = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        int srcRows = src.rows();
        int srcCols = src.cols();
        Mat source1 = src.submat(new Rect(0, 0, srcRows / 2, srcRows / 2));
        Mat source2 = src.submat(new Rect(srcCols / 2, 0, srcCols / 2, srcRows / 2));
        Mat source3 = src.submat(new Rect(0, srcRows / 2, srcCols / 2, srcRows / 2));
        Mat source4 = src.submat(new Rect(srcCols / 2, srcRows / 2, srcCols / 2, srcRows / 2));

        Mat src01 = colorimage.submat(new Rect(0, 0, srcRows / 2, srcRows / 2));
        Mat src02 = colorimage.submat(new Rect(srcCols / 2, 0, srcCols / 2, srcRows / 2));
        Mat src03 = colorimage.submat(new Rect(0, srcRows / 2, srcCols / 2, srcRows / 2));
        Mat src04 = colorimage.submat(new Rect(srcCols / 2, srcRows / 2, srcCols / 2, srcRows / 2));

        // Mat rrr = OpenCVUtil.imageBinary(src01);
        Mat circles = new Mat(); // 声明一个向量，保存检测出的圆的圆心坐标和半径
        Imgproc.HoughCircles(source1, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 300 / 8, 200, 90, 10, 50); // 霍夫变换检测圆
        LogUtils.info("图片高 宽：" + src.rows() + "		" + src.cols());
        LogUtils.info(circles.cols());
        int cols = circles.cols();
        if (cols > 0) {
            for (int i = 0; i < circles.cols(); i++) {
                BigDecimal vCircle[] = circles.get(0, i);
                Point center = new Point(vCircle[0], vCircle[1]);
                int radius = (int) Math.round(vCircle[2]);
                Core.circle(src01, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
                Core.circle(src01, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
                anchor01.x = vCircle[0];
                anchor01.y = vCircle[1];
            }
        }
        String destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect_cc1.png";
        Highgui.imwrite(destPath, src01);

        Imgproc.HoughCircles(source2, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 300 / 8, 200, 90, 10, 50); // 霍夫变换检测圆
        LogUtils.info(circles.cols());
        if (circles.cols() > 0) {
            for (int i = 0; i < circles.cols(); i++) {
                BigDecimal vCircle[] = circles.get(0, i);
                Point center = new Point(vCircle[0], vCircle[1]);
                int radius = (int) Math.round(vCircle[2]);
                Core.circle(src02, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
                Core.circle(src02, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
                anchor02.x = vCircle[0] + srcCols / 2;
                anchor02.y = vCircle[1];
            }
        }
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect_cc2.png";
        Highgui.imwrite(destPath, src02);

        Imgproc.HoughCircles(source3, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 300 / 8, 200, 90, 10, 50); // 霍夫变换检测圆
        LogUtils.info(circles.cols());
        if (circles.cols() > 0) {
            for (int i = 0; i < circles.cols(); i++) {
                BigDecimal vCircle[] = circles.get(0, i);
                Point center = new Point(vCircle[0], vCircle[1]);
                int radius = (int) Math.round(vCircle[2]);
                Core.circle(src03, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
                Core.circle(src03, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
                anchor03.x = vCircle[0];
                anchor03.y = vCircle[1] + srcRows / 2;
            }
        }
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect_cc3.png";
        Highgui.imwrite(destPath, src03);

        Imgproc.HoughCircles(source4, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, 300 / 8, 200, 90, 10, 50); // 霍夫变换检测圆
        LogUtils.info(circles.cols());
        if (circles.cols() > 0) {
            for (int i = 0; i < circles.cols(); i++) {
                BigDecimal vCircle[] = circles.get(0, i);
                Point center = new Point(vCircle[0], vCircle[1]);
                int radius = (int) Math.round(vCircle[2]);
                Core.circle(src04, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
                Core.circle(src04, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
                anchor04.x = vCircle[0] + srcCols / 2;
                anchor04.y = vCircle[1] + srcRows / 2;
            }
        }
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "rect_cc4.png";
        Highgui.imwrite(destPath, src04);
    }
}
