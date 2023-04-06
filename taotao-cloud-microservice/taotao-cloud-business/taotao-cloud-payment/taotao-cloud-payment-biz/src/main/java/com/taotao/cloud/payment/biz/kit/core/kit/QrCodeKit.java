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

package com.taotao.cloud.payment.biz.kit.core.kit;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;

/** google 开源图形码工具类 */
public class QrCodeKit {
    /**
     * 图形码生成工具
     *
     * @param contents 内容
     * @param barcodeFormat BarcodeFormat对象
     * @param format 图片格式，可选[png,jpg,bmp]
     * @param width 宽
     * @param height 高
     * @param margin 边框间距px
     * @param saveImgFilePath 存储图片的完整位置，包含文件名
     * @return {boolean}
     */
    public static boolean encode(
            String contents,
            BarcodeFormat barcodeFormat,
            Integer margin,
            ErrorCorrectionLevel errorLevel,
            String format,
            int width,
            int height,
            String saveImgFilePath) {
        boolean bool = false;
        BufferedImage bufImg;
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(3);
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, errorLevel);
        hints.put(EncodeHintType.MARGIN, margin);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, barcodeFormat, width, height, hints);
            MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
            bufImg = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
            bool = writeToFile(bufImg, format, saveImgFilePath);
        } catch (Exception e) {
            LogUtils.error("图形码生成工具生成错误", e);
        }
        return bool;
    }

    /**
     * @param srcImgFilePath 要解码的图片地址
     * @return {Result}
     */
    public static Result decode(String srcImgFilePath) {
        Result result = null;
        BufferedImage image;
        try {
            File srcFile = new File(srcImgFilePath);
            image = ImageIO.read(srcFile);
            if (null != image) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
                result = new MultiFormatReader().decode(bitmap, hints);
            } else {
                throw new IllegalArgumentException("Could not decode image.");
            }
        } catch (Exception e) {
            LogUtils.error("图片解码错误", e);
        }
        return result;
    }

    /**
     * 将BufferedImage对象写入文件
     *
     * @param bufImg BufferedImage对象
     * @param format 图片格式，可选[png,jpg,bmp]
     * @param saveImgFilePath 存储图片的完整位置，包含文件名
     * @return {boolean}
     */
    public static boolean writeToFile(BufferedImage bufImg, String format, String saveImgFilePath) {
        boolean bool = false;
        try {
            bool = ImageIO.write(bufImg, format, new File(saveImgFilePath));
        } catch (Exception e) {
            LogUtils.error("将BufferedImage对象写入文件错误", e);
        }
        return bool;
    }
}
