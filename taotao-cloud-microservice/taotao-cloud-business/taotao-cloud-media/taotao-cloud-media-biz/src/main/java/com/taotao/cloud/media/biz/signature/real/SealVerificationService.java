package com.taotao.cloud.media.biz.signature.real;

import com.taotao.cloud.media.biz.signature.DigitalSignatureService;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.PublicKey;

public class SealVerificationService {

    private final DigitalSignatureService digitalSignatureService;

    public SealVerificationService(DigitalSignatureService digitalSignatureService) {
        this.digitalSignatureService = digitalSignatureService;
    }

    // 验证电子印章
    public boolean verifySeal(File imageFile, File signatureFile) throws Exception {
        // 读取图像文件
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        // 将BufferedImage转换为字节数组
        byte[] imageData = convertBufferedImageToByteArray(bufferedImage);

        // 读取签名文件
        String signature = new String(Files.readAllBytes(signatureFile.toPath()), StandardCharsets.UTF_8);

        // 验证签名
        return digitalSignatureService.verifyData(imageData, signature);
    }

    // 转换BufferedImage为字节数组方法
    private byte[] convertBufferedImageToByteArray(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        }
    }
}
