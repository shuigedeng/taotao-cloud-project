//package com.taotao.cloud.doc.picture.util;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.apache.batik.transcoder.Transcoder;
//import org.apache.batik.transcoder.TranscoderInput;
//import org.apache.batik.transcoder.TranscoderOutput;
//import org.apache.batik.transcoder.image.JPEGTranscoder;
//import org.apache.batik.transcoder.image.PNGTranscoder;
//import org.apache.fop.svg.PDFTranscoder;
//
//import java.io.*;
//
//public class SvgUtil {
//
//    /**
//     * svg文件转成PDF
//     *
//     * @param svg:
//     * @param pdf:
//     * @throws
//     * @return: void
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/12/15 13:55
//     */
//    public static void convert2Pdf(File svg, File pdf) {
//        OutputStream outs = null;
//        InputStream ins = null;
//        try {
//            outs = new BufferedOutputStream(new FileOutputStream(pdf));
//            ins = new FileInputStream(svg);
//            Transcoder transcoder = new PDFTranscoder();
//            TranscoderInput input = new TranscoderInput(ins);
//            TranscoderOutput output = new TranscoderOutput(outs);
//            transcoder.transcode(input, output);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                ins.close();
//                outs.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * svg转为png
//     *
//     * @param svg:
//     * @param jpeg:
//     * @throws
//     * @return: void
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/12/15 13:55
//     */
//    public static void convert2Jpeg(File svg, File jpeg) {
//        InputStream ins = null;
//        OutputStream outs = null;
//        try {
//            ins = new FileInputStream(svg);
//            outs = new BufferedOutputStream(new FileOutputStream(jpeg));
//            Transcoder transcoder = new JPEGTranscoder();
//            //为防止ERROR: The JPEG quality has not been specified. Use the default one: no compression 错误，需如下配置
//            transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 0.99f);
//            TranscoderInput input = new TranscoderInput(ins);
//            TranscoderOutput output = new TranscoderOutput(outs);
//            transcoder.transcode(input, output);
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        } finally {
//            try {
//                ins.close();
//                outs.close();
//            } catch (Exception ee) {
//                // TODO: handle exception
//                ee.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * svg转为png
//     *
//     * @param svg:
//     * @param png:
//     * @throws
//     * @return: void
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/12/15 13:55
//     */
//    public static void convert2Png(File svg, File png) {
//        InputStream ins = null;
//        OutputStream outs = null;
//        try {
//            ins = new FileInputStream(svg);
//            outs = new BufferedOutputStream(new FileOutputStream(png));
//            Transcoder transcoder = new PNGTranscoder();
//            //为防止图片转换时出现图片大小和SVG设置的大小不一致,需要在转换时设置图片大小  2450和150是SVG中width和height
////				transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(2450.0));
////				transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Float(150.0));
//            TranscoderInput input = new TranscoderInput(ins);
//            TranscoderOutput output = new TranscoderOutput(outs);
//            transcoder.transcode(input, output);
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        } finally {
//            try {
//                ins.close();
//                outs.close();
//            } catch (Exception ee) {
//                // TODO: handle exception
//                ee.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 字符串转成pdf
//     *
//     * @param svg:
//     * @param pdf:
//     * @throws
//     * @return: void
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/12/15 13:55
//     */
//    public static void convertStr2Pdf(String svg, File pdf) {
//        OutputStream outs = null;
//        InputStream ins = null;
//        try {
//            outs = new BufferedOutputStream(new FileOutputStream(pdf));
//            ins = new ByteArrayInputStream(svg.getBytes());
//            Transcoder transcoder = new PDFTranscoder();
//            TranscoderInput input = new TranscoderInput(ins);
//            TranscoderOutput output = new TranscoderOutput(outs);
//            transcoder.transcode(input, output);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                ins.close();
//                outs.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//
//        File file = new File("g://score_0.svg");
//        File pdf = new File("g://score_0.pdf");
//        File png = new File("g://score_0.png");
//        File jpeg = new File("g://score.jpg");
//
//        SvgUtil.convert2Pdf(file, pdf);
//        //  SvgUtil.convert2Png(file, png);
//        //    SvgUtil.convert2Jpeg(file,jpeg);
//    }
//
//}
