package com.taotao.cloud.office.convert.config;

import cn.hutool.core.io.FileUtil;
import com.aspose.words.License;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * <p>
 * `aspose-words`授权处理
 * </p>
 *
 * @description
 * @since 2020/12/30$ 11:33$
 */
public class MatchLicense {

	public static void init() {
		try {
			LogUtils.info("实现`aspose-words`授权 -> 去掉头部水印");
            /*
              实现匹配文件授权 -> 去掉头部水印 `Evaluation Only. Created with Aspose.Words. Copyright 2003-2018 Aspose Pty Ltd.` |
                                          `Evaluation Only. Created with Aspose.Cells for Java. Copyright 2003 - 2020 Aspose Pty Ltd.`
             */
			InputStream is = new ClassPathResource("license.xml").getInputStream();
			License license = new License();
			license.setLicense(is);

			//临时写法
			FileUtil.mkdir(Constants.DEFAULT_FOLDER_TMP_GENERATE);
		} catch (Exception e) {
			LogUtils.error("《`aspose-words`授权》 失败： {}", e.getMessage());
		}
	}

}
