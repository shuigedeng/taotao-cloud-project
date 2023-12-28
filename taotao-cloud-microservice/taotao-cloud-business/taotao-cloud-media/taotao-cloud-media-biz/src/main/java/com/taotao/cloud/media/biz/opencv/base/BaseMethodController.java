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

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.taotao.cloud.media.biz.opencv.common.BaseController;
import com.taotao.cloud.media.biz.opencv.common.utils.CommonUtil;
import com.taotao.cloud.media.biz.opencv.common.utils.Constants;
import com.taotao.cloud.media.biz.opencv.common.utils.OpenCVUtil;
import com.taotao.cloud.media.biz.opencv.demo.DemoController;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * BaseMethodController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 16:03:42
 */
@Controller
@RequestMapping(value = "base")
public class BaseMethodController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    /** 二值化方法测试 创建者 Songer 创建时间 2018年3月9日 */
    @RequestMapping(value = "binary")
    public void binary(
            HttpServletResponse response, String imagefile, Integer binaryType, BigDecimal thresh, BigDecimal maxval) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 二值化方法");

        // 灰度化
        // Imgproc.cvtColor(source, destination, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        // 加载为灰度图显示
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        logger.info("binaryType:{},thresh:{},maxval:{}", binaryType, thresh, maxval);
        switch (binaryType) {
            case 0:
                binaryType = Imgproc.THRESH_BINARY;
                break;
            case 1:
                binaryType = Imgproc.THRESH_BINARY_INV;
                break;
            case 2:
                binaryType = Imgproc.THRESH_TRUNC;
                break;
            case 3:
                binaryType = Imgproc.THRESH_TOZERO;
                break;
            case 4:
                binaryType = Imgproc.THRESH_TOZERO_INV;
                break;
            default:
                break;
        }
        Imgproc.threshold(source, destination, BigDecimal.valueOf(thresh), BigDecimal.valueOf(maxval), binaryType);
        //		Imgproc.adaptiveThreshold(source, destination, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
        // Imgproc.THRESH_BINARY_INV, 31, 15);
        //		Imgproc.threshold(source, destination, 170, 255, Imgproc.THRESH_BINARY_INV);
        //		Imgproc.threshold(source, destination, 127, 255, Imgproc.THRESH_TOZERO);
        //		Imgproc.threshold(source, destination, 0, 255, Imgproc.THRESH_TOZERO_INV);

        // String filename = imagefile.substring(imagefile.lastIndexOf("/"), imagefile.length());
        // String filename_end = filename.substring(filename.lastIndexOf("."), filename.length());
        // String filename_pre = filename.substring(0, filename.lastIndexOf("."));
        // LogUtils.info(filename_pre);
        // LogUtils.info(filename_end);
        // filename = filename_pre + "_" + binaryType + "_" + thresh + "_" + maxval + "_" +
        // filename_end;

        // 原方式1生成图片后，页面读取的方式，但是实时性不好改为方式2
        // String destPath = Constants.DIST_IMAGE_PATH + filename;
        // File dstfile = new File(destPath);
        // if (StringUtils.isNotBlank(filename) && dstfile.isFile() && dstfile.exists()) {
        // dstfile.delete();
        // logger.info("删除图片：" + filename);
        // }
        // Highgui.imwrite(destPath, destination);
        // logger.info("生成目标图片==============" + destPath);
        // renderString(response, filename);
        // renderString(response, Constants.SUCCESS);
        // 方式1end//

        // 方式2，回写页面图片流
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 自适用二值化
     *
     * @param response
     * @param imagefile
     * @param binaryType 二值化类型
     * @param blockSize 附近区域面积
     * @param constantC 它只是一个常数，从平均值或加权平均值中减去的常数
     */
    @RequestMapping(value = "adaptiveBinary")
    public void adaptiveBinary(
            HttpServletResponse response,
            String imagefile,
            Integer adaptiveMethod,
            Integer binaryType,
            Integer blockSize,
            BigDecimal constantC) {
        //
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 自适用二值化方法");

        // 灰度化
        // Imgproc.cvtColor(source, destination, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        // 加载为灰度图显示
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        logger.info("binaryType:{},blockSize:{},constantC:{}", binaryType, blockSize, constantC);
        switch (adaptiveMethod) {
            case 0:
                adaptiveMethod = Imgproc.ADAPTIVE_THRESH_MEAN_C;
                break;
            case 1:
                adaptiveMethod = Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
                break;
        }

        switch (binaryType) {
            case 0:
                binaryType = Imgproc.THRESH_BINARY;
                break;
            case 1:
                binaryType = Imgproc.THRESH_BINARY_INV;
                break;
            case 2:
                binaryType = Imgproc.THRESH_TRUNC;
                break;
            case 3:
                binaryType = Imgproc.THRESH_TOZERO;
                break;
            case 4:
                binaryType = Imgproc.THRESH_TOZERO_INV;
                break;
            default:
                break;
        }
        Imgproc.adaptiveThreshold(source, destination, 255, adaptiveMethod, binaryType, blockSize, constantC);

        // 方式2，回写页面图片流
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 自适用二值化+zxing识别条形码
     *
     * @param response
     * @param imagefile
     * @param binaryType 二值化类型
     * @param blockSize 附近区域面积
     * @param constantC 它只是一个常数，从平均值或加权平均值中减去的常数
     */
    @RequestMapping(value = "zxing")
    public void zxing(
            HttpServletResponse response,
            String imagefile,
            Integer adaptiveMethod,
            Integer binaryType,
            Integer blockSize,
            BigDecimal constantC) {
        //
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 自适用二值化方法");

        // 灰度化
        // Imgproc.cvtColor(source, destination, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        // 加载为灰度图显示
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        logger.info("binaryType:{},blockSize:{},constantC:{}", binaryType, blockSize, constantC);
        switch (adaptiveMethod) {
            case 0:
                adaptiveMethod = Imgproc.ADAPTIVE_THRESH_MEAN_C;
                break;
            case 1:
                adaptiveMethod = Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
                break;
        }

        switch (binaryType) {
            case 0:
                binaryType = Imgproc.THRESH_BINARY;
                break;
            case 1:
                binaryType = Imgproc.THRESH_BINARY_INV;
                break;
            case 2:
                binaryType = Imgproc.THRESH_TRUNC;
                break;
            case 3:
                binaryType = Imgproc.THRESH_TOZERO;
                break;
            case 4:
                binaryType = Imgproc.THRESH_TOZERO_INV;
                break;
            default:
                break;
        }
        // Imgproc.adaptiveThreshold(source, destination, 255, adaptiveMethod, binaryType,
        // blockSize, constantC);
        Imgproc.threshold(source, destination, 190, 255, Imgproc.THRESH_BINARY);
        String result = parseCode(destination);

        renderString(response, result);
    }

    private static String parseCode(Mat mat) {
        String resultText = "无法识别！！！";
        try {
            MultiFormatReader formatReader = new MultiFormatReader();
            // if (!file.exists()) {
            // LogUtils.info("nofile");
            // return;
            // }
            // BufferedImage image = ImageIO.read(file);

            BufferedImage image = OpenCVUtil.toBufferedImage(mat);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            Result result = formatReader.decode(binaryBitmap, hints);
            StringBuffer sbuffer = new StringBuffer();
            sbuffer.append("解析结果 = " + result.toString() + "\n");
            sbuffer.append("二维码格式类型 = " + result.getBarcodeFormat() + "\n");
            sbuffer.append("二维码文本内容 = " + result.getText() + "\n");
            resultText = sbuffer.toString();
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return resultText;
    }

    /** 高斯滤波方法测试 创建者 Songer 创建时间 2018年3月9日 */
    @RequestMapping(value = "gaussian")
    public void gaussian(
            HttpServletResponse response,
            String imagefile,
            String kwidth,
            String kheight,
            String sigmaX,
            String sigmaY) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 二值化方法");

        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        logger.info("kwidth:{},kheight:{},sigmaX:{},sigmaY:{}", kwidth, kheight, sigmaX, sigmaY);
        Imgproc.GaussianBlur(
                source,
                destination,
                new Size(2 * Integer.valueOf(kwidth) + 1, 2 * Integer.valueOf(kheight) + 1),
                Integer.valueOf(sigmaX),
                Integer.valueOf(sigmaY));
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 图像锐化操作
     *
     * @param response
     * @param imagefile
     * @param ksize 中值滤波内核size
     * @param alpha 控制图层src1的透明度
     * @param beta 控制图层src2的透明度
     * @param gamma gamma越大合并的影像越明亮 void
     */
    @RequestMapping(value = "sharpness")
    public void sharpness(
            HttpServletResponse response,
            String imagefile,
            int ksize,
            BigDecimal alpha,
            BigDecimal beta,
            BigDecimal gamma) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 锐化操作");

        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        // 先进行中值滤波操作
        Imgproc.medianBlur(source, destination, 2 * ksize + 1);
        // 通过合并图层的方式进行效果增强 alpha控制src1的透明度，beta控制src2 的透明图；gamma越大合并的影像越明亮
        // public static void addWeighted(Mat src1, BigDecimal alpha, Mat src2, BigDecimal beta,
        // BigDecimal gamma, Mat dst)
        Core.addWeighted(source, alpha, destination, beta, gamma, destination);

        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 漫水填充
     *
     * @param response
     * @param imagefile
     */
    @RequestMapping(value = "floodfill")
    public void floodfill(
            HttpServletResponse response,
            String imagefile,
            BigDecimal graysize,
            BigDecimal lodiff,
            BigDecimal updiff,
            int flag) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 漫水填充操作");

        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        // Mat mask = new Mat(source.rows() + 2, source.cols() + 2, source.type());
        Mat mask = new Mat();
        Rect rect = new Rect();
        // 简单调用方式
        //		Imgproc.floodFill(source, mask, new Point(0, 0), new Scalar(graysize));

        // 表示floodFill函数标识符低八位的连通值4、8 0-7位可以设置为4或8
        int g_nConnectivity = 4;
        // 中间八位部分,新的重新绘制的像素值 255，中8为8-15位，当此值不设置或为0时掩码mask会默认设置为1
        int g_nNewMaskVal = 255;
        /**
         * 漫水填充的模式:0 默认方式，既不选FLOODFILL_FIXED_RANGE又不选FLOODFILL_MASK_ONLY
         * FLOODFILL_FIXED_RANGE:每个像素点都将于种子点，而不是相邻点相比较。即设置此值时，
         * 则只有当某个相邻点与种子像素之间的差值在指定范围内才填充，否则考虑当前点与其相邻点的差是否落在制定范围 FLOODFILL_MASK_ONLY
         * 如果设置，函数不填充原始图像，而去填充掩码图像。
         */
        int g_nFillMode = 0;
        if (flag == 0) { // 默认方式
            g_nFillMode = 0;
        } else if (flag == 1) { // FLOODFILL_FIXED_RANGE方式
            g_nFillMode = Imgproc.FLOODFILL_FIXED_RANGE;
        } else { // FLOODFILL_MASK_ONLY方式
            g_nFillMode = Imgproc.FLOODFILL_MASK_ONLY;
            mask = new Mat(source.rows() + 2, source.cols() + 2, source.type()); // 延展图像
        }

        LogUtils.info(g_nNewMaskVal << 8);

        int flags = g_nConnectivity | (g_nNewMaskVal << 8) | g_nFillMode;

        // 使用mask调用方式
        Imgproc.floodFill(
                source,
                mask,
                new Point(0, 0),
                new Scalar(graysize),
                rect,
                new Scalar(lodiff),
                new Scalar(updiff),
                flags);

        try {
            if (flag == 2) { // FLOODFILL_MASK_ONLY方式填充的是掩码图像
                byte[] imgebyte = OpenCVUtil.covertMat2Byte1(mask);
                renderImage(response, imgebyte);
            } else {
                byte[] imgebyte = OpenCVUtil.covertMat2Byte1(source);
                renderImage(response, imgebyte);
            }
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 图片缩放方法测试 创建者 Songer 创建时间 2018年3月15日 */
    @RequestMapping(value = "resize")
    public void resize(
            HttpServletResponse response,
            String imagefile,
            BigDecimal rewidth,
            BigDecimal reheight,
            Integer resizeType) {
        // 默认都是放大
        BigDecimal width = rewidth;
        BigDecimal height = reheight;

        if (resizeType == 2) {
            width = 1 / width;
            height = 1 / height;
        }
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 图片缩放方法测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        logger.info("resizeType:{},rewidth:{},reheight:{}", resizeType, rewidth, reheight);
        Imgproc.resize(source, destination, new Size(0, 0), width, height, 0);
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 腐蚀膨胀测试 创建者 Songer 创建时间 2018年3月15日 */
    @RequestMapping(value = "erodingAndDilation")
    public void erodingAndDilation(
            HttpServletResponse response,
            String imagefile,
            BigDecimal kSize,
            Integer operateType,
            Integer shapeType,
            boolean isBinary) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 腐蚀膨胀测试测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        if (isBinary) {
            source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            // Imgproc.threshold(source, source, 100, 255, Imgproc.THRESH_BINARY);
        }
        BigDecimal size = BigDecimal.valueOf(kSize);
        int shape = 0;
        switch (shapeType) {
            case 0:
                shape = Imgproc.MORPH_RECT;
                break;
            case 1:
                shape = Imgproc.MORPH_CROSS;
                break;
            case 2:
                shape = Imgproc.MORPH_ELLIPSE;
                break;
        }
        Mat element = Imgproc.getStructuringElement(shape, new Size(2 * size + 1, 2 * size + 1));
        logger.info("kSize:{},operateType:{},shapeType:{},isBinary:{}", kSize, operateType, shapeType, isBinary);
        if (operateType == 1) { // 腐蚀
            Imgproc.erode(source, destination, element);
        } else { // 膨胀
            Imgproc.dilate(source, destination, element);
        }
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 腐蚀膨胀使用进阶 更高级的形态学变换处理：morphologyEx 创建者 Songer 创建时间 2018年3月15日 */
    @RequestMapping(value = "morphologyEx")
    public void morphologyEx(
            HttpServletResponse response,
            String imagefile,
            BigDecimal kSize,
            Integer operateType,
            Integer shapeType,
            boolean isBinary) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 腐蚀膨胀测试测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        if (isBinary) {
            source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            // Imgproc.threshold(source, source, 100, 255, Imgproc.THRESH_BINARY);
        }
        BigDecimal size = BigDecimal.valueOf(kSize);
        int shape = 0;
        switch (shapeType) {
            case 0:
                shape = Imgproc.MORPH_RECT;
                break;
            case 1:
                shape = Imgproc.MORPH_CROSS;
                break;
            case 2:
                shape = Imgproc.MORPH_ELLIPSE;
                break;
        }

        int op = 2;
        switch (operateType) { // 主要是为了方便查看参数是哪一个
            case 2:
                op = Imgproc.MORPH_OPEN;
                break;
            case 3:
                op = Imgproc.MORPH_CLOSE;
                break;
            case 4:
                op = Imgproc.MORPH_GRADIENT;
                break;
            case 5:
                op = Imgproc.MORPH_TOPHAT;
                break;
            case 6:
                op = Imgproc.MORPH_BLACKHAT;
                break;
            case 7:
                op = Imgproc.MORPH_HITMISS;
                break;
        }

        Mat element = Imgproc.getStructuringElement(shape, new Size(2 * size + 1, 2 * size + 1));
        logger.info("kSize:{},operateType:{},shapeType:{},isBinary:{}", kSize, operateType, shapeType, isBinary);

        Imgproc.morphologyEx(source, destination, op, element);
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 边缘检测Canny 创建者 Songer 创建时间 2018年3月15日 */
    @RequestMapping(value = "canny")
    public void canny(
            HttpServletResponse response,
            String imagefile,
            BigDecimal threshold1,
            BigDecimal threshold2,
            boolean isBinary) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 边缘检测测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.Canny(source, destination, threshold1, threshold2);
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 霍夫线变换 创建者 Songer 创建时间 2018年3月19日 */
    @RequestMapping(value = "houghline")
    public void houghline(
            HttpServletResponse response,
            String imagefile,
            BigDecimal threshold1,
            BigDecimal threshold2,
            Integer threshold,
            BigDecimal minLineLength,
            BigDecimal maxLineGap) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 霍夫线变换测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source1 = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR); // 彩色图
        Mat source2 = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE); // 灰度图
        Mat lineMat = new Mat(source2.rows(), source2.cols(), source2.type());
        Mat destination = new Mat(source2.rows(), source2.cols(), source2.type());
        Imgproc.Canny(source2, destination, threshold1, threshold2);
        Imgproc.HoughLinesP(destination, lineMat, 1, Math.PI / 180, threshold, minLineLength, maxLineGap);
        int[] a = new int[(int) lineMat.total() * lineMat.channels()]; // 数组a存储检测出的直线端点坐标
        lineMat.get(0, 0, a);
        for (int i = 0; i < a.length; i += 4) {
            // new Scalar(255, 0, 0) blue
            // new Scalar(0, 255, 0) green
            // new Scalar(0, 0, 255) red
            Core.line(source1, new Point(a[i], a[i + 1]), new Point(a[i + 2], a[i + 3]), new Scalar(0, 255, 0), 2);
        }

        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(source1);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 霍夫圆变换 创建者 Songer 创建时间 2018年3月20日 */
    @RequestMapping(value = "houghcircle")
    public void houghcircle(
            HttpServletResponse response,
            String imagefile,
            BigDecimal minDist,
            BigDecimal param1,
            BigDecimal param2,
            Integer minRadius,
            Integer maxRadius) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 霍夫圆变换测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source1 = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR); // 彩色图
        Mat source2 = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE); // 灰度图
        Mat circleMat = new Mat(source2.rows(), source2.cols(), source2.type());

        Imgproc.HoughCircles(
                source2,
                circleMat,
                Imgproc.CV_HOUGH_GRADIENT,
                1.0,
                minDist,
                param1,
                param2,
                minRadius,
                maxRadius); // 霍夫变换检测圆
        LogUtils.info("----------------" + circleMat.cols());
        int cols = circleMat.cols();
        // Point anchor01 = new Point();
        if (cols > 0) {
            for (int i = 0; i < cols; i++) {
                BigDecimal vCircle[] = circleMat.get(0, i);
                Point center = new Point(vCircle[0], vCircle[1]);
                int radius = (int) Math.round(vCircle[2]);
                Core.circle(source1, center, 3, new Scalar(0, 255, 0), -1, 8, 0); // 绿色圆心
                Core.circle(source1, center, radius, new Scalar(0, 0, 255), 3, 8, 0); // 红色圆边
                // anchor01.x = vCircle[0];
                // anchor01.y = vCircle[1];

            }
        }
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(source1);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 颜色识别测试 创建者 Songer 创建时间 2018年3月20日 */
    @RequestMapping(value = "findcolor")
    public void findcolor(HttpServletResponse response, String imagefile, Integer color, Integer colorType) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 查找颜色测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        if (colorType == 1) { // 1为RGB方式，2为HSV方式
            BigDecimal B = 0;
            BigDecimal G = 0;
            BigDecimal R = 0;
            switch (color) {
                case 1: // red
                    B = 0;
                    G = 0;
                    R = 255;
                    break;
                case 2: // blue
                    B = 255;
                    G = 0;
                    R = 0;
                    break;
                case 3: // green
                    B = 0;
                    G = 255;
                    R = 0;
                    break;
                case 4: // yellow
                    B = 0;
                    G = 255;
                    R = 255;
                    break;
            }
            Core.inRange(source, new Scalar(B, G, R), new Scalar(B, G, R), destination);
        } else { // HSV方式
            Imgproc.cvtColor(source, source, Imgproc.COLOR_BGR2HSV);
            BigDecimal min = 0;
            BigDecimal max = 0;
            // 泛红色系(176,90,90)-(0, 90, 90)-(20,255,255) 简易：0-20
            // 泛蓝色系(100, 90, 90)-(120,255,255)
            // 泛绿色系(60, 90, 90)-(80,255,255)
            // 泛黄色系(23, 90, 90)-(38,255,255)
            switch (color) {
                case 1: // red
                    min = 0;
                    max = 20;
                    break;
                case 2: // blue
                    min = 100;
                    max = 120;
                    break;
                case 3: // green
                    min = 60;
                    max = 80;
                    break;
                case 4: // yellow
                    min = 23;
                    max = 38;
                    break;
            }
            Core.inRange(source, new Scalar(min, 90, 90), new Scalar(max, 255, 255), destination);
        }
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 轮廓识别测试 创建者 Songer 创建时间 2018年3月20日 */
    @RequestMapping(value = "contours")
    public void contours(
            HttpServletResponse response, String imagefile, Integer mode, Integer method, Integer contourNum) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 轮廓识别测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        logger.info("mode:{},method:{}", mode, method);

        switch (mode) {
            case 0:
                mode = Imgproc.RETR_EXTERNAL;
                break;
            case 1:
                mode = Imgproc.RETR_LIST;
                break;
            case 2:
                mode = Imgproc.RETR_CCOMP;
                break;
            case 3:
                mode = Imgproc.RETR_TREE;
                break;
        }
        switch (method) {
            case 0:
                method = Imgproc.CV_CHAIN_CODE;
                break;
            case 1:
                method = Imgproc.CHAIN_APPROX_NONE;
                break;
            case 2:
                method = Imgproc.CHAIN_APPROX_SIMPLE;
                break;
            case 3:
                method = Imgproc.CHAIN_APPROX_TC89_L1;
                break;
            case 4:
                method = Imgproc.CHAIN_APPROX_TC89_KCOS;
                break;
            case 5:
                method = Imgproc.CV_LINK_RUNS;
                break;
        }

        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        // Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Mat destination = Mat.zeros(source.size(), CvType.CV_8UC3);
        Mat hierarchy = new Mat(source.rows(), source.cols(), CvType.CV_8UC1, new Scalar(0));
        Vector<MatOfPoint> contours = new Vector<MatOfPoint>();
        Imgproc.findContours(source, contours, hierarchy, mode, method, new Point());
        LogUtils.info(contours.size());
        logger.info("轮廓数量为：{}，当前请求要展现第{}个轮廓", contours.size(), contourNum);
        // contourNum因为轮廓计数是从0开始
        if (contourNum == -1 || (contourNum + 1) > contours.size()) {
            logger.info("轮廓数量已经超出，默认显示所有轮廓，轮廓数量：{}", contours.size());
            contourNum = -1;
        }
        Imgproc.drawContours(destination, contours, contourNum, new Scalar(0, 255, 0), 1);
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(destination);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /** 模板查找测试 创建者 Songer 创建时间 2018年3月21日 */
    @RequestMapping(value = "findtemplate")
    public void findtemplate(
            HttpServletResponse response,
            String imagefile,
            Integer method,
            Integer imageType,
            BigDecimal x1,
            BigDecimal y1,
            BigDecimal x2,
            BigDecimal y2,
            BigDecimal width,
            BigDecimal height) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 模板查找测试");
        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        // Mat destination = new Mat(source.rows(), source.cols(), source.type());
        // String templateimage = Constants.SOURCE_IMAGE_PATH + "/template.png";
        // LogUtils.info(templateimage);
        // Mat matchtemp = Highgui.imread(templateimage);
        // 优化代码，模板图像直接通过前端截取或取得，而不是写死，此处用到了OpenCV的截取图像功能
        logger.info("{},{},{},{}", x1, y1, width, height);
        Mat matchtemp = source.submat(new Rect(
                Integer.valueOf(CommonUtil.setScare(x1.toString(), 0)),
                Integer.valueOf(CommonUtil.setScare(y1.toString(), 0)),
                Integer.valueOf(CommonUtil.setScare(width.toString(), 0)),
                Integer.valueOf(CommonUtil.setScare(height.toString(), 0))));

        int result_cols = source.cols() - matchtemp.cols() + 1;
        int result_rows = source.rows() - matchtemp.rows() + 1;
        Mat destination = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(source, matchtemp, destination, method);
        // 矩阵归一化处理
        Core.normalize(destination, destination, 0, 255, Core.NORM_MINMAX, -1, new Mat());
        // minMaxLoc(imagematch, minVal, maxVal2, minLoc, maxLoc01, new Mat());
        MinMaxLocResult minmaxLoc = Core.minMaxLoc(destination);
        logger.info("相似值=================：最大：" + minmaxLoc.maxVal + "    最小：" + minmaxLoc.minVal);
        Point matchLoc = new Point();
        switch (method) {
            case 0:
                // method = Imgproc.TM_SQDIFF;
                matchLoc = minmaxLoc.minLoc;
                break;
            case 1:
                // method = Imgproc.TM_SQDIFF_NORMED;
                matchLoc = minmaxLoc.minLoc;
                break;
            case 2:
                // method = Imgproc.TM_CCORR;
                matchLoc = minmaxLoc.maxLoc;
                break;
            case 3:
                // method = Imgproc.TM_CCORR_NORMED;
                matchLoc = minmaxLoc.maxLoc;
                break;
            case 4:
                // method = Imgproc.TM_CCOEFF;
                matchLoc = minmaxLoc.maxLoc;
                break;
            case 5:
                // method = Imgproc.TM_CCOEFF_NORMED;
                matchLoc = minmaxLoc.maxLoc;
                break;
            default:
                // method = Imgproc.TM_SQDIFF;
                matchLoc = minmaxLoc.minLoc;
                break;
        }

        if (imageType == 0) { // 显示过程图片
            source = destination;
        } else { // 显示最终框选结果
            Core.rectangle(
                    source,
                    matchLoc,
                    new Point(matchLoc.x + matchtemp.cols(), matchLoc.y + matchtemp.rows()),
                    new Scalar(0, 255, 0),
                    2);
        }
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(source);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 灰度直方图
     *
     * @param response
     * @param imagefile
     * @param cols
     * @return Mat
     */
    @RequestMapping(value = "grayHistogram")
    public void grayHistogram(
            HttpServletResponse response,
            String imagefile,
            Integer cols,
            Integer imageW,
            Integer imageH,
            Integer imageKedu,
            boolean isShow) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 灰度直方图测试");
        String sourcePath = Constants.PATH + imagefile;
        Mat source = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        List<Mat> images = new ArrayList<Mat>();
        images.add(source);
        MatOfInt channels = new MatOfInt(0); // 图像通道数，0表示只有一个通道
        MatOfInt histSize = new MatOfInt(cols); // CV_8U类型的图片范围是0~255，共有256个灰度级
        Mat histogramOfGray = new Mat(); // 输出直方图结果，共有256行，行数的相当于对应灰度值，每一行的值相当于该灰度值所占比例
        MatOfFloat histRange = new MatOfFloat(0, 255);
        Imgproc.calcHist(images, channels, new Mat(), histogramOfGray, histSize, histRange, false); // 计算直方图
        MinMaxLocResult minmaxLoc = Core.minMaxLoc(histogramOfGray);
        // 按行归一化
        // Core.normalize(histogramOfGray, histogramOfGray, 0, histogramOfGray.rows(),
        // Core.NORM_MINMAX, -1, new Mat());

        // 创建画布
        int histImgRows = imageH;
        int histImgCols = imageW;
        int colStep = (int) Math.floor((histImgCols) / histSize.get(0, 0)[0]);
        Mat histImg = new Mat(histImgRows, histImgCols, CvType.CV_8UC3, new Scalar(255, 255, 255)); // 重新建一张图片，绘制直方图

        int max = (int) minmaxLoc.maxVal;
        LogUtils.info("max--------" + max);
        BigDecimal bin_u = (BigDecimal) (histImg.height() - 20) / max; // max: 最高条的像素个数，则 bin_u 为单个像素的高度
        int kedu = 0;
        for (int i = 1; kedu <= minmaxLoc.maxVal; i++) {
            kedu = i * max / 10;
            // 在图像中显示文本字符串
            Core.putText(
                    histImg, kedu + "", new Point(0, histImg.height() - 5 - kedu * bin_u), 1, 1, new Scalar(255, 0, 0));
            if (isShow) {
                // 附上高度坐标线，因为高度在画图时-了20，此处也减掉
                Core.line(
                        histImg,
                        new Point(0, histImg.height() - 20 - kedu * bin_u),
                        new Point(imageW, histImg.height() - 20 - (kedu + 1) * bin_u),
                        new Scalar(255, 0, 0),
                        1,
                        8,
                        0);
            }
        }

        LogUtils.info("灰度级:" + histSize.get(0, 0)[0]);
        for (int i = 0; i < histSize.get(0, 0)[0]; i++) { // 画出每一个灰度级分量的比例，注意OpenCV将Mat最左上角的点作为坐标原点
            Core.rectangle(
                    histImg,
                    new Point(colStep * i, histImgRows - 20),
                    new Point(
                            colStep * (i + 1),
                            histImgRows - bin_u * Math.round(histogramOfGray.get(i, 0)[0]) - 20),
                    new Scalar(0, 0, 0),
                    1,
                    8,
                    0);
            // if (i % 10 == 0) {
            // Core.putText(histImg, Integer.toString(i), new Point(colStep * i, histImgRows - 5),
            // 1, 1, new Scalar(255,
            // 0, 0)); // 附上x轴刻度
            // }
            // 每隔10画一下刻度,方式2
            kedu = i * imageKedu;
            Core.rectangle(
                    histImg,
                    new Point(colStep * kedu, histImgRows - 20),
                    new Point(colStep * (kedu + 1), histImgRows - 20),
                    new Scalar(255, 0, 0),
                    2,
                    8,
                    0);
            Core.putText(
                    histImg,
                    kedu + "",
                    new Point(histImgCols / 256 * kedu, histImgRows - 5),
                    1,
                    1,
                    new Scalar(255, 0, 0)); // 附上x轴刻度
        }
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(histImg);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    // public void qrCode(HttpServletResponse response, String imagefile, Integer binaryType,
    // BigDecimal thresh, BigDecimal maxval) {
    // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    // String sourcePath = Constants.PATH + imagefile;
    // // 加载为灰度图显示
    // Mat imageGray = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    // Mat image = new Mat(imageGray.rows(), imageGray.cols(), imageGray.type());
    // Mat imageGuussian = new Mat(imageGray.rows(), imageGray.cols(), imageGray.type());
    // Mat imageSobelX,imageSobelY,imageSobelOut;
    // imageGray.copyTo(image);
    //
    // // imshow("Source Image",image);
    //
    // GaussianBlur(imageGray,imageGuussian,Size(3,3),0);
    // Imgproc.GaussianBlur(imageGray, imageGuussian,new Size(5, 5),
    // Integer.valueOf(sigmaX), Integer.valueOf(sigmaY));
    //
    // //水平和垂直方向灰度图像的梯度和,使用Sobel算子
    // Mat imageX16S,imageY16S;
    // Sobel(imageGuussian,imageX16S,CV_16S,1,0,3,1,0,4);
    // Sobel(imageGuussian,imageY16S,CV_16S,0,1,3,1,0,4);
    // convertScaleAbs(imageX16S,imageSobelX,1,0);
    // convertScaleAbs(imageY16S,imageSobelY,1,0);
    // imageSobelOut=imageSobelX+imageSobelY;
    // imshow("XY方向梯度和",imageSobelOut);
    // Mat srcImg =imageSobelOut;
    // //宽高扩充，非必须，特定的宽高可以提高傅里叶运算效率
    // Mat padded;
    // int opWidth = getOptimalDFTSize(srcImg.rows);
    // int opHeight = getOptimalDFTSize(srcImg.cols);
    // copyMakeBorder(srcImg, padded, 0, opWidth-srcImg.rows, 0, opHeight-srcImg.cols,
    // BORDER_CONSTANT, Scalar::all(0));
    // Mat planes[] = {Mat_<float>(padded), Mat::zeros(padded.size(), CV_32F)};
    // Mat comImg;
    // //通道融合，融合成一个2通道的图像
    // merge(planes,2,comImg);
    // dft(comImg, comImg);
    // split(comImg, planes);
    // magnitude(planes[0], planes[1], planes[0]);
    // Mat magMat = planes[0];
    // magMat += Scalar::all(1);
    // log(magMat, magMat); //对数变换，方便显示
    // magMat = magMat(Rect(0, 0, magMat.cols & -2, magMat.rows & -2));
    // //以下把傅里叶频谱图的四个角落移动到图像中心
    // int cx = magMat.cols/2;
    // int cy = magMat.rows/2;
    // Mat q0(magMat, Rect(0, 0, cx, cy));
    // Mat q1(magMat, Rect(0, cy, cx, cy));
    // Mat q2(magMat, Rect(cx, cy, cx, cy));
    // Mat q3(magMat, Rect(cx, 0, cx, cy));
    // Mat tmp;
    // q0.copyTo(tmp);
    // q2.copyTo(q0);
    // tmp.copyTo(q2);
    // q1.copyTo(tmp);
    // q3.copyTo(q1);
    // tmp.copyTo(q3);
    // normalize(magMat, magMat, 0, 1, CV_MINMAX);
    // Mat magImg(magMat.size(), CV_8UC1);
    // magMat.convertTo(magImg,CV_8UC1,255,0);
    // imshow("傅里叶频谱", magImg);
    // //HoughLines查找傅里叶频谱的直线，该直线跟原图的一维码方向相互垂直
    // threshold(magImg,magImg,180,255,CV_THRESH_BINARY);
    // imshow("二值化", magImg);
    // vector<Vec2f> lines;
    // float pi180 = (float)CV_PI/180;
    // Mat linImg(magImg.size(),CV_8UC3);
    // HoughLines(magImg,lines,1,pi180,100,0,0);
    // int numLines = lines.size();
    // float theta;
    // for(int l=0; l<numLines; l++)
    // {
    // float rho = lines[l][0];
    // theta = lines[l][1];
    // float aa=(theta/CV_PI)*180;
    // Point pt1, pt2;
    // BigDecimal a = cos(theta), b = sin(theta);
    // BigDecimal x0 = a*rho, y0 = b*rho;
    // pt1.x = cvRound(x0 + 1000*(-b));
    // pt1.y = cvRound(y0 + 1000*(a));
    // pt2.x = cvRound(x0 - 1000*(-b));
    // pt2.y = cvRound(y0 - 1000*(a));
    // line(linImg,pt1,pt2,Scalar(255,0,0),3,8,0);
    // }
    // imshow("Hough直线",linImg);
    // //校正角度计算
    // float angelD=180*theta/CV_PI-90;
    // Point center(image.cols/2, image.rows/2);
    // Mat rotMat = getRotationMatrix2D(center,angelD,1.0);
    // Mat imageSource = Mat::ones(image.size(),CV_8UC3);
    // warpAffine(image,imageSource,rotMat,image.size(),1,0,Scalar(255,255,255));//仿射变换校正图像
    // imshow("角度校正",imageSource);
    // //Zbar一维码识别
    // ImageScanner scanner;
    // scanner.set_config(ZBAR_NONE, ZBAR_CFG_ENABLE, 1);
    // int width1 = imageSource.cols;
    // int height1 = imageSource.rows;
    // uchar *raw = (uchar *)imageSource.data;
    // Image imageZbar(width1, height1, "Y800", raw, width1 * height1);
    // scanner.scan(imageZbar); //扫描条码
    // Image::SymbolIterator symbol = imageZbar.symbol_begin();
    // if(imageZbar.symbol_begin()==imageZbar.symbol_end())
    // {
    // cout<<"查询条码失败，请检查图片！"<<endl;
    // }
    // for(;symbol != imageZbar.symbol_end();++symbol)
    // {
    // cout<<"类型："<<endl<<symbol->get_type_name()<<endl<<endl;
    // cout<<"条码："<<endl<<symbol->get_data()<<endl<<endl;
    // }
    // namedWindow("Source Window",0);
    // imshow("Source Window",imageSource);
    // waitKey();
    // imageZbar.set_data(NULL,0);
    // return 0;
    //
    // }

    // public static void main(String[] args) {
    //	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    //
    // }

    @RequestMapping(value = "picTransform")
    public void picTransform(HttpServletResponse response, String imagefile) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 图像转换开始");
        // 我们假设我们识别的图片如样例一样有明显的边界，那我们可以用边缘检测算法将真正有效区域抽离出来，
        // 以此来提高识别准确度和识别精度
        // 先进行边缘检测
        //		String sourcePath = "d:\\test\\abc\\a.png";
        String sourcePath = Constants.PATH + imagefile;
        Mat source = Highgui.imread(sourcePath);
        //		Mat destination = new Mat(source.rows(), source.cols(), source.type());
        // 复制一个source作为四点转换的原图，因为source在轮廓识别时会被覆盖，建议图像处理时都将原图复制一份，
        // 因为opencv的很多算法都会更改传入的soure图片，如果不注意可能就会导致各种异常。
        Mat orign = source.clone();
        // 为了加速图像处理，以及使我们的边缘检测步骤更加准确，我们将扫描图像的大小调整为具有500像素的高度。
        Mat dst = source.clone();
        // 缩放比例
        BigDecimal ratio = NumberUtil.div(500, orign.height());
        LogUtils.info("----------" + ratio);
        BigDecimal width = ratio * orign.width();
        Imgproc.resize(source, dst, new Size(width, 500));
        // 灰度化,加载为灰度图显示
        Mat gray = dst.clone();
        Imgproc.cvtColor(dst, gray, Imgproc.COLOR_BGR2GRAY);
        Highgui.imwrite("d:\\test\\abc\\o1.png", gray);
        // 高斯滤波,去除杂点等干扰
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
        // canny边缘检测算法，经过canny算法或的图像会变成二值化效果
        Mat edges = gray.clone();
        Imgproc.Canny(gray, edges, 75, 200);
        Highgui.imwrite("d:\\test\\abc\\o2.png", edges);

        String destPath = "d:\\test\\abc\\dst.png";
        Mat hierarchy = new Mat(gray.rows(), gray.cols(), CvType.CV_8UC1, new Scalar(0));
        Vector<MatOfPoint> contours = new Vector<MatOfPoint>();
        // 轮廓识别，查找外轮廓
        Imgproc.findContours(
                edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point());
        List<Point> listPoint = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint2f newPoint = new MatOfPoint2f(contours.get(i).toArray());
            // 周长，第1个参数是轮廓，第二个参数代表是否是闭环的图形
            BigDecimal peri = 0.01 * Imgproc.arcLength(newPoint, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            //			approx.convertTo(approx, CvType.CV_32F);
            // 近似轮廓逼近，然后通过获取多边形的所有定点，如果是四个定点，就代表是矩形
            Imgproc.approxPolyDP(newPoint, approx, peri, true);
            // 只考虑矩形,如果近似轮廓有4个点，我们就认为已经找到了该矩形。
            if (approx.rows() == 4) {
                // 通过reshape函数将4个点取出来（4行2列的矩阵）
                Mat points = approx.reshape(2, 4);
                LogUtils.info(points.dump());
                BigDecimal[] point1 = points.get(0, 0);
                BigDecimal[] point2 = points.get(1, 0);
                BigDecimal[] point3 = points.get(2, 0);
                BigDecimal[] point4 = points.get(3, 0);
                // 之前因为我们已经将图片进行了缩放，所以此处要将图片尺寸还原
                listPoint.add(new Point(point1[0] / ratio, point1[1] / ratio));
                listPoint.add(new Point(point2[0] / ratio, point2[1] / ratio));
                listPoint.add(new Point(point3[0] / ratio, point3[1] / ratio));
                listPoint.add(new Point(point4[0] / ratio, point4[1] / ratio));
                for (Point d : listPoint) {
                    LogUtils.info(d);
                }
                LogUtils.info("######################");
                break;
            }
        }
        // 绘制轮廓，注意是在缩放过的图片上绘制的，别在原图上画，肯定画的不对。
        Imgproc.drawContours(dst, contours, -1, new Scalar(0, 255, 0), 2);
        Highgui.imwrite("d:\\test\\abc\\o3.png", dst);
        Mat resullt = fourPointTransform(orign, listPoint);
        Highgui.imwrite(destPath, resullt);
        // 方式2，回写页面图片流
        try {
            byte[] imgebyte = OpenCVUtil.covertMat2Byte1(resullt);
            renderImage(response, imgebyte);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * py中imutils中经典的4点转换方法的java实现
     *
     * @param source
     * @param listPoint
     * @return Mat
     *     <p>
     */
    private static Mat fourPointTransform(Mat source, List<Point> listPoint) {
        // 获得点的顺序
        List<Point> newOrderList = orderPoints(listPoint);
        for (Point point : newOrderList) {
            LogUtils.info(point);
        }
        // 计算新图像的宽度，它将是右下角和左下角x坐标之间或右上角和左上角x坐标之间的最大距离
        // 此处的顺序别搞错0,1,2,3依次是左上[0]，右上[1]，右下[2]，左下[3]
        Point leftTop = newOrderList.get(0);
        Point rightTop = newOrderList.get(1);
        Point rightBottom = newOrderList.get(2);
        Point leftBottom = newOrderList.get(3);
        BigDecimal widthA =
                Math.sqrt(Math.pow(rightBottom.x - leftBottom.x, 2) + Math.pow(rightBottom.y - leftBottom.y, 2));
        BigDecimal widthB = Math.sqrt(Math.pow(rightTop.x - leftTop.x, 2) + Math.pow(rightTop.y - leftTop.y, 2));
        int maxWidth = Math.max((int) widthA, (int) widthB);

        // 计算新图像的高度，这将是右上角和右下角y坐标或左上角和左下角y坐标之间的最大距离，
        // 这里用到的初中数学知识点和点的距离计算(x1,y1),(x2,y2)距离=√((x2-x1)^2+(y2-y1)^2)
        BigDecimal heightA =
                Math.sqrt(Math.pow(rightTop.x - rightBottom.x, 2) + Math.pow(rightTop.y - rightBottom.y, 2));
        BigDecimal heightB = Math.sqrt(Math.pow(leftTop.x - leftBottom.x, 2) + Math.pow(leftTop.y - leftBottom.y, 2));
        int maxHeight = Math.max((int) heightA, (int) heightB);
        LogUtils.info("宽度：" + maxWidth);
        LogUtils.info("高度：" + maxHeight);
        // 现在我们指定目标图像的尺寸，构造目标点集以获得图像的“鸟瞰图”（即自上而下的视图），
        // 再次指定左上角，右上角的点，右下角和左下角的顺序
        Point dstPoint1 = new Point(0, 0);
        Point dstPoint2 = new Point(maxWidth - 1, 0);
        Point dstPoint3 = new Point(maxWidth - 1, maxHeight - 1);
        Point dstPoint4 = new Point(0, maxHeight - 1);

        // 计算透视变换矩阵rectMat原四顶点位置，dstMat目标顶点位置
        MatOfPoint2f rectMat = new MatOfPoint2f(leftTop, rightTop, rightBottom, leftBottom);
        MatOfPoint2f dstMat = new MatOfPoint2f(dstPoint1, dstPoint2, dstPoint3, dstPoint4);

        // opencv透视转换方法
        Mat transmtx = Imgproc.getPerspectiveTransform(rectMat, dstMat);
        // 注意定义的新图像宽高设置
        Mat resultMat = Mat.zeros((int) maxHeight - 1, (int) maxWidth - 1, CvType.CV_8UC3);
        Imgproc.warpPerspective(source, resultMat, transmtx, resultMat.size());
        Highgui.imwrite("D:\\test\\abc\\t2.png", resultMat);

        // 返回矫正后的图像
        return resultMat;
    }

    /**
     * 4点排序，四个点按照左上、右上、右下、左下组织返回
     *
     * @param listPoint
     * @return List<Point>
     *     <p>
     */
    private static List<Point> orderPoints(List<Point> listPoint) {
        // python中有很多关于数组的函数处理如排序、比较、加减乘除等，在这里我们使用List进行操作
        // 如numpy.argsort;numpy.argmin;numpy.argmax;sum(axis = 1);diff(pts, axis = 1)等等，有兴趣的可以查阅相关资料
        // 四个点按照左上、右上、右下、左下组织返回
        // 直接在这里添加我们的排序规则,按照x坐标轴升序排列，小的放前面
        Collections.sort(listPoint, new Comparator<Point>() {
            @Override
            public int compare(Point arg0, Point arg1) {
                if (arg0.x < arg1.x) {
                    return -1;
                } else if (arg0.x > arg1.x) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        // 排序之后前2个点就是左侧的点，后2个点为右侧的点
        // 对比Y轴，y值小的是左上的点，y大的是左下的点
        Point top_left = new Point();
        Point bottom_left = new Point();
        Point top_right = new Point();
        Point bottom_right = new Point();

        Point leftPoint1 = listPoint.get(0);
        Point leftPoint2 = listPoint.get(1);
        Point rightPoint1 = listPoint.get(2);
        Point rightPoint2 = listPoint.get(3);
        if (leftPoint1.y > leftPoint2.y) {
            top_left = leftPoint2;
            bottom_left = leftPoint1;
        } else {
            top_left = leftPoint1;
            bottom_left = leftPoint2;
        }
        // 定位右侧的2个点右上和右下使用方法是毕达哥拉斯定理，就是勾股定理距离长的认为是右下角
        // 计算左上方点和右侧两个点的欧氏距离
        // (y2-y1)^2+(x2-x1)^2 开根号
        BigDecimal rightLength1 =
                Math.sqrt(Math.pow((rightPoint1.y - top_left.y), 2) + Math.pow((rightPoint1.x - top_left.x), 2));
        BigDecimal rightLength2 =
                Math.sqrt(Math.pow((rightPoint2.y - top_left.y), 2) + Math.pow((rightPoint2.x - top_left.x), 2));
        if (rightLength1 > rightLength2) {
            // 长度长的那个是右下角,短的为右上角；这个算法有一种情况会有可能出问题，比如倒梯形，但是在正常的俯角拍摄时不会出现这种情况
            // 还有一种方案是按照左侧的那种对比方案，根据y轴的高度判断。
            top_right = rightPoint2;
            bottom_right = rightPoint1;
        } else {
            top_right = rightPoint1;
            bottom_right = rightPoint2;
        }
        // 按照左上，右上，右下，左下的顺时针顺序排列，这点很重要，透视变换时根据这个顺序进行对应
        List<Point> newListPoint = new ArrayList<>();
        newListPoint.add(top_left);
        newListPoint.add(top_right);
        newListPoint.add(bottom_right);
        newListPoint.add(bottom_left);

        return newListPoint;
    }
}
