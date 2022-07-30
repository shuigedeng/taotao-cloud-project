//package com.taotao.cloud.doc.pdf.util;
//
//import com.lvmoney.frame.base.core.constant.BaseConstant;
//import com.lvmoney.frame.core.util.FileUtil;
//import com.lvmoney.frame.office.common.constant.OfficeConstant;
//import com.lvmoney.frame.office.pdf.util.vo.Pic2PdfVo;
//import com.lvmoney.frame.office.pdf.util.vo.item.Pic2PdfVoItem;
//import com.itextpdf.text.BadElementException;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfStamper;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.rendering.PDFRenderer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PdfUtil {
//
//    /**
//     * word应用程序的programId
//     */
//    private static final String PROGRAM_ID = "KWPS.Application";
//    /**
//     * Visible 设置Word不可见
//     */
//    private static final String PROPERTY_VISIBLE = "Visible";
//
//    /**
//     * Documents 获得Word中所有打开的文档，返回documents对象
//     */
//    private static final String PROPERTY_DOCUMENTS = "Documents";
//
//    /**
//     * AutomationSecurity 禁用宏
//     */
//    private static final String PROPERTY_AUTO_SECURITY = "AutomationSecurity";
//
//    /**
//     * AutomationSecurity Variant
//     */
//    private static final Integer PROPERTY_AUTO_SECURITY_VARIANT = 3;
//
//    /**
//     * 调用 ExportAsFixedFormat
//     */
//    private static final String CALL_EXPORT_AS_FIXED_FORMAT = "ExportAsFixedFormat";
//
//    /**
//     * 调用 close
//     */
//    private static final String CALL_CLOSE = "Close";
//    /**
//     * 调用 quit
//     */
//    private static final String CALL_QUIT = "Quit";
//
//    /**
//     * quit 参数
//     */
//    private static final Integer QUIT_PARAM = 0;
//
//    /**
//     * 调用 Open
//     */
//    private static final String CALL_OPEN = "Open";
//    /**
//     * word保存为pdf格式宏，值为17
//     */
//    private static final int WORD_FORMAT_PDF = 17;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PdfUtil.class);
//    /**
//     * 尾页
//     */
//    private static final Integer PAGE_LAST = 9999;
//
//    /**
//     * 首页
//     */
//    private static final Integer PAGE_FIRST = 1;
//    /**
//     * DPI
//     */
//    private static final Integer WINDOWS_NATIVE_DPI = 144;
//    /**
//     * 线粗
//     */
//    private static final float LINE_BASIC_STROKE = 2.0f;
//
//    /**
//     * 线的颜色 r
//     */
//    private static final Integer LINE_COLOR_R = 193;
//
//    /**
//     * 线的颜色 g
//     */
//    private static final Integer LINE_COLOR_G = 193;
//    /**
//     * 线的颜色 b
//     */
//    private static final Integer LINE_COLOR_B = 193;
//
//    /**
//     * word 转化成 pdf
//     *
//     * @param res:
//     * @param target:
//     * @throws
//     * @return: void
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/10/14 18:15
//     */
//    public static void word2Pdf(String res, String target) {
//        try {
//            // 打开Word应用程序
//            ActiveXComponent app = new ActiveXComponent(PROGRAM_ID);
//            LOGGER.info("开始转化Word:{}为pdf:{}...", res, target);
//            // 设置Word不可见
//            app.setProperty(PROPERTY_VISIBLE, new Variant(false));
//            // 禁用宏
//            app.setProperty(PROPERTY_AUTO_SECURITY, new Variant(PROPERTY_AUTO_SECURITY_VARIANT));
//            // 获得Word中所有打开的文档，返回documents对象
//            Dispatch docs = app.getProperty(PROPERTY_DOCUMENTS).toDispatch();
//            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
//            Dispatch doc = Dispatch.call(docs, CALL_OPEN, res, false, true).toDispatch();
//            // word保存为pdf格式宏，值为17
//            Dispatch.call(doc, CALL_EXPORT_AS_FIXED_FORMAT, target, WORD_FORMAT_PDF);
//            Dispatch.call(doc, CALL_CLOSE, false);
//            // 关闭Word应用程序
//            app.invoke(CALL_QUIT, QUIT_PARAM);
//            LOGGER.info("结束转化Word:{}为pdf:{}...", res, target);
//        } catch (Exception e) {
//            LOGGER.error("Word:{}为pdf:{}报错:{}", e);
//        }
//
//    }
//
//    /**
//     * 插入图片到pdf
//     *
//     * @param pic2PdfVo:
//     * @throws
//     * @return: void
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/10/14 18:15
//     */
//    public static void picInsertPdf(Pic2PdfVo pic2PdfVo) {
//        Document document = null;
//        PdfStamper stamper = null;
//        PdfReader reader = null;
//        try {
//            InputStream input = FileUtil.byte2Input(pic2PdfVo.getPdf());
//            reader = new PdfReader(input);
//            //获取页数
//            int pageCount = reader.getNumberOfPages();
//
//            String targetPath = pic2PdfVo.getDirectory() + BaseConstant.FILE_SEPARATOR + pic2PdfVo.getPdfName() + OfficeConstant.PDF_SUFFIX;
//            stamper = new PdfStamper(reader, new FileOutputStream(targetPath));
//
//            document = new Document(reader.getPageSize(1));
//
//            // 获取页面宽度
//            float width = document.getPageSize().getWidth();
//
//            Integer pageNo = pic2PdfVo.getPageNo();
//            List<Pic2PdfVoItem> item = pic2PdfVo.getItem();
//            if (PAGE_FIRST.equals(pageNo)) {
//                PdfContentByte under = stamper.getOverContent(PAGE_FIRST);
//                item.forEach(e -> {
//                    Image image = pic2PdfVoItem2Image(e, width);
//                    try {
//                        under.addImage(image);
//                    } catch (DocumentException documentException) {
//                        documentException.printStackTrace();
//                    }
//                });
//
//            } else if (PAGE_LAST.equals(pageNo)) {
//                PdfContentByte under = stamper.getOverContent(pageCount);
//                item.forEach(e -> {
//                    Image image = pic2PdfVoItem2Image(e, width);
//                    try {
//                        under.addImage(image);
//                    } catch (DocumentException documentException) {
//                        documentException.printStackTrace();
//                    }
//                });
//            } else if (pageNo > pageCount || pageNo < PAGE_FIRST) {
//                LOGGER.error("pdf没有找到对应的页码:{}", pageNo);
//                return;
//            }
//
//
//        } catch (Exception e) {
//            LOGGER.error("图片插入pdf出错:{}", e);
//        } finally {
//            if (document != null) {
//                document.close();
//            }
//            if (stamper != null) {
//                try {
//                    stamper.close();
//                } catch (DocumentException e) {
//                    LOGGER.error("stamper关闭出错:{}", e);
//                } catch (IOException e) {
//                    LOGGER.error("关闭stamper Io出错:{}", e);
//                }
//            }
//            if (reader != null) {
//                reader.close();
//            }
//        }
//    }
//
//
//    public static final BufferedImage pdf2Png(InputStream input) {
//        PDFRenderer renderer = null;
//        try (PDDocument doc = PDDocument.load(input)) {
//            renderer = new PDFRenderer(doc);
//            int pageCount = doc.getNumberOfPages();
//            BufferedImage image = null;
//            for (int i = 0; i < pageCount; i++) {
//                if (image != null) {
//                    image = combineBufferedImages(image, renderer.renderImageWithDPI(i, WINDOWS_NATIVE_DPI));
//                }
//
//                if (i == 0) {
//                    // Windows jni DPI
//                    image = renderer.renderImageWithDPI(i, WINDOWS_NATIVE_DPI);
//                }
//
//            }
//            return combineBufferedImages(image);
//        } catch (IOException e) {
//            LOGGER.error("pdf 转换成png:{}", e);
//            return null;
//        }
//    }
//
//    /**
//     * BufferedImage拼接处理，添加分割线
//     *
//     * @param images:
//     * @throws
//     * @return: java.awt.image.BufferedImage
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/10/14 18:19
//     */
//    public static BufferedImage combineBufferedImages(BufferedImage... images) {
//        int height = 0;
//        int width = 0;
//        for (BufferedImage image : images) {
//            height += image.getHeight();
//            width = image.getWidth();
//        }
//        BufferedImage combo = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = combo.createGraphics();
//        int x = 0;
//        int y = 0;
//        for (BufferedImage image : images) {
//            // 线条粗细
//            g2.setStroke(new BasicStroke(LINE_BASIC_STROKE));
//            // 线条颜色
//            g2.setColor(new Color(LINE_COLOR_R, LINE_COLOR_G, LINE_COLOR_B));
//            // 线条起点及终点位置
//            g2.drawLine(x, y, width, y);
//
//            g2.drawImage(image, x, y, null);
//            //x += image.getWidth();
//            y += image.getHeight();
//
//        }
//        return combo;
//    }
//
//
//    /**
//     * 构造生成image
//     *
//     * @param pic2PdfVoItem:
//     * @param width:
//     * @throws
//     * @return: com.itextpdf.text.Image
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/10/14 18:15
//     */
//    private static Image pic2PdfVoItem2Image(Pic2PdfVoItem pic2PdfVoItem, float width) {
//        byte[] imageByte = pic2PdfVoItem.getImage();
//        try {
//            Image image = Image.getInstance(imageByte);
//            float picWidth = pic2PdfVoItem.getWidth();
//            float picHeight = pic2PdfVoItem.getWidth();
//            float x = pic2PdfVoItem.getX();
//            float y = pic2PdfVoItem.getY();
//            // 根据域的大小缩放图片
//            image.scaleToFit(picWidth, picHeight);
//            image.setAbsolutePosition(width - picWidth - x, y);
//            return image;
//        } catch (BadElementException e) {
//            LOGGER.error("构造pdf插入图片出错:{}", e);
//            return null;
//        } catch (IOException e) {
//            LOGGER.error("构造pdf插入图片时 IO出错:{}", e);
//            return null;
//        }
//
//    }
//
//
//    public static void main(String[] args) {
////        File file = new File("D:\\2019_PDF.pdf");
////        String htmlPath = "D:\\12121212.html";
////        InputStream inputStream = null;
////        BufferedImage bufferedImage = null;
////        try {
////            inputStream = new FileInputStream(file);
////            bufferedImage = PdfUtil.pdf2Png(inputStream);
////            String base64_png = FileUtil.bufferedImage2Base64(bufferedImage);
////            createHtmlByBase64(base64_png, htmlPath);
////
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } finally {
////            try {
////                if (inputStream != null) {
////                    inputStream.close();
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
//
//        word2Pdf("D:\\c2.docx", "D:\\test.pdf");
//        Pic2PdfVo pic2PdfVo = new Pic2PdfVo();
//        pic2PdfVo.setPdfName("test2");
//        pic2PdfVo.setPdf(FileUtil.file2byte("D:\\test.pdf"));
//        pic2PdfVo.setDirectory("D:\\");
//        pic2PdfVo.setPageNo(1);
//        List<Pic2PdfVoItem> item = new ArrayList<>();
//        Pic2PdfVoItem pic2PdfVoItem = new Pic2PdfVoItem();
//        pic2PdfVoItem.setImage(FileUtil.file2byte("D:\\images\\12.jpg"));
//        pic2PdfVoItem.setY(100f);
//        pic2PdfVoItem.setX(100f);
//        pic2PdfVoItem.setWidth(20f);
//        pic2PdfVoItem.setHeight(30f);
//        item.add(pic2PdfVoItem);
//        pic2PdfVo.setItem(item);
//        picInsertPdf(pic2PdfVo);
//    }
//
//    /**
//     * 通过Base64创建HTML文件并输出html文件
//     *
//     * @param base64:
//     * @param htmlPath:
//     * @throws
//     * @return: void
//     * @author: lvmoney /XXXXXX科技有限公司
//     * @date: 2021/10/14 18:42
//     */
//    public static void createHtmlByBase64(String base64, String htmlPath) {
//        StringBuilder stringHtml = new StringBuilder();
//        try (PrintStream printStream = new PrintStream(new FileOutputStream(htmlPath))) {
//            // 输入HTML文件内容
//            stringHtml.append("<html><head>");
//            stringHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
//            stringHtml.append("<title></title>");
//            stringHtml.append("</head>");
//            stringHtml.append(
//                    "<body style=\"\r\n" + "    text-align: center;\r\n" + "    background-color: #C1C1C1;\r\n" + "\">");
//            stringHtml.append("<img src=\"data:image/png;base64," + base64 + "\" />");
//            stringHtml.append("<a name=\"head\" style=\"position:absolute;top:0px;\"></a>");
//            //添加锚点用于返回首页
//            stringHtml.append("<a style=\"position:fixed;bottom:10px;right:10px\" href=\"#head\">回到首页</a>");
//            stringHtml.append("</body></html>");
//            printStream.println(stringHtml.toString());
//        } catch (FileNotFoundException e) {
//            LOGGER.error("通过Base64创建HTML文件并输出html文件报错:{}", e);
//        }
//
//
//    }
//}
