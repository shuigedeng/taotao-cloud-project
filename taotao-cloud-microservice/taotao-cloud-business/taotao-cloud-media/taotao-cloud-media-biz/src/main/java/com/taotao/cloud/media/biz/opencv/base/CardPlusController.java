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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CardPlusController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 16:04:51
 */
@Controller
@RequestMapping(value = "cardPlus")
public class CardPlusController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CardPlusController.class);

    /** 答题卡识别优化 创建者 Songer 创建时间 2018年3月23日 */
    @RequestMapping(value = "answerSheet")
    public void answerSheet(
            HttpServletResponse response, String imagefile, Integer binary_thresh, String blue_red_thresh) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.info("\n 完整答题卡识别");

        String sourcePath = Constants.PATH + imagefile;
        logger.info("url==============" + sourcePath);
        Mat sourceMat = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_COLOR);
        long t1 = new Date().getTime();
        String destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk0.png";
        Highgui.imwrite(destPath, sourceMat);
        logger.info("原答题卡图片======" + destPath);
        // 初始图片灰度图
        Mat sourceMat1 = Highgui.imread(sourcePath, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk1.png";
        Highgui.imwrite(destPath, sourceMat1);
        logger.info("生成灰度图======" + destPath);
        // 先膨胀 后腐蚀算法，开运算消除细小杂点
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * 1 + 1, 2 * 1 + 1));
        Imgproc.morphologyEx(sourceMat1, sourceMat1, Imgproc.MORPH_OPEN, element);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk2.png";
        Highgui.imwrite(destPath, sourceMat1);
        logger.info("生成膨胀腐蚀后的图======" + destPath);

        // 切割右侧和底部标记位图片
        Mat rightMark = new Mat(sourceMat1, new Rect(sourceMat1.cols() - 100, 0, 100, sourceMat1.rows()));

        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk3.png";
        Highgui.imwrite(destPath, rightMark);
        logger.info("截取右侧定位点图======" + destPath);
        // 平滑处理消除噪点毛刺等等
        Imgproc.GaussianBlur(rightMark, rightMark, new Size(3, 3), 0);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk4.png";
        Highgui.imwrite(destPath, rightMark);
        logger.info("平滑处理后的右侧定位点图======" + destPath);

        // 根据右侧定位获取水平投影，并获取纵向坐标
        Mat matright = horizontalProjection(rightMark);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk5.png";
        Highgui.imwrite(destPath, matright);
        logger.info("右侧水平投影图======" + destPath);
        // 获取y坐标点，返回的是横向条状图集合
        List<Rect> listy = getBlockRect(matright, 1, 0);

        Mat footMark = new Mat(sourceMat1, new Rect(0, sourceMat1.rows() - 150, sourceMat1.cols(), 50));
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk6.png";
        Highgui.imwrite(destPath, footMark);
        logger.info("截取底部定位点图======" + destPath);

        Imgproc.GaussianBlur(footMark, footMark, new Size(3, 3), 0);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk7.png";
        Highgui.imwrite(destPath, footMark);
        logger.info("平滑处理后的底部定位点图======" + destPath);

        // 根据底部定位获取垂直投影，并获取横向坐标
        Mat matbootom = verticalProjection(footMark);

        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk8.png";
        Highgui.imwrite(destPath, matbootom);
        logger.info("底部垂直投影图======" + destPath);
        // 获取x坐标点，返回的是竖向的柱状图集合
        List<Rect> listx = getBlockRect(matbootom, 0, 0);

        // 高阶处理：增加HSV颜色查找，查找红色像素点
        Mat matRed = findColorbyHSV(sourceMat, 156, 180);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk9.png";
        Highgui.imwrite(destPath, matRed);
        logger.info("HSV找出红色像素点======" + destPath);

        Mat dstNoRed = new Mat(sourceMat1.rows(), sourceMat1.cols(), sourceMat1.type());
        dstNoRed = OpenCVUtil.dilation(sourceMat1);
        // Imgproc.threshold(sourceMat1, dstNoRed, 190, 255, Imgproc.THRESH_BINARY);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk10.png";
        Highgui.imwrite(destPath, dstNoRed);
        logger.info("原灰度图的图片======" + destPath);

        Photo.inpaint(dstNoRed, matRed, dstNoRed, 1, Photo.INPAINT_NS);
        // findBlackColorbyHSV(sourceMat);
        // for (int i = 0;i<dstNoRed.rows();i++) {
        // for (int j = 0; j < dstNoRed.cols(); j++) {
        // if(matRed.get(i, j)[0]==255){//代表识别出的红色区域
        // dstNoRed.put(i,j,255);
        // }
        // }
        // }

        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk11.png";
        Highgui.imwrite(destPath, dstNoRed);
        logger.info("去除红颜色后的图片======" + destPath);

        Mat grayHistogram1 = getGrayHistogram(dstNoRed);
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk12.png";
        Highgui.imwrite(destPath, grayHistogram1);
        logger.info("灰度直方图图片1======" + destPath);

        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk13.png";
        Mat answerMat = dstNoRed.submat(new Rect(41, 895, 278, 133));
        Mat grayHistogram2 = getGrayHistogram(answerMat);
        Highgui.imwrite(destPath, grayHistogram2);
        logger.info("灰度直方图图片2======" + destPath);

        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk14.png";
        Imgproc.threshold(dstNoRed, dstNoRed, binary_thresh, 255, Imgproc.THRESH_BINARY_INV);
        Highgui.imwrite(destPath, dstNoRed);
        logger.info("去除红色基础上进行二值化======" + destPath);
        String redvalue = StringUtils.split(blue_red_thresh, ",")[0];
        String bluevalue = StringUtils.split(blue_red_thresh, ",")[1];
        LogUtils.info(bluevalue + "			" + redvalue);
        TreeMap<Integer, String> resultMap = new TreeMap<Integer, String>();
        StringBuffer resultValue = new StringBuffer();
        for (int no = 0; no < listx.size(); no++) {
            Rect rectx = listx.get(no);
            for (int an = 0; an < listy.size(); an++) {
                Rect recty = listy.get(an);
                Mat selectdst = new Mat(
                        dstNoRed,
                        new Range(recty.y, recty.y + recty.height),
                        new Range(rectx.x, rectx.x + rectx.width));
                // 本来是在每个区域内进行二值化，后来挪至了14步，整体进行二值化，因此注释掉此处2行
                // Mat selectdst = new Mat(select.rows(), select.cols(), select.type());
                // Imgproc.threshold(select, selectdst, 170, 255, Imgproc.THRESH_BINARY);

                // LogUtils.info("rectx.x,
                // recty.y=="+rectx.x+","+recty.y+"rectx.width,recty.height=="+rectx.width+","+recty.height);
                BigDecimal p100 =
                        Core.countNonZero(selectdst) * 100 / (selectdst.size().area());
                String que_answer = getQA(no, an);
                Integer que = Integer.valueOf(que_answer.split("_")[0]);
                String answer = que_answer.split("_")[1];
                // LogUtils.info(Core.countNonZero(selectdst) + "/" + selectdst.size().area());
                LogUtils.info(que_answer + ":			" + p100);

                if (p100 >= Integer.valueOf(bluevalue)) { // 蓝色
                    Core.rectangle(
                            sourceMat,
                            new Point(rectx.x, recty.y),
                            new Point(rectx.x + rectx.width, recty.y + recty.height),
                            new Scalar(255, 0, 0),
                            2);
                    // logger.info(que_answer + ":填涂");
                    if (StringUtils.isNotEmpty(resultMap.get(que))) {
                        resultMap.put(que, resultMap.get(que) + "," + answer);
                    } else {
                        resultMap.put(que, answer);
                    }
                } else if (p100 > Integer.valueOf(redvalue) && p100 < Integer.valueOf(bluevalue)) { // 红色
                    Core.rectangle(
                            sourceMat,
                            new Point(rectx.x, recty.y),
                            new Point(rectx.x + rectx.width, recty.y + recty.height),
                            new Scalar(0, 0, 255),
                            2);
                    // logger.info(que_answer + ":临界");
                    if (StringUtils.isNotEmpty(resultMap.get(que))) {
                        resultMap.put(que, resultMap.get(que) + ",(" + answer + ")");
                    } else {
                        resultMap.put(que, "(" + answer + ")");
                    }
                } else { // 绿色
                    Core.rectangle(
                            sourceMat,
                            new Point(rectx.x, recty.y),
                            new Point(rectx.x + rectx.width, recty.y + recty.height),
                            new Scalar(0, 255, 0),
                            1);
                    // logger.info(que_answer + ":未涂");
                }
            }
        }

        // for (Object result : resultMap.keySet()) {
        for (int i = 1; i <= 100; i++) {
            // logger.info("key=" + result + " value=" + resultMap.get(result));
            resultValue.append("  " + i + "=" + (StringUtils.isEmpty(resultMap.get(i)) ? "未填写" : resultMap.get(i)));
            if (i % 5 == 0) {
                resultValue.append("<br>");
            }
        }
        destPath = Constants.PATH + Constants.DEST_IMAGE_PATH + "dtk15.png";
        Highgui.imwrite(destPath, sourceMat);
        logger.info("框选填图区域，绿色为选项，蓝色为填图，红色为临界======" + destPath);
        long t2 = new Date().getTime();
        LogUtils.info(t2 - t1);

        // logger.info("输出最终结果：" + resultValue.toString());

        renderString(response, resultValue.toString());
    }

    /** 绘制灰度直方图用于调整识别区域阈值判断 */
    public Mat getGrayHistogram(Mat img) {
        List<Mat> images = new ArrayList<Mat>();
        images.add(img);
        MatOfInt channels = new MatOfInt(0); // 图像通道数，0表示只有一个通道
        MatOfInt histSize = new MatOfInt(256); // CV_8U类型的图片范围是0~255，共有256个灰度级
        Mat histogramOfGray = new Mat(); // 输出直方图结果，共有256行，行数的相当于对应灰度值，每一行的值相当于该灰度值所占比例
        MatOfFloat histRange = new MatOfFloat(0, 255);
        Imgproc.calcHist(images, channels, new Mat(), histogramOfGray, histSize, histRange, false); // 计算直方图
        MinMaxLocResult minmaxLoc = Core.minMaxLoc(histogramOfGray);
        // 按行归一化
        // Core.normalize(histogramOfGray, histogramOfGray, 0, histogramOfGray.rows(),
        // Core.NORM_MINMAX, -1, new Mat());

        // 创建画布
        int histImgRows = 600;
        int histImgCols = 1300;
        LogUtils.info("---------" + histSize.get(0, 0)[0]);
        int colStep = (int) Math.floor(histImgCols / histSize.get(0, 0)[0]); // 舍去小数，不能四舍五入，有可能列宽不够
        Mat histImg = new Mat(histImgRows, histImgCols, CvType.CV_8UC3, new Scalar(255, 255, 255)); // 重新建一张图片，绘制直方图

        int max = (int) minmaxLoc.maxVal;
        LogUtils.info("--------" + max);
        BigDecimal bin_u = (BigDecimal) (histImgRows - 20) / max; // max: 最高条的像素个数，则 bin_u 为单个像素的高度，因为画直方图的时候上移了20像素，要减去
        int kedu = 0;
        for (int i = 1; kedu <= minmaxLoc.maxVal; i++) {
            kedu = i * max / 10;
            // 在图像中显示文本字符串
            Core.putText(histImg, kedu + "", new Point(0, histImgRows - kedu * bin_u), 1, 1, new Scalar(0, 0, 0));
        }

        for (int i = 0; i < histSize.get(0, 0)[0]; i++) { // 画出每一个灰度级分量的比例，注意OpenCV将Mat最左上角的点作为坐标原点
            // LogUtils.info(i + ":=====" + histogramOfGray.get(i, 0)[0]);
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
            kedu = i * 10;
            // 每隔10画一下刻度
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
                    new Point(colStep * kedu, histImgRows - 5),
                    1,
                    1,
                    new Scalar(255, 0, 0)); // 附上x轴刻度
        }

        return histImg;
    }

    // 获取题号及选项填涂情况
    public String getQA(int no, int an) {
        // 返回1A、1B、1C...2A类似这样的返回值
        int first = no + 1 + an / 4 * 20;
        String second = "";
        if (an % 4 == 0) {
            second = "A";
        } else if (an % 4 == 1) {
            second = "B";
        } else if (an % 4 == 2) {
            second = "C";
        } else if (an % 4 == 3) {
            second = "D";
        }
        return first + "_" + second;
    }

    /**
     * 红色色系0-20，160-180 蓝色色系100-120 绿色色系60-80 黄色色系23-38 识别出的颜色会标记为白色，其他的为黑色
     *
     * @param min
     * @param max
     */
    public static Mat findColorbyHSV(Mat source, int min, int max) {
        Mat hsv_image = new Mat();
        Imgproc.GaussianBlur(source, source, new Size(3, 3), 0, 0);
        Imgproc.cvtColor(source, hsv_image, Imgproc.COLOR_BGR2HSV);
        // String imagenameb = "D:\\test\\testImge\\ttbefore.jpg";
        // Highgui.imwrite(imagenameb, hsv_image);
        Mat thresholded = new Mat();
        Core.inRange(hsv_image, new Scalar(min, 90, 90), new Scalar(max, 255, 255), thresholded);
        return thresholded;
    }

    /**
     * 查找黑色
     *
     * @param source
     * @return
     */
    public static Mat findBlackColorbyHSV(Mat source) {
        Mat hsv_image = new Mat();
        Imgproc.GaussianBlur(source, source, new Size(3, 3), 0, 0);
        Imgproc.cvtColor(source, hsv_image, Imgproc.COLOR_BGR2HSV);
        String imagenameb = "D:\\test\\testImge\\ttbefore.jpg";
        Highgui.imwrite(imagenameb, hsv_image);
        Mat thresholded = new Mat();
        Core.inRange(hsv_image, new Scalar(0, 0, 0), new Scalar(180, 255, 46), thresholded);
        String ttblack = "D:\\test\\testImge\\ttblack.jpg";
        Highgui.imwrite(ttblack, thresholded);
        return thresholded;
    }

    /**
     * 水平投影
     *
     * @param source 传入灰度图片Mat
     * @return
     */
    public static Mat horizontalProjection(Mat source) {
        Mat dst = new Mat(source.rows(), source.cols(), source.type());
        // 先进行反转二值化
        Imgproc.threshold(source, dst, 150, 255, Imgproc.THRESH_BINARY_INV);
        // 水平积分投影
        // 每一行的白色像素的个数
        int[] rowswidth = new int[dst.rows()];
        for (int i = 0; i < dst.rows(); i++) {
            for (int j = 0; j < dst.cols(); j++) {
                if (dst.get(i, j)[0] == 255) {
                    rowswidth[i]++;
                }
            }
        }
        // 定义一个白色跟原图一样大小的画布
        Mat matResult = new Mat(dst.rows(), dst.cols(), CvType.CV_8UC1, new Scalar(255, 255, 255));
        // 将每一行按照行像素值大小填充像素宽度
        for (int i = 0; i < matResult.rows(); i++) {
            for (int j = 0; j < rowswidth[i]; j++) {
                matResult.put(i, j, 0);
            }
        }
        return matResult;
    }

    /**
     * 垂直投影
     *
     * @param source 传入灰度图片Mat
     * @return
     */
    public static Mat verticalProjection(Mat source) {
        // 先进行反转二值化
        Mat dst = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.threshold(source, dst, 150, 255, Imgproc.THRESH_BINARY_INV);
        // 垂直积分投影
        // 每一列的白色像素的个数
        int[] colswidth = new int[dst.cols()];
        for (int j = 0; j < dst.cols(); j++) {
            for (int i = 0; i < dst.rows(); i++) {
                if (dst.get(i, j)[0] == 255) {
                    colswidth[j]++;
                }
            }
        }
        Mat matResult = new Mat(dst.rows(), dst.cols(), CvType.CV_8UC1, new Scalar(255, 255, 255));
        // 将每一列按照列像素值大小填充像素宽度
        for (int j = 0; j < matResult.cols(); j++) {
            for (int i = 0; i < colswidth[j]; i++) {
                matResult.put(matResult.rows() - 1 - i, j, 0);
            }
        }
        return matResult;
    }

    /**
     * 图片切块
     *
     * @param srcImg 传入水平或垂直投影的图片对象Mat
     * @param proType 传入投影Mat对象的 投影方式0：垂直投影图片,竖向切割；1：水平投影图片，横向切割
     * @param rowXY 由于传来的是可能是原始图片的部分切片，要计算切块的实际坐标位置需要给出切片时所在的坐标，所以需要传递横向切片的y坐标或者纵向切片的横坐标
     *     如当proType==0时，传入的是切片的垂直投影，那么切成块后能得出x坐标及块宽高度，但是实际y坐标需要加上原切片的y坐标值，所以rowXY为切片的y坐标点，
     *     同理当proType==1时，rowXY应该为x坐标
     * @return
     */
    public static List<Rect> getBlockRect(Mat srcImg, Integer proType, int rowXY) {
        Imgproc.threshold(srcImg, srcImg, 150, 255, Imgproc.THRESH_BINARY_INV);
        // 注意 countNonZero 方法是获取非0像素（白色像素）数量，所以一般要对图像进行二值化反转
        List<Rect> rectList = new ArrayList<Rect>();
        int size = proType == 0 ? srcImg.cols() : srcImg.rows();
        int[] pixNum = new int[size];
        if (proType == 0) {
            for (int i = 0; i < srcImg.cols(); i++) {
                Mat col = srcImg.col(i);
                pixNum[i] = Core.countNonZero(col) > 1 ? Core.countNonZero(col) : 0;
            }
        } else { // 水平投影只关注行
            for (int i = 0; i < srcImg.rows(); i++) {
                Mat row = srcImg.row(i);
                pixNum[i] = Core.countNonZero(row) > 1 ? Core.countNonZero(row) : 0;
            }
        }
        int startIndex = 0; // 记录进入字符区的索引
        int endIndex = 0; // 记录进入空白区域的索引
        boolean inBlock = false; // 是否遍历到了字符区内
        for (int i = 0; i < size; i++) {
            if (!inBlock && pixNum[i] != 0) { // 进入字符区，上升跳变沿
                inBlock = true;
                startIndex = i;
            } else if (pixNum[i] == 0 && inBlock) { // 进入空白区，下降跳变沿存储
                endIndex = i;
                inBlock = false;
                Rect rect = null;
                if (proType == 0) {
                    rect = new Rect(startIndex, rowXY, (endIndex - startIndex), srcImg.rows());
                } else {
                    rect = new Rect(rowXY, startIndex, srcImg.cols(), (endIndex - startIndex));
                }
                rectList.add(rect);
            }
        }
        return rectList;
    }
}
