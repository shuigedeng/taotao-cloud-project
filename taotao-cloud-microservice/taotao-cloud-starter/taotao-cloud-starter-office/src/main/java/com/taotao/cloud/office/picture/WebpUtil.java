//package com.taotao.cloud.doc.picture.util;
//
//
//import com.lvmoney.frame.core.util.FileUtil;
//import org.apache.commons.io.IOUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.imageio.IIOImage;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageReader;
//import javax.imageio.ImageWriter;
//import javax.imageio.stream.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//
//public class WebpUtil {
//    private static final Logger LOGGER = LoggerFactory.getLogger(WebpUtil.class);
//
//
//    public static void main(String[] args) throws IOException {
//        String inputJpgPath = "D:/data/images/test.jpg";
//        String outputWebpPath = "D:/data/images/test4_.webp";
//        File file = new File(inputJpgPath);
//        InputStream stream = new FileInputStream(file);
//        File outFile = new File(outputWebpPath);
//        OutputStream os = new FileOutputStream(outFile);
//        BufferedOutputStream bos = new BufferedOutputStream(os);
//        byte[] isToByte = image2Webp(stream);
//        bos.write(isToByte);
//
//        InputStream stream2 = new FileInputStream(outputWebpPath);
//        byte[] pngs = webp2Image(stream2, "png");
//        String outputWebpPath2 = "D:/data/images/test5_.png";
//        File outFile2 = new File(outputWebpPath2);
//        OutputStream os2 = new FileOutputStream(outFile2);
//        BufferedOutputStream bos2 = new BufferedOutputStream(os2);
//        bos2.write(pngs);
//
//
//    }
//
//    /**
//     * 将文件流（图片）转换成*.webp的二进制流
//     *
//     * @param stream:
//     * @throws
//     * @return: byte[]
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2022/7/29 14:54
//     */
//    public static byte[] image2Webp(InputStream stream) {
//        BufferedImage image = null;
//        try {
//            image = ImageIO.read(stream);
//        } catch (IOException e) {
//            LOGGER.error("imputstream 无法转化成 BufferedImage");
//            return null;
//        }
//        ImageWriter writer = ImageIO.getImageWritersByMIMEType(OfficeConstant.IMAGE_TYPE_WEBP).next();
//        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
//        writeParam.setCompressionMode(WebPWriteParam.MODE_DEFAULT);
//        OutputStream outputStream = new ByteArrayOutputStream();
//        MemoryCacheImageOutputStream memoryCacheImageOutputStream = new MemoryCacheImageOutputStream(outputStream);
//        writer.setOutput(memoryCacheImageOutputStream);
//        try {
//            writer.write(null, new IIOImage(image, null, null), writeParam);
//        } catch (IOException e) {
//            LOGGER.error("将转换好的*.webp文件写入MemoryCacheImageOutputStream失败:{}", e);
//            return null;
//        }
//        try {
//            memoryCacheImageOutputStream.flush();
//        } catch (IOException e) {
//            LOGGER.error("将MemoryCacheImageOutputStream写入OutputStream失败:{}", e);
//            return null;
//        }
//        ByteArrayInputStream parse = FileUtil.parse(outputStream);
//
//        try {
//            byte[] result = IOUtils.toByteArray(parse);
//            return result;
//        } catch (IOException e) {
//            LOGGER.error("文件流转化成字节流失败:{}", e);
//            return null;
//        }
//    }
//
//    /**
//     * 将*.webp文件转换成指定类型图片文件
//     *
//     * @param stream:
//     * @param type:
//     * @throws
//     * @return: byte[]
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2022/7/29 15:03
//     */
//    public static byte[] webp2Image(InputStream stream, String type) {
//        MemoryCacheImageInputStream input = new MemoryCacheImageInputStream(stream);
//        ImageReader reader = ImageIO.getImageReadersByMIMEType(OfficeConstant.IMAGE_TYPE_WEBP).next();
//        WebPReadParam readParam = new WebPReadParam();
//        readParam.setBypassFiltering(true);
//
//        // Configure the input on the ImageReader
//        reader.setInput(input);
//
//        // Decode the image
//        try {
//            BufferedImage image = reader.read(0, readParam);
//            byte[] bytes = FileUtil.bufferedImage2Bytes(image, type);
//            return bytes;
//
//        } catch (IOException e) {
//            LOGGER.error("将*.webp文件转换成指定文件类型{}不成功:{}", type, e);
//            return null;
//        }
//
//    }
//
//
//}
