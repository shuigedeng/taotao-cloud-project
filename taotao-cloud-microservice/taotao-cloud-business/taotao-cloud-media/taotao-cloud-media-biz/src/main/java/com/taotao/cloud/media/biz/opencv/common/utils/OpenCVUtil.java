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

package com.taotao.cloud.media.biz.opencv.common.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class OpenCVUtil {
    public static BufferedImage covertMat2Buffer(Mat mat) throws IOException {
        long time1 = new Date().getTime();
        // Mat 转byte数组
        BufferedImage originalB = toBufferedImage(mat);
        long time3 = new Date().getTime();
        LogUtils.info("保存读取方法2转=" + (time3 - time1));
        return originalB;
        // ImageIO.write(originalB, "jpg", new File("D:\\test\\testImge\\ws2.jpg"));
    }

    public static byte[] covertMat2Byte(Mat mat) throws IOException {
        long time1 = new Date().getTime();
        // Mat 转byte数组
        byte[] return_buff = new byte[(int) (mat.total() * mat.channels())];
        Mat mat1 = new Mat();
        mat1.get(0, 0, return_buff);
        long time3 = new Date().getTime();
        LogUtils.info(mat.total() * mat.channels());
        LogUtils.info("保存读取方法2转=" + (time3 - time1));
        return return_buff;
    }

    public static byte[] covertMat2Byte1(Mat mat) throws IOException {
        long time1 = new Date().getTime();
        MatOfByte mob = new MatOfByte();
        Highgui.imencode(".jpg", mat, mob);
        long time3 = new Date().getTime();
        // LogUtils.info(mat.total() * mat.channels());
        LogUtils.info("Mat转byte[] 耗时=" + (time3 - time1));
        return mob.toArray();
    }

    public static BufferedImage toBufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    /**
     * 腐蚀膨胀是针对于白色区域来说的，腐蚀即腐蚀白色区域 腐蚀算法（黑色区域变大）
     *
     * @param source
     * @return
     */
    public static Mat eroding(Mat source) {
        return eroding(source, 1);
    }

    public static Mat eroding(Mat source, BigDecimal erosion_size) {
        Mat resultMat = new Mat(source.rows(), source.cols(), source.type());
        Mat element =
                Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * erosion_size + 1, 2 * erosion_size + 1));
        Imgproc.erode(source, resultMat, element);
        return resultMat;
    }

    /**
     * 腐蚀膨胀是针对于白色区域来说的，膨胀是膨胀白色区域 膨胀算法（白色区域变大）
     *
     * @param source
     * @return
     */
    public static Mat dilation(Mat source) {
        return dilation(source, 1);
    }

    /**
     * 腐蚀膨胀是针对于白色区域来说的，膨胀是膨胀白色区域
     *
     * @param source
     * @param dilationSize 膨胀因子2*x+1 里的x
     * @return Mat
     */
    public static Mat dilation(Mat source, BigDecimal dilation_size) {
        Mat resultMat = new Mat(source.rows(), source.cols(), source.type());
        Mat element = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT, new Size(2 * dilation_size + 1, 2 * dilation_size + 1));
        Imgproc.dilate(source, resultMat, element);
        return resultMat;
    }

    /**
     * 轮廓识别，使用最外轮廓发抽取轮廓RETR_EXTERNAL，轮廓识别方法为CHAIN_APPROX_SIMPLE
     *
     * @param source 传入进来的图片Mat对象
     * @return 返回轮廓结果集
     */
    public static Vector<MatOfPoint> findContours(Mat source) {
        Mat rs = new Mat();
        /**
         * 定义轮廓抽取模式 RETR_EXTERNAL:只检索最外面的轮廓; RETR_LIST:检索所有的轮廓，并将其放入list中;
         * RETR_CCOMP:检索所有的轮廓，并将他们组织为两层:顶层是各部分的外部边界，第二层是空洞的边界; RETR_TREE:检索所有的轮廓，并重构嵌套轮廓的整个层次。
         */
        int mode = Imgproc.RETR_EXTERNAL;
        // int mode = Imgproc.RETR_TREE;
        /**
         * 定义轮廓识别方法 边缘近似方法(除了RETR_RUNS使用内置的近似，其他模式均使用此设定的近似算法)。可取值如下:
         * CV_CHAIN_CODE:以Freeman链码的方式输出轮廓，所有其他方法输出多边形(顶点的序列)。 CHAIN_APPROX_NONE:将所有的连码点，转换成点。
         * CHAIN_APPROX_SIMPLE:压缩水平的、垂直的和斜的部分，也就是，函数只保留他们的终点部分。
         * CHAIN_APPROX_TC89_L1，CV_CHAIN_APPROX_TC89_KCOS:使用the flavors of Teh-Chin chain近似算法的一种。
         * LINK_RUNS:通过连接水平段的1，使用完全不同的边缘提取算法。使用CV_RETR_LIST检索模式能使用此方法。
         */
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Vector<MatOfPoint> contours = new Vector<MatOfPoint>();
        Imgproc.findContours(source, contours, rs, mode, method, new Point());
        return contours;
    }
}
