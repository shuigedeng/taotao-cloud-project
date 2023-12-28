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

package com.taotao.cloud.workflow.biz.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.Cleanup;

/** */
public class CodeUtil {
    /** 放到session中的key */
    public static final String RANDOMCODEKEY = "RANDOMCODEKEY";
    /** */
    private Random random = new Random();
    /** 随机产生的字符串 */
    private String randString = "abcdefghjklmnopqrstuvwxyz23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    /** 图片宽 */
    private int width = 120;
    /** 图片高 */
    private int height = 40;
    /** 干扰线数量 */
    private int lineSize = 10;
    /** 随机产生字符数量 */
    private int stringNum = 4;

    /** 获得字体 */
    private Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
    }

    /** 获得颜色 */
    private Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /** 生成随机图片 */
    public void getRandcode(HttpServletResponse response, Integer stringLength) {
        try {
            HttpSession session = ServletUtil.getSession();
            // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            // 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
            Graphics g = image.getGraphics();
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));
            g.setColor(getRandColor(110, 133));
            // 绘制干扰线
            for (int i = 0; i <= lineSize; i++) {
                drowLine(g);
            }
            // 绘制随机字符
            String randomString = "";
            if (Objects.nonNull(stringLength)) {
                stringNum = stringLength;
            }
            for (int i = 1; i <= stringNum; i++) {
                randomString = drowString(g, randomString, i);
            }
            session.removeAttribute(RANDOMCODEKEY);
            session.setAttribute(RANDOMCODEKEY, randomString);
            LogUtils.info("后台代码" + randomString);
            g.dispose();
            @Cleanup ServletOutputStream outputStream = response.getOutputStream();
            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "JPEG", outputStream);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /** 绘制字符串 */
    private String drowString(Graphics g, String randomString, int i) {
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
        String rand = String.valueOf(getRandomString(random.nextInt(randString.length())));
        randomString += rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 13 * i, 16);
        return randomString;
    }

    /** 绘制干扰线 */
    private void drowLine(Graphics g) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /** 获取随机的字符 */
    public String getRandomString(int num) {
        return String.valueOf(randString.charAt(num));
    }
}
